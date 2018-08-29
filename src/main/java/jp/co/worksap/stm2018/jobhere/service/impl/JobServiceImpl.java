package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.JobRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobDTO> list() {
        List<Job> jobList = jobRepository.findAll();
        List<JobDTO> jobDTOList = new ArrayList<>();
        for (Job job : jobList) {
            jobDTOList.add(JobDTO.builder()
                    .id(job.getId())
                    .name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime()).build());
        }
        return jobDTOList;
    }

    @Transactional
    @Override
    public JobDTO update(String id, JobDTO jobDTO) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setUpdateTime(timestamp);
            job.setCount(jobDTO.getCount());
            job.setDepartment(jobDTO.getDepartment());
            job.setDetail(jobDTO.getDetail());
            job.setName(jobDTO.getName());
            job.setRemark(jobDTO.getRemark());
            jobRepository.save(job);
            return JobDTO.builder()
                    .id(id)
                    .name(jobDTO.getName())
                    .detail(jobDTO.getDetail())
                    .count(jobDTO.getCount())
                    .department(jobDTO.getDepartment())
                    .remark(jobDTO.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(timestamp).build();
        } else {
            throw new ValidationException("Job id does not exist!");
        }
    }

    @Transactional
    @Override
    public JobDTO save(JobDTO jobDTO) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Job jobToSave = new Job();
        jobToSave.setId(uuid);
        jobToSave.setName(jobDTO.getName());
        jobToSave.setDetail(jobDTO.getDetail());
        jobToSave.setCount(jobDTO.getCount());
        jobToSave.setDepartment(jobDTO.getDepartment());
        jobToSave.setRemark(jobDTO.getRemark());
        jobToSave.setUpdateTime(timestamp);
        jobRepository.save(jobToSave);
        return JobDTO.builder().id(uuid).name(jobDTO.getName())
                .detail(jobDTO.getDetail())
                .count(jobDTO.getCount())
                .department(jobDTO.getDepartment())
                .remark(jobDTO.getRemark())
                .createTime(timestamp)
                .updateTime(timestamp).build();
    }

    @Override
    public JobDTO getDetail(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            return JobDTO.builder().id(job.getId()).name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime()).build();
        } else {
            throw new ValidationException("Job id does not exist!");
        }
    }
}

