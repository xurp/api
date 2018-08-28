package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.UserDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("")
    @NeedLogin
    UserDTO showStatus(HttpServletRequest request) {
        String token = String.valueOf(request.getHeader("Api-Token"));
        // TODO: match token with ApiTokenRepository
        // TODO: throw NotLoginException if not login (no use-able token found)
        return null;
    }

    @PostMapping("")
    ApiTokenDTO login(@RequestBody LoginDTO loginDto) {
        return authService.login(loginDto);
    }
}
