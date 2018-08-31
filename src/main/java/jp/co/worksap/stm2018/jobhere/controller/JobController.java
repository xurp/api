package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    JobDTO save(HttpServletRequest request,@RequestBody JobDTO jobDto) {
        Company company=((User)request.getAttribute("getuser")).getCompany();
        return jobService.save(company,jobDto);
    }

    @PutMapping("/{id}")
    @NeedLogin
    JobDTO update(HttpServletRequest request,@PathVariable("id") String id, @RequestBody JobDTO jobDto) {
        Company company=((User)request.getAttribute("getuser")).getCompany();
        return jobService.update(company,id, jobDto);
    }

    @GetMapping("/{id}")
    JobDTO detail(@PathVariable("id") String id) {
        return jobService.getDetail(id);
    }

    @GetMapping("")
    @NeedLogin
    List<JobDTO> list(HttpServletRequest request) {
        //this function is only for hr. not for candidate(no matter login or not)
        Company company=((User)request.getAttribute("getuser")).getCompany();
        return jobService.list(company);
    }
}
