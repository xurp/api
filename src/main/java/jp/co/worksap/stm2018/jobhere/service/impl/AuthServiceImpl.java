package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.ApiTokenRepository;
import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ForbiddenException;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.ApiToken;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.RegisterDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ApiTokenRepository apiTokenRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    AuthServiceImpl(UserRepository userRepository, ApiTokenRepository apiTokenRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.apiTokenRepository = apiTokenRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    @Override
    public ApiTokenDTO login(LoginDTO loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new ForbiddenException("Wrong password or username");
        }
        if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            String tokenId = user.getUsername() + "stm" + user.getRole();
            Optional<ApiToken> apiTokenOptional = apiTokenRepository.findById(tokenId);
            if (!apiTokenOptional.isPresent()) {
                ApiToken apiToken = new ApiToken();
                apiToken.setId(tokenId);
                apiToken.setUser(user);
                apiTokenRepository.save(apiToken);
                return ApiTokenDTO.builder()
                        .token(apiToken.getId())
                        .build();
            }
            return ApiTokenDTO.builder()
                    .token(apiTokenOptional.get().getId())
                    .build();
        } else {
            throw new ForbiddenException("Wrong password or username");
        }
    }

    @Transactional
    @Override
    public ApiTokenDTO register(RegisterDTO registerDTO) {

        User user = userRepository.findByUsername(registerDTO.getUsername());
        if (user != null) {
            throw new ValidationException("Username Existed!");
        } else {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String tokenId = registerDTO.getUsername() + "stm" + registerDTO.getRole();
            ApiToken apiToken = new ApiToken();
            apiToken.setId(tokenId);
            if (registerDTO.getRole().equals("candidate")) {

                Resume resume = Resume.builder()
                        .name(registerDTO.getUsername())
                        .email(registerDTO.getEmail()).build();

                User userToSave = User.builder()
                        .id(uuid)
                        .username(registerDTO.getUsername())
                        .role("candidate")
                        .email(registerDTO.getEmail()).resume(resume).build();
                userToSave.setPassword(registerDTO.getPassword());
                userRepository.save(userToSave);
                apiToken.setUser(userToSave);
                apiTokenRepository.save(apiToken);
            } else {
                //save one and many at one time(many not exists, else use many to save one)
                Company c = companyRepository.findFirstByCompanyNameAndLegalPerson(registerDTO.getCompanyName().trim(), registerDTO.getLegalPerson().trim());
                if (c == null || c.getStatus().equals("company-n")) {//this is a new company or the old company is company-n, insert new company
                    Company company = new Company();
                    company.setId(UUID.randomUUID().toString().replace("-", ""));
                    company.setCompanyName(registerDTO.getCompanyName());
                    company.setLegalPerson(registerDTO.getLegalPerson());
                    company.setStatus("company-n");
                    User userToSave = User.builder()
                            .id(uuid)
                            .username(registerDTO.getUsername())
                            .role("hr-n")
                            .email(registerDTO.getEmail()).build();
                    userToSave.setPassword(registerDTO.getPassword());

                    userToSave.setCompany(company);
                    company.addUser(userToSave);
                    companyRepository.save(company);
                    //I think the following save is unnecessary
                    userRepository.save(userToSave);
                    apiToken.setUser(userToSave);
                    apiTokenRepository.save(apiToken);
                } else {//there is a real company
                    User userToSave = User.builder()
                            .id(uuid)
                            .username(registerDTO.getUsername())
                            .role("hr-n")
                            .email(registerDTO.getEmail()).company(c).build();
                    userToSave.setPassword(registerDTO.getPassword());
                    c.addUser(userToSave);
                    companyRepository.save(c);
                }
            }
            return ApiTokenDTO.builder()
                    .token(apiToken.getId())
                    .build();
        }
    }

    @Override
    public User getUserByToken(String token) {
        Optional<ApiToken> apiTokenOptional = apiTokenRepository.findById(token);
        if (apiTokenOptional.isPresent()) {
            ApiToken apiToken = apiTokenOptional.get();
            return apiToken.getUser();
        }
        return null;
    }

    @Override
    public Optional<ApiToken> findById(String token) {
        return apiTokenRepository.findById(token);
    }
}
