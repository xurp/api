package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Item;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ItemDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.JobStepDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/8/29.
 */
@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("")
    @NeedLogin
    JobDTO save(HttpServletRequest request, @RequestBody JobDTO jobDto) {
        Company company = ((User) request.getAttribute("getuser")).getCompany();
        return jobService.save(company, jobDto);
    }

    @PutMapping("/{id}")
    @NeedLogin
    JobDTO update(HttpServletRequest request, @PathVariable("id") String id, @RequestBody JobDTO jobDto) {
        Company company = ((User) request.getAttribute("getuser")).getCompany();
        return jobService.update(company, id, jobDto);
    }

    @GetMapping("/{id}")
    JobStepDTO detail(@PathVariable("id") String id) {
        return jobService.getDetail(id);
    }

    @GetMapping("")
    @NeedLogin
    List<JobDTO> list(HttpServletRequest request) {
        //If the user is "hr" return jobList of the hr's company.
        //For "admin" return jobList of all companies.
        List<JobDTO> jobDTOList = new ArrayList<>();
        User curUser = ((User) request.getAttribute("getuser"));
        String role = curUser.getRole();
        if (role.equals("hr")) {
            jobDTOList = jobService.list(curUser.getCompany());
        } else if (role.equals("candidate")) {
            jobDTOList = jobService.listAll(((User) request.getAttribute("getuser")).getId());
        }
        return jobDTOList;
    }
    @GetMapping("/steps")
    List<Step> stepList(HttpServletRequest request) {
        String jobid=request.getParameter("jobId");
        return jobService.getStepList(jobid);
    }

    @PutMapping("/steps")
    @NeedLogin
    void updateJobStep(HttpServletRequest request,@RequestBody JobStepDTO jobStepDTO) {
        jobService.updateJobStep(jobStepDTO);
    }

    @GetMapping("/items")
    List<Item> itemList(HttpServletRequest request) {
        String stepId=request.getParameter("stepId");
        return jobService.getItemList(stepId);
    }

    @PutMapping("/items")
    @NeedLogin
    void updateStepItem(HttpServletRequest request,@RequestBody ItemDTO itmDTO) {
        jobService.updateStepItem(itmDTO);
    }

}
