package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Assessment;
import jp.co.worksap.stm2018.jobhere.model.domain.Cooperator;
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


@Service
public class AssessmentServiceImpl implements AssessmentService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final CooperatorRepository cooperatorRepository;
    private final AssessmentRepository assessmentRepository;


    @Autowired
    AssessmentServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository, UserRepository userRepository, ApplicationRepository applicationRepository, CooperatorRepository cooperatorRepository, AssessmentRepository assessmentRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.cooperatorRepository = cooperatorRepository;
    }

    @Transactional
    @Override
    public AssessmentDTO save(String applicationId, String cooperatorId, String subject, String content) {
        System.out.println("save");
        Assessment assessment = new Assessment();
        assessment.setId(UUID.randomUUID().toString().replace("-", ""));
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
        assessment.setComment("Here is your comment:");
        assessment.setPass("assessing");
        assessmentRepository.save(assessment);
        //step of application '+1'
        application.setStep((Integer.parseInt(step) + 1) + "");
        applicationRepository.save(application);
        Mail.send("chorespore@163.com", cooperator.getEmail(), subject,content);

        return AssessmentDTO.builder()
                .id(assessment.getId())

                .cooperator(assessment.getCooperator())
                .applicationId(assessment.getApplicationId())
                .assessmentTime(assessment.getAssessmentTime())
                .comment(assessment.getComment())
                .step(assessment.getStep())
                .pass(assessment.getPass()).build();
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
                return ApplicationAndAssessmentDTO.builder().applicationId(applicationId)
                        .job(application.getJob())
                        .resume(application.getResume())
                        .step(application.getStep())
                        .assessments(assessmentList).build();
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


}

