package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
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

    private final JobRepository jobRepository;
    private final OfferRepository offerRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final StepRepository stepRepository;
    private final AssessmentRepository assessmentRepository;
    private final Mail mail;

    @Autowired
    ApplicationServiceImpl(AssessmentRepository assessmentRepository, Mail mail, JobRepository jobRepository, OfferRepository offerRepository,
                           ResumeRepository resumeRepository, UserRepository userRepository,

                           ApplicationRepository applicationRepository, StepRepository stepRepository) {
        this.assessmentRepository = assessmentRepository;
        this.mail = mail;
        this.offerRepository = offerRepository;
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
            resume.setExperience(r.getExperience());
            resume.setSalary(r.getSalary());
            resume.setGraduation(r.getGraduation());

            Application application = new Application();
            application.setId(uuid);
            application.setResume(resume);
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
    }

    @Override
    public ApplicationDTO link(String jobId, String resumeId) {
        User user = userRepository.getByResume(resumeRepository.getOne(resumeId));

        return save(jobId, resumeId, user.getId());
    }

    @Override
    public ApplicationDTO find(String applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            List<Assessment> assessmentList = assessmentRepository.findByApplicationId(applicationId);
            assessmentList.sort((a, b) -> Double.compare(Double.parseDouble(a.getStep()), Double.parseDouble(b.getStep())));
            String step = application.getStep();
            if (assessmentList.size() > 0) {
                Assessment assessment = assessmentList.get(assessmentList.size() - 1);
                if (assessment.getCooperator() != null && assessment.getPass().equals("assessing"))
                    step = "++" + step;
            }
            return ApplicationDTO.builder()
                    .id(application.getId())
                    .resume(application.getResume())
                    .job(application.getJob())
                    .step(step)
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
                //if (step.equals("ALL") || Math.abs(Double.parseDouble(application.getStep().replaceAll("-",""))-Double.parseDouble(step.replaceAll("-","")))<0.01) {
                if (step.equals("ALL") || Math.abs(Double.parseDouble(application.getStep().replace("+", "").replace("-", "")) - Double.parseDouble(step.replace("+", "").replace("-", ""))) < 0.01) {
                    List<Assessment> assessmentList = assessmentRepository.findByApplicationId(application.getId());
                    String applicationStep = application.getStep();
                    if (assessmentList.size() > 0) {//else, step =0 resume filter
                        assessmentList.sort((a, b) -> Double.compare(Double.parseDouble(a.getStep()), Double.parseDouble(b.getStep())));
                        Assessment assessment = assessmentList.get(assessmentList.size() - 1);
                        if (assessment.getCooperator() != null && assessment.getPass().equals("assessing"))
                            applicationStep = "++" + applicationStep;
                    }
                    applicationDTOList.add(ApplicationDTO.builder()
                            .id(application.getId())
                            .resume(application.getResume())
                            .job(application.getJob())
                            .step(applicationStep)
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
        //when interviewer return pass or fail
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
            //May should use the following if, but no one will use /{id}/step on purpose
            //if (Math.abs(Double.valueOf(application.getStep().replace("+", "").replace("-", "")) - stepList.get(stepList.size()-2).getIndex())<0.01 && application.getStep().charAt(0) == '+') {
            if (application.getStep().charAt(0) == '+') {
                Optional<Step> stepOptional = stepList.stream().filter(tr -> tr.getIndex() > Double.valueOf(application.getStep().replace("+", ""))).findFirst();
                if (stepOptional.isPresent()) {
                    application.setStep(stepOptional.get().getIndex() + "");
                    applicationRepository.save(application);
                    Offer offer = new Offer();
                    offer.setId(UUID.randomUUID().toString().replace("-", ""));
                    offer.setApplicationId(applicationId);
                    offer.setCompanyId(application.getJob().getCompany().getId());
                    offer.setOfferTime(new Timestamp(System.currentTimeMillis()));
                    offer.setSendStatus("0");
                    offerRepository.save(offer);
                } else {
                    throw new ValidationException("Step in system has errors.");
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
    public void decline(EmailDTO emailDTO) {
        //This method is called N times
        Optional<Application> applicationOptional = applicationRepository.findById(emailDTO.getApplicationId());
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            String step = application.getStep();

            List<Step> stepList = stepRepository.findByJobId(application.getJob().getId());
            if (stepList == null || stepList.size() == 0)
                stepList = stepRepository.findByJobId("-1");
            stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));
            if (step.charAt(0) == '-' && step.charAt(1) == '-') {
                throw new ValidationException("The rejection email has been sent.");
            } else if (step.charAt(0) == '-') {
                application.setStep("-" + step);
                applicationRepository.save(application);
                String content = emailDTO.getContent();
                String companyName = application.getJob().getCompany().getCompanyName();
                content = content.replaceAll("\\[position_name\\]", application.getJob().getName());
                content = content.replaceAll("\\[company_name\\]", companyName);
                content = content.replaceAll("\\[candidate_name\\]", application.getResume().getName());
                mail.send("chorespore@163.com", emailDTO.getReceiver(), application.getResume().getName()+ " -- "+emailDTO.getSubject(), content);
            }
            //maybe the following two replace is unnecessary
            else if (Math.abs(Double.valueOf(step.replace("+", "").replace("-", "")) - stepList.get(0).getIndex()) < 0.01) {
                application.setStep("--" + step);
                applicationRepository.save(application);
                String content = emailDTO.getContent();
                String companyName = application.getJob().getCompany().getCompanyName();
                content = content.replaceAll("\\[position_name\\]", application.getJob().getName());
                content = content.replaceAll("\\[company_name\\]", companyName);
                content = content.replaceAll("\\[candidate_name\\]", application.getResume().getName());
                mail.send("chorespore@163.com", emailDTO.getReceiver(), application.getResume().getName()+ " -- "+emailDTO.getSubject(), content);
            } else {
                throw new ValidationException("The interviewer has not rejected the candidate.");
            }

        } else {
            throw new NotFoundException("Application no found");
        }

    }

    @Override
    public List<ApplicationDTO> candidateApplicationList(User user) {
        String userId = user.getId();
        List<Application> list = userRepository.findById(userId).get().getApplications();
        List<ApplicationDTO> applicationDTOList = new ArrayList<>();
        for (Application application : list) {
            List<Step> stepList = stepRepository.findByJobId(application.getJob().getId());
            if (stepList == null || stepList.size() == 0)
                stepList = stepRepository.findByJobId("-1");
            stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));
            applicationDTOList.add(ApplicationDTO.builder()
                    .id(application.getId())
                    .resume(application.getResume())
                    .job(application.getJob())
                    .step(application.getStep())
                    .user(application.getUser())
                    .createTime(application.getCreateTime())
                    .updateTime(application.getUpdateTime()).
                            stepList(stepList).build());
        }
        return applicationDTOList;
    }

}

