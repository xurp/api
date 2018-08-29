package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.JobRepository;
import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class JobServiceImpl implements JobService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    public void update(JobDTO jobDTO) {
        Job job = jobRepository.findByName(jobDTO.getName());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        private String id;
//        private String name;
//        private String detail;
//        private int count;
//        private String department;
//        private String remark;
//        private Timestamp createTime;
//        private Timestamp updateTime;
        Job jobToSave = Job.builder()
                .id(job.getId())
                .name(jobDTO.getName())
                .detail(jobDTO.getDetail())
                .count(jobDTO.getCount())
                .department(jobDTO.getDepartment())
                .remark(jobDTO.getRemark())
                .createTime(job.getCreateTime())
                .updateTime(timestamp).build();
        jobRepository.save(jobToSave);
    }
}

