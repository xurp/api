package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
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
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final StepRepository stepRepository;

    @Autowired
    ApplicationServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,
                           ResumeRepository resumeRepository, UserRepository userRepository,
                           ApplicationRepository applicationRepository, StepRepository stepRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.stepRepository = stepRepository;
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
            //application.setStep("no");
            Timestamp t = new Timestamp(System.currentTimeMillis());
            application.setUpdateTime(t);
            User user = userRepository.findById(userId).get();
            application.setUser(user);
            Job job = jobOptional.get();
            List<Step> stepList = stepRepository.findByJobId(job.getId());
            if (stepList == null || stepList.size() == 0)
                stepList = stepRepository.findByJobId("-1");
            List<Step> sortedList = stepList.stream().sorted((a, b) -> Double.compare(a.getIndex(), b.getIndex())).collect(Collectors.toList());
            application.setStep(String.valueOf(sortedList.get(0).getIndex()));//there may be error of '.'
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
                if (step.equals("ALL") || Math.abs(Double.parseDouble(application.getStep())-Double.parseDouble(step))<0.01) {
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
    public void update(AssessmentDTO assessmentDTO) {
        Optional<Application> applicationOptional = applicationRepository.findById(assessmentDTO.getApplicationId());
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            String pass = assessmentDTO.getPass();
            if (pass.equals("pass")) {
                application.setStep("+" + assessmentDTO.getStep());
            } else if (pass.equals("fail")) {
                application.setStep("-" + assessmentDTO.getStep());
            }
            applicationRepository.save(application);
        } else {
            throw new NotFoundException("Application not found");
        }
    }

    @Override
    public void hrUpdate(String applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            Job job = application.getJob();
            List<Step> stepList = stepRepository.findByJobId(job.getId());
            if (stepList == null || stepList.size() == 0)
                stepList = stepRepository.findByJobId("-1");

            stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));

            if (Double.valueOf(application.getStep()) - stepList.get(0).getIndex() == 0 || application.getStep().charAt(0) == '+') {
                Optional<Step> stepOptional = stepList.stream().filter(tr -> tr.getIndex() > Double.valueOf(application.getStep().replace("+", ""))).findFirst();
                if (stepOptional.isPresent()) {
                    application.setStep(stepOptional.get().getIndex() + "");
                }
            } else {
                throw new ValidationException("The application has been turn down");
            }
        } else {
            throw new NotFoundException("Application no found");
        }
    }

    @Transactional
    @Override
    public void updateApplicationStep(String applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            Job job = application.getJob();
            List<Step> stepList = stepRepository.findByJobId(job.getId());
            if (stepList == null || stepList.size() == 0)
                stepList = stepRepository.findByJobId("-1");

            stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));

            if (Math.abs(Double.valueOf(application.getStep()) - stepList.get(0).getIndex())<0.01 || application.getStep().charAt(0) == '+') {
                Optional<Step> stepOptional = stepList.stream().filter(tr -> tr.getIndex() > Double.valueOf(application.getStep().replace("+", ""))).findFirst();
                if (stepOptional.isPresent()) {
                    application.setStep(stepOptional.get().getIndex() + "");
                    applicationRepository.save(application);
                    //return stepOptional.get().getIndex() + "";
                }
                else {
                    throw new ValidationException("Step in system has errors.");
                }
            } else {
                throw new ValidationException("The application has been turn down");
            }
        } else {
            throw new NotFoundException("Application no found");
        }
    }

}

