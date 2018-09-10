package jp.co.worksap.stm2018.jobhere.controller;

import io.swagger.annotations.ApiOperation;
import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * Created by xu_xi-pc on 2018/9/3.
 */
@Slf4j
@RestController
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ResumeService resumeService;
    private final JobService jobService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, ResumeService resumeService, JobService jobService) {
        this.applicationService = applicationService;
        this.resumeService = resumeService;
        this.jobService = jobService;
    }
    @ApiOperation(value="save new application", notes="use Job to save Application(and consume)")
    @PostMapping("")
    @NeedLogin
    ApplicationDTO save(HttpServletRequest request, @RequestBody String jobId) {
        jobId = jobId.substring(0, jobId.length() - 1);
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("candidate")) {
            String resumeId = user.getResume().getId();
            //String uuid=UUID.randomUUID().toString().replace("-", "");
            //resume.setId(uuid);
            //resumeService.save(resume);
            return applicationService.save(jobId, resumeId, user.getId());

        } else {
            throw new ValidationException("You are not candidate!");
        }

    }
    @ApiOperation(value="list applications of a step", notes="if step is ALL or the step of application equals to request, return it")
    @GetMapping("")
    @NeedLogin
    List<ApplicationDTO> list(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            String jobId = request.getParameter("jobId");
            String step = request.getParameter("step");
            return applicationService.list(jobId, step);
        } else {
            log.warn("Permission Denied!");
            throw new ValidationException("Permission Denied!");
        }
    }
    @ApiOperation(value="get application detail", notes="get application detail")
    @GetMapping("/{id}")
    @NeedLogin
    ApplicationDTO find(HttpServletRequest request, @PathVariable("id") String id) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            return applicationService.find(id);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }
    @ApiOperation(value="update step of application and send offer", notes="update step of application and send offer")
    @PutMapping("/{id}/step")
    @NeedLogin
    void update(HttpServletRequest request, @PathVariable("id") String id) {
        //last step:offer, change step of application
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            applicationService.updateApplicationStep(id);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }
    @ApiOperation(value="decline the application and send email to the candidate ", notes="when step is resume or -; if step is resume filter, set --; else set -")
    @PutMapping("/decline")
    @NeedLogin
    void decline(HttpServletRequest request,@RequestBody EmailDTO emailDTO) {
        //if batch operation, call this function N times
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            applicationService.decline(emailDTO);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }


}
