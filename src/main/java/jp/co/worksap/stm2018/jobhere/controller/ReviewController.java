package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.NotLoginException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.CooperatorDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.UserDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final AuthService authService;

    @Autowired
    public ReviewController(ReviewService reviewService, AuthService authService) {
        this.reviewService = reviewService;
        this.authService = authService;
    }


    @GetMapping("")
    List<UserDTO> list(HttpServletRequest request) throws LoginException {
        String token = String.valueOf(request.getHeader("Api-Token"));
        if (token == null)
            throw new NotLoginException();
        User user = authService.getUserByToken(token);
        return reviewService.list(user);
    }

    @GetMapping("/cooperator")
    @NeedLogin
    List<CooperatorDTO> listCooperator(HttpServletRequest request) {

        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            return reviewService.listCooperator();
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

    @PostMapping("/{id}")
    void inspect(HttpServletRequest request, @PathVariable("id") String id, @RequestBody String pass) throws LoginException {
        String token = String.valueOf(request.getHeader("Api-Token"));
        pass = pass.replace("=", "");
        if (token == null)
            throw new NotLoginException();
        User inspector = authService.getUserByToken(token);
        reviewService.inspect(inspector, id, pass);
    }

}
