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
            ApiToken apiToken = new ApiToken();
            // TODO: create and save newly generated apiToken
            return ApiTokenDTO.builder()
                    .token(apiToken.getId())
                    .build();
        } else {
            throw new ForbiddenException("Wrong password or username");
        }
    }
}
