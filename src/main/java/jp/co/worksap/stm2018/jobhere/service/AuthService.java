package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;

public interface AuthService {

    ApiTokenDTO login(LoginDTO loginDto);
}
