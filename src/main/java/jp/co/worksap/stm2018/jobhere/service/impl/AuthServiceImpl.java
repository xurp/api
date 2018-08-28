package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.ApiTokenRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ForbiddenException;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.model.domain.ApiToken;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ApiTokenRepository apiTokenRepository;

    @Autowired
    AuthServiceImpl(UserRepository userRepository, ApiTokenRepository apiTokenRepository) {
        this.userRepository = userRepository;
        this.apiTokenRepository = apiTokenRepository;
    }

    @Override
    public ApiTokenDTO login(LoginDTO loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new NotFoundException();
        }
        if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            String tokenId=user.getUsername()+"stm"+user.getRole();
            Optional<ApiToken> apiTokenOptional = apiTokenRepository.findById(tokenId);
            if(!apiTokenOptional.isPresent()){
                ApiToken apiToken = new ApiToken();
                apiToken.setId(tokenId);
                apiToken.setUser(user);
                //apiToken.builder().id(tokenId).user(user);
                apiTokenRepository.save(apiToken);
                return ApiTokenDTO.builder()
                        .token(apiToken.getId())
                        .build();
            }
            // TODO: create and save newly generated apiToken
            return ApiTokenDTO.builder()
                    .token(apiTokenOptional.get().getId())
                    .build();
        } else {
            throw new ForbiddenException("Wrong password or username");
        }
    }

    @Override
    public User getUserByToken(String token) {
        System.out.println("111");
        Optional<ApiToken> apiTokenOptional = apiTokenRepository.findById(token);
        if(apiTokenOptional.isPresent()){
            ApiToken apiToken=apiTokenOptional.get();
            return apiToken.getUser();
        }
        return null;
    }

    @Override
    public Optional<ApiToken> findById(String token) {
        return apiTokenRepository.findById(token);
    }
}
