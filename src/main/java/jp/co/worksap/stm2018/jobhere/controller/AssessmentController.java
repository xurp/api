package jp.co.worksap.stm2018.jobhere.controller;

import io.swagger.annotations.ApiOperation;
import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApplicationAndAssessmentDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import jp.co.worksap.stm2018.jobhere.service.AssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/3.
 */
@Slf4j
@RestController
@RequestMapping("/assessment")
public class AssessmentController {
    private final ApplicationService applicationService;
    private final ResumeService resumeService;
    private final JobService jobService;
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(ApplicationService applicationService, ResumeService resumeService, JobService jobService, AssessmentService assessmentService) {
        this.applicationService = applicationService;
        this.resumeService = resumeService;
        this.jobService = jobService;
        this.assessmentService = assessmentService;
    }

    //@ApiOperation(value="create new assessment, send email and update step of application", notes="when step is +")
    @ApiOperation(value = "save outbox and send email of selecting time", notes = "when step is +")
    @PostMapping("")
    @NeedLogin
    void save(HttpServletRequest request, @RequestBody EmailDTO emailDTO) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            //hr views application detail then click 'send email'
            //create new assessment, send email and update step of application
            //assessmentService.save(emailDTO.getApplicationId(), emailDTO.getCooperatorId(),emailDTO.getSubject(),emailDTO.getContent(),emailDTO.getAssessId());

            //after choose date and time module
            assessmentService.saveOutboxAndMakeAppointment(emailDTO);
        } else {
            log.warn("Permission Denied!");
            throw new ValidationException("Permission Denied!");
        }
    }


    @GetMapping("")
    @NeedLogin
    List<AssessmentDTO> list(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            String applicationId = request.getParameter("applicationId");
            return assessmentService.list(applicationId);
        } else {
            log.warn("Permission Denied!");
            throw new ValidationException("Permission Denied!");
        }
    }

    @GetMapping("/{id}")
    ApplicationAndAssessmentDTO getDetail(HttpServletRequest request, @PathVariable("id") String id) {
        //interviewer can get detail without signing in. delete @NeedLogin
        return assessmentService.getDetail(id);
    }

    @PutMapping("/{id}")
    void update(HttpServletRequest request, @RequestBody AssessmentDTO assessmentDTO) {
        //interviewer can get detail without signing in. delete @NeedLogin
        assessmentService.update(assessmentDTO);
        applicationService.update(assessmentDTO);
    }


    @PutMapping("/{id}/schedule")
    void schedule(HttpServletRequest request,@PathVariable("id") String id, @RequestBody AssessmentDTO assessmentDTO) {
        //Set interview time chosen by the candidate
        assessmentDTO.setId(id);
        assessmentService.schedule(assessmentDTO,request.getHeader("Referer"));
    }

    @PutMapping("/rearrange")
    @NeedLogin
    void resendEmail(@RequestBody EmailDTO emailDTO){
        //if interviewer forget selecting date, resend
        //if interviewer has selected date, should not resend?
        assessmentService.resendEmail(emailDTO);
    }

    @PutMapping("/reset")
    @NeedLogin
    void reassessment(@RequestBody EmailDTO emailDTO){
        assessmentService.reassessment(emailDTO);
    }


}
