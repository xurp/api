package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ForbiddenException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.UserDTO;
import jp.co.worksap.stm2018.jobhere.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    ReviewServiceImpl(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<UserDTO> list(User user) {
        List<UserDTO> userDTOList = new ArrayList<>();

        if (user.getRole().equals("hr")) {
            List<User> userList = user.getCompany().getUsers();
            for (User u : userList) {
                if (u.getRole().equals("hr-n")) {
                    userDTOList.add(UserDTO.builder()
                            .id(u.getId())
                            .username(u.getUsername())
                            .email(u.getEmail())
                            .role(u.getRole())
                            .companyName(u.getCompany().getCompanyName())
                            .legalPerson(u.getCompany().getLegalPerson())
                            .build());
                }
            }

        } else if (user.getRole().equals("admin")) {
            List<Company> companyList = companyRepository.findByStatus("company-n");
            for (Company c : companyList) {
                for (User u : c.getUsers()) {
                    if (u.getRole().equals("hr-n")) {
                        userDTOList.add(UserDTO.builder()
                                .id(u.getId())
                                .username(u.getUsername())
                                .email(u.getEmail())
                                .role(u.getRole())
                                .companyName(u.getCompany().getCompanyName())
                                .legalPerson(u.getCompany().getLegalPerson())
                                .build());
                    }
                }
            }
        }

        return userDTOList;
    }

    @Override
    public void inspect(User inspector, String id, String pass) {
        String inspectorRole = inspector.getRole();
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new ValidationException("ID Not Found!");
        }
        User user = userOptional.get();

        if (inspectorRole.equals("admin")) {
            if (pass.equals("true")) {
                Company company = user.getCompany();
                user.setRole("hr");
                company.setStatus("company");
                List<Company> companyList = companyRepository.findByStatus("company-n");
                for (Company c : companyList) {
                    if (c.getCompanyName().equals(company.getCompanyName()) && c.getLegalPerson().equals(company.getLegalPerson())) {
                        companyRepository.delete(c);
                    }
                }
                userRepository.save(user);
                companyRepository.save(company);
            } else if (pass.equals("false")) {
                userRepository.delete(user);
            }
        } else if (inspectorRole.equals("hr")) {
            if (inspector.getCompany().equals(user.getCompany())) {
                if (pass.equals("true")) {
                    user.setRole("hr");
                    user.setCompany(inspector.getCompany());//?
                    userRepository.save(user);
                } else if (pass.equals("false")) {
                    userRepository.delete(user);
                }
            }
        } else {
            throw new ForbiddenException("Not Authorized!");
        }


    }
}
