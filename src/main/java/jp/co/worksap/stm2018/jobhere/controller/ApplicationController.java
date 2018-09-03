package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * Created by xu_xi-pc on 2018/9/3.
 */
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

//    @GetMapping("")
//    @NeedLogin
//    List<ApplicationDTO> list(HttpServletRequest request, @RequestBody String jobId, @RequestBody String step) {
//        User user = (User) request.getAttribute("getuser");
//        if (user.getRole().equals("hr")) {
//            jobId = jobId.substring(0, jobId.length() - 1);
//            step = step.substring(0, jobId.length() - 1);
//
//            return applicationService.list(jobId, step);
//        } else {
//            throw new ValidationException("Permission Denied!");
//        }
//
//    }

    @GetMapping("")
    @NeedLogin
    List<ApplicationDTO> list(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            String jobId = request.getParameter("jobId");
            String step = request.getParameter("step");
            //jobId = jobId.substring(0, jobId.length() - 1);
            //step = step.substring(0, jobId.length() - 1);

            return applicationService.list(jobId, step);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

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

    @PutMapping("/{id}/step")
    @NeedLogin
    void update(HttpServletRequest request, @PathVariable("id") String id, @RequestBody String step) {
        step = step.substring(0, step.length() - 1);
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            applicationService.update(id, step);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }


}
