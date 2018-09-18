package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.ScheduleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    ScheduleController(ScheduleService scheduleService) {
        this.scheduleService=scheduleService;
    }

    @RequestMapping(value = "/assessment", method = RequestMethod.GET)
    @NeedLogin
    public List<AssessmentDTO> getAssesements(HttpServletRequest request) throws Exception {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("candidate")) {
            return scheduleService.getAssessments(user.getId());
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    @NeedLogin
    public void getAppointments(HttpServletRequest request) throws Exception {
//        ExcelUtils.exportRecruitRecord(request, response, dashboardService.export("e98a28756cae4e648d70b02e42aa5ae0"));

        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
//            ExcelUtils.exportRecruitRecord(request, response, dashboardService.export(user.getId()));
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }
}
