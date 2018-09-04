package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    AssessmentServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository, UserRepository userRepository, ApplicationRepository applicationRepository,CooperatorRepository cooperatorRepository,AssessmentRepository assessmentRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.assessmentRepository=assessmentRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.cooperatorRepository=cooperatorRepository;
    }
    @Transactional
    @Override
    public AssessmentDTO save(String applicationId, String cooperatorId) {
        Assessment assessment=new Assessment();
        assessment.setId(UUID.randomUUID().toString().replace("-", ""));
        assessment.setApplicationId(applicationId);
        assessment.setCooperator(cooperatorRepository.findById(cooperatorId).get());//assessment is onetoone cooperator, but does not need to save cooperator. here , cooperator will be overwrite but it's ok.
        Application application=applicationRepository.findById(applicationId).get();
        assessment.setStep(application.getStep());
        assessment.setComment("Here is your comment:");
        assessment.setPass("assessing");
        assessmentRepository.save(assessment);
        Mail.send("chorespore@163.com","xu_xi@worksap.co.jp","Please give your comment","candidate name:"+application.getUser().getUsername());
        return AssessmentDTO.builder().id(assessment.getId())
                .applicationId(assessment.getApplicationId())
                .cooperator(assessment.getCooperator())
                .step(assessment.getStep()).pass(assessment.getPass()).build();
    }




}

