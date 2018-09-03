package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.dao.JobRepository;
import jp.co.worksap.stm2018.jobhere.dao.ResumeRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
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
public class ApplicationServiceImpl implements ApplicationService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;

    @Autowired
    ApplicationServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,ResumeRepository resumeRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository=resumeRepository;
    }



    @Transactional
    @Override
    public ApplicationDTO save(String jobId,String resumeId) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        Optional<Job> jobOptional=jobRepository.findById(jobId);
        if(jobOptional.isPresent()){
            Resume r=resumeRepository.findById(resumeId).get();
            Resume resume=new Resume();
            resume.setId(uuid1);
            resume.setAge(r.getAge());
            resume.setDegree(r.getDegree());
            resume.setEmail(r.getEmail());
            resume.setGender(r.getGender());
            resume.setIntro(r.getIntro());
            resume.setMajor(r.getMajor());
            resume.setName(r.getName());
            resume.setOpen(r.isOpen());
            resume.setPhone(r.getPhone());
            resume.setSchool(r.getSchool());

            Application application=new Application();
            application.setId(uuid);
            application.setResume(resume);
            application.setStep("no");
            Job job=jobOptional.get();
            application.setJob(job);
            job.addApplication(application);
            jobRepository.save(job);
            return ApplicationDTO.builder().id(application.getId())
                    .resume(application.getResume())
                    .job(application.getJob())
                    .step(application.getStep()).build();
        }
        else{
            throw new ValidationException("Job id does not exist!");
        }

        /*return JobDTO.builder().id(uuid).name(jobDTO.getName())
                .detail(jobDTO.getDetail())
                .count(jobDTO.getCount())
                .department(jobDTO.getDepartment())
                .remark(jobDTO.getRemark())
                .createTime(timestamp)
                .updateTime(timestamp).company(c).build();*/

    }


}

