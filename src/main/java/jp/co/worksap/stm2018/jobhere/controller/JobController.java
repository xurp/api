package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xu_xi-pc on 2018/8/29.
 */
@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;
    @Autowired
    public JobController(JobService jobService){
        this.jobService=jobService;
    }

    @PostMapping("")
    JobDTO save(@RequestBody JobDTO jobDto){
        return jobService.save(jobDto);
    }

    @PutMapping("/{id}")
    JobDTO update(@PathVariable("id") String id,@RequestBody JobDTO jobDto){
        return jobService.update(id,jobDto);
    }
}
