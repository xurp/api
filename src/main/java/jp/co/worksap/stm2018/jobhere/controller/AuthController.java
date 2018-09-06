package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.NotLoginException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.RegisterDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.UserDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.JobService;
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
            throw new NotLoginException();
        User user=authService.getUserByToken(token);
        if(user==null)
            return null;
        return UserDTO.builder().id(user.getId()).username(user.getUsername()).role(user.getRole()).build();
    }

    @PostMapping("")
    ApiTokenDTO login(HttpServletRequest request,@RequestBody LoginDTO loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    ApiTokenDTO register(@RequestBody RegisterDTO registerDTO) {

            return authService.register(registerDTO);
    }



}
