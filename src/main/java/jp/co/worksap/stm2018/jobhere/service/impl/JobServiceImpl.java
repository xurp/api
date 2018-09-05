package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.dao.JobRepository;
import jp.co.worksap.stm2018.jobhere.dao.StepRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.JobStepDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final StepRepository stepRepository;

    @Autowired
    JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,StepRepository stepRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.stepRepository=stepRepository;
    }

    @Override
    public List<JobDTO> list(Company company) {
        Company c = companyRepository.findById(company.getId()).get();//lazy, may be need this
        List<Job> jobList = c.getJobs();
        //List<Job> jobList = jobRepository.findAll();
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
                    .updateTime(job.getUpdateTime()).company(job.getCompany()).build());
        }
        return jobDTOList;
    }

    @Override
    public List<JobDTO> listAll(String userid) {
        List<Job> jobList = jobRepository.findAll();
        List<JobDTO> jobDTOList = new ArrayList<>();
        for (Job job : jobList) {
            List<Application> applications=job.getApplications();
            boolean flag=false;
            for(Application a:applications){
                if(a.getUser().getId().equals(userid)){
                    flag=true;//has applied
                    break;
                }
            }
            if(flag)
            jobDTOList.add(JobDTO.builder()
                    .id(job.getId())
                    .name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime())
                    .company(job.getCompany()).applied(true).build());
            else
                jobDTOList.add(JobDTO.builder()
                        .id(job.getId())
                        .name(job.getName())
                        .detail(job.getDetail())
                        .count(job.getCount())
                        .department(job.getDepartment())
                        .remark(job.getRemark())
                        .createTime(job.getCreateTime())
                        .updateTime(job.getUpdateTime())
                        .company(job.getCompany()).applied(false).build());
        }
        return jobDTOList;
    }

    @Transactional
    @Override
    public JobDTO update(Company company, String id, JobDTO jobDTO) {
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
            Company c = companyRepository.findById(company.getId()).get();//lazy, so it is necessary to search from db. if set it to eagar, all should be eagar
            job.setCompany(c);
            c.addJob(job);
            return JobDTO.builder()
                    .id(id)
                    .name(jobDTO.getName())
                    .detail(jobDTO.getDetail())
                    .count(jobDTO.getCount())
                    .department(jobDTO.getDepartment())
                    .remark(jobDTO.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(timestamp).company(c).build();
        } else {
            throw new ValidationException("Job id does not exist!");
        }
    }

    @Transactional
    @Override
    public JobDTO save(Company company, JobDTO jobDTO) {
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
        //jobRepository.save(jobToSave);
        Company c = companyRepository.findById(company.getId()).get();//lazy, so it is necessary to search from db. if set it to eagar, all should be eagar
        jobToSave.setCompany(c);
        c.addJob(jobToSave);
        companyRepository.save(c);
        return JobDTO.builder().id(uuid).name(jobDTO.getName())
                .detail(jobDTO.getDetail())
                .count(jobDTO.getCount())
                .department(jobDTO.getDepartment())
                .remark(jobDTO.getRemark())
                .createTime(timestamp)
                .updateTime(timestamp).company(c).build();
    }

    @Override
    public JobStepDTO getDetail(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            List<Step> stepList=stepRepository.findByJobId(job.getId());
            if(stepList==null||stepList.size()==0)
                stepList=stepRepository.findByJobId("-1");
            List<Step> sortedList = stepList.stream().sorted((a, b) -> Double.compare(a.getIndex(),b.getIndex())).collect(Collectors.toList());
            return JobStepDTO.builder().id(job.getId()).name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime()).step(sortedList).build();
        } else {
            throw new ValidationException("Job id does not exist!");
        }
    }
}

