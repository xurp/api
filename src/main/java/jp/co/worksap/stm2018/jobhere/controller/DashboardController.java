package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.DashboardService;
import jp.co.worksap.stm2018.jobhere.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

            return dashboardService.find(user.getId());
        } else {
            throw new ValidationException("Permission Denied!");
        }

    }


    /***
     * 批量导出表记录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
//    @NeedLogin
    public void exportSurface(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExcelUtils.exportRecruitRecord(request, response, dashboardService.export("e98a28756cae4e648d70b02e42aa5ae0"));

//        User user = (User) request.getAttribute("getuser");
//        if (user.getRole().equals("hr")) {
//            ExcelUtils.exportRecruitRecord(request, response, dashboardService.export(user.getId()));
//        } else {
//            throw new ValidationException("Permission Denied!");
//        }
    }

}
