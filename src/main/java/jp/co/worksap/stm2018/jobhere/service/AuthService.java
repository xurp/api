package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.ApiToken;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;

import java.util.Optional;

public interface AuthService {

    ApiTokenDTO login(LoginDTO loginDto);

    User getUserByToken(String token);

    Optional<ApiToken> findById(String token);
}
