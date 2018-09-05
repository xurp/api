package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApplicationAndAssessmentDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.AssessmentService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AssessmentServiceImpl implements AssessmentService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final StepRepository stepRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final CooperatorRepository cooperatorRepository;
    private final AssessmentRepository assessmentRepository;


    @Autowired
    AssessmentServiceImpl(JobRepository jobRepository, StepRepository stepRepository, UserRepository userRepository, ApplicationRepository applicationRepository, CooperatorRepository cooperatorRepository, AssessmentRepository assessmentRepository) {
        this.stepRepository = stepRepository;
        this.jobRepository = jobRepository;
        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.cooperatorRepository = cooperatorRepository;
    }

    @Transactional
    @Override
    public AssessmentDTO save(String applicationId, String cooperatorId, String subject, String content,String assessId) {
        String newstep=hrUpdate(applicationId);
        Assessment assessment = new Assessment();
        assessment.setId(assessId);
        assessment.setApplicationId(applicationId);
        //List<Cooperator> cooperators =cooperatorRepository.findAllById(Arrays.asList(cooperatorArr));
        //assessment.setCooperators(cooperators);
        Cooperator cooperator=cooperatorRepository.findById(cooperatorId).get();
        assessment.setCooperator(cooperator);//originally, assessment is onetoone cooperator, but does not need to save cooperator. here , cooperator will be overwrite but it's ok.
        Application application = applicationRepository.findById(applicationId).get();
        String step = application.getStep();
        if (step.charAt(0) == '+' || step.charAt(0) == '-')
            step = step.substring(1);
        assessment.setStep(step);
        assessment.setStep(newstep);
        assessment.setComment(" ");
        assessment.setPass("assessing");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        assessment.setAssessmentTime(timestamp);
        assessmentRepository.save(assessment);
        //step of application '+1'
        //application.setStep((Integer.parseInt(step) + 1) + "");
        application.setStep(newstep);
        applicationRepository.save(application);
        Mail.send("chorespore@163.com", cooperator.getEmail(), subject,content);

        return AssessmentDTO.builder()
                .id(assessment.getId())
                .cooperator(assessment.getCooperator())
                .applicationId(assessment.getApplicationId())
                .assessmentTime(assessment.getAssessmentTime())
                .comment(assessment.getComment())
                .step(assessment.getStep())
                .pass(assessment.getPass())
        .assessmentTime(timestamp).build();
    }

    public List<AssessmentDTO> list(String applicationId) {
        List<AssessmentDTO> assessmentDTOList = new ArrayList<>();
        List<Assessment> assessmentList = assessmentRepository.findByApplicationId(applicationId);
        for (Assessment assessment : assessmentList) {
            assessmentDTOList.add(AssessmentDTO.builder()
                    .id(assessment.getId())
                    .cooperator(assessment.getCooperator())
                    .applicationId(assessment.getApplicationId())
                    .assessmentTime(assessment.getAssessmentTime())
                    .comment(assessment.getComment())
                    .step(assessment.getStep())
                    .pass(assessment.getPass()).build());
        }
        return assessmentDTOList;
    }

    @Override
    public ApplicationAndAssessmentDTO getDetail(String id) {
        //id:assessmentId
        Optional<Assessment> assessmentOptional = assessmentRepository.findById(id);
        if (assessmentOptional.isPresent()) {
            Assessment assessment = assessmentOptional.get();
            if (assessment.getAssessmentTime() != null) {
                String applicationId = assessment.getApplicationId();
                Application application = applicationRepository.findById(applicationId).get();
                List<Assessment> assessmentList = assessmentRepository.findByApplicationId(applicationId);
                List<Assessment> sortedList = assessmentList.stream().sorted((a, b) -> Double.compare(Double.parseDouble(a.getStep()),Double.parseDouble(b.getStep()))).collect(Collectors.toList());
                sortedList.remove(sortedList.size()-1);

                List<Step> stepList = stepRepository.findByJobId(application.getJob().getId());
                if (stepList == null || stepList.size() == 0)
                    stepList = stepRepository.findByJobId("-1");
                stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));

                return ApplicationAndAssessmentDTO.builder().applicationId(applicationId)
                        .job(application.getJob())
                        .resume(application.getResume())
                        .step(application.getStep())
                        .assessments(sortedList)
                                .stepList(stepList).build();
            } else {
                return null;
                //throw new ValidationException("You've done it, thank you.");
            }
        } else {
            throw new ValidationException("The link is wrong, please contact HR.");
        }

    }

    @Override
    public void update(AssessmentDTO assessmentDTO) {
        Optional<Assessment> assessmentOptional = assessmentRepository.findById(assessmentDTO.getId());
        if (assessmentOptional.isPresent()) {
            Assessment assessment = assessmentOptional.get();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            assessment.setAssessmentTime(timestamp);
            assessment.setPass(assessmentDTO.getPass());
            assessment.setComment(assessmentDTO.getComment());
            assessmentRepository.save(assessment);
        } else {
            throw new NotFoundException("Application not found");
        }
    }

    /*public String hrUpdate(String applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            if (application.getStep().charAt(0) == '+') {
                Job job = application.getJob();
                List<Step> stepList = stepRepository.findByJobId(job.getId());
                if(stepList==null||stepList.size()==0)
                    stepList=stepRepository.findByJobId("-1");
                List<Step> sortedList = stepList.stream().sorted((a, b) -> Double.compare(a.getIndex(), b.getIndex())).collect(Collectors.toList());
                Optional<Step> stepOptional = sortedList.stream().filter(tr -> tr.getIndex() > Double.valueOf(application.getStep().replace("+", ""))).findFirst();
                if (stepOptional.isPresent()) {
                    return stepOptional.get().getIndex() + "";
                    //application.setStep(stepOptional.get().getIndex() + "");
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
    }*/
    public String hrUpdate(String applicationId) {
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
                    //application.setStep(stepOptional.get().getIndex() + "");
                    return stepOptional.get().getIndex() + "";
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

