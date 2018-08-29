package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.RegisterDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.UserDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
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
    UserDTO showStatus(HttpServletRequest request) throws Exception{
        String token = String.valueOf(request.getHeader("Api-Token"));
        if (token == null)
            throw new LoginException();
        User user=authService.getUserByToken(token);
        if(user==null)
            return null;
        // TODO: match token with ApiTokenRepository
        // TODO: throw NotLoginException if not login (no use-able token found)
        return UserDTO.builder().id(user.getId()).username(user.getUsername()).role(user.getRole()).build();
    }

    @GetMapping("/test")
    UserDTO showtest(HttpServletRequest request) {
        return UserDTO.builder().id("1").build();
    }

    @PostMapping("")
    ApiTokenDTO login(@RequestBody LoginDTO loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    ApiTokenDTO register(@RequestBody RegisterDTO registerDTO) {

        return authService.register(registerDTO);
    }


}
