package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
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
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    ApplicationServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository, ResumeRepository resumeRepository, UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }


    @Transactional
    @Override
    public ApplicationDTO save(String jobId, String resumeId, String userId) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Resume r = resumeRepository.findById(resumeId).get();
            Resume resume = new Resume();
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

            Application application = new Application();
            application.setId(uuid);
            application.setResume(resume);
            application.setStep("no");
            Timestamp t = new Timestamp(System.currentTimeMillis());
            application.setUpdateTime(t);
            User user = userRepository.findById(userId).get();
            application.setUser(user);
            Job job = jobOptional.get();
            application.setJob(job);
            job.addApplication(application);
            jobRepository.save(job);
            return ApplicationDTO.builder().id(application.getId())
                    .resume(application.getResume())
                    .job(application.getJob())
                    .step(application.getStep()).createTime(t)
                    .updateTime(t).build();
        } else {
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

    @Override
    public ApplicationDTO find(String applicationId) {
//        private String id;
//        private Resume resume;
//        private Job job;
//        private String step;
//        private User user;
//        private Timestamp createTime;
//        private Timestamp updateTime;

        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            return ApplicationDTO.builder()
                    .id(application.getId())
                    .resume(application.getResume())
                    .job(application.getJob())
                    .step(application.getStep())
                    .user(application.getUser())
                    .createTime(application.getCreateTime())
                    .updateTime(application.getUpdateTime()).build();
        } else {
            throw new NotFoundException("Application id does not exist!");
        }
    }

    @Override
    public List<ApplicationDTO> list(String jobId, String step) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        List<ApplicationDTO> applicationDTOList = new ArrayList<>();
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            for (Application application : job.getApplications()) {
                if (step.equals("ALL") || application.getStep().equals(step)) {
                    applicationDTOList.add(ApplicationDTO.builder()
                            .id(application.getId())
                            .resume(application.getResume())
                            .job(application.getJob())
                            .step(application.getStep())
                            .user(application.getUser())
                            .createTime(application.getCreateTime())
                            .updateTime(application.getUpdateTime()).build());
                }
            }
        } else {
            throw new NotFoundException("Job id does not exist!");
        }
        return applicationDTOList;
    }

    @Override
    public void update(String applicationId, String step) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStep(step);
            applicationRepository.save(application);
        } else {
            throw new NotFoundException("Application not found");
        }
    }


}

