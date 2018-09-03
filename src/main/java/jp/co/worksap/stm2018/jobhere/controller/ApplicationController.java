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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
        this.resumeService=resumeService;
        this.jobService=jobService;
    }

    @PostMapping("")
    @NeedLogin
    ApplicationDTO save(HttpServletRequest request, @RequestBody String jobId) {
        jobId=jobId.substring(0,jobId.length()-1);
        User user = (User) request.getAttribute("getuser");
        if(user.getRole().equals("candidate")){
            String resumeId=user.getResume().getId();
            //String uuid=UUID.randomUUID().toString().replace("-", "");
            //resume.setId(uuid);
            //resumeService.save(resume);
            return applicationService.save(jobId,resumeId);

        }
        else{
            throw new ValidationException("You are not candidate!");
        }

    }
}
