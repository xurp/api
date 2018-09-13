package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/hr")
    @NeedLogin
    DashboardDTO find(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            DashboardDTO res = dashboardService.find(user.getId());
            return res;
        } else {
            throw new ValidationException("Permission Denied!");
        }

    }
}
