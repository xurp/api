package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.AppointedTimeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AppointedTimeAndApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private AppointedTimeRepository appointedTimeRepository;

    @Override
    public List<AssessmentDTO> getAssessments(String candidateId) {
//        private String id;
//        private Cooperator cooperator;
//        private String applicationId;
//        private Timestamp assessmentTime;
//        private Timestamp interviewTime;
//        private String comment;
//        private String step;
//        private String pass;
//        private String operationId;

        List<AssessmentDTO> assessmentDTOList=new ArrayList<>();
        User user = userRepository.getOne(candidateId);
        List<Application> applicationList=user.getApplications();
        for (Application application : applicationList) {
            List<Assessment> assessmentList=assessmentRepository.findByApplicationId(application.getId());
            for(Assessment assessment:assessmentList) {
                assessmentDTOList.add(AssessmentDTO.builder()
                        .id(assessment.getId())
                        .cooperator(assessment.getCooperator())
                        .applicationId(assessment.getApplicationId())
                        .assessmentTime(assessment.getAssessmentTime())
                        .interviewTime(assessment.getInterviewTime())
                        .comment(assessment.getComment())
                        .step(assessment.getStep())
                        .pass(assessment.getPass()).build());
            }
        }

        return assessmentDTOList;
    }


    @Override
    public List<AppointedTimeAndApplicationDTO> getAppointments(String candidateId) {
//        private String id;
//        private String operationId;
//        private Timestamp startDate;
//        private Timestamp endDate;
//        private Timestamp startTime;
//        private String cooperatorId;
//        private ApplicationDTO applicationDTO;

        List<AppointedTimeAndApplicationDTO> appointedTimeAndApplicationDTOList =new ArrayList<>();
        User user = userRepository.getOne(candidateId);
        List<Application> applicationList=user.getApplications();
        for (Application application : applicationList) {
            List<AppointedTime> appointedTimeList= appointedTimeRepository.getByApplicationId(application.getId());
            for (AppointedTime appointedTime : appointedTimeList) {
                ApplicationDTO applicationDTO = ApplicationDTO.builder()
                        .id(application.getId())
                        .resume(application.getResume())
                        .job(application.getJob())
                        .step(application.getStep())
                        .user(application.getUser())
                        .createTime(application.getCreateTime())
                        .updateTime(application.getUpdateTime()).build();

                appointedTimeAndApplicationDTOList.add(AppointedTimeAndApplicationDTO.builder()
                        .id(appointedTime.getId())
                        .operationId(appointedTime.getOperationId())
                        .startDate(appointedTime.getStartDate())
                        .endDate(appointedTime.getEndDate())
                        .startDate(appointedTime.getStartDate())
                        .periods(appointedTime.getPeriods())
                        .cooperatorId(appointedTime.getCooperatorId())
                        .applicationDTO(applicationDTO).build());
            }
        }

        return appointedTimeAndApplicationDTOList;


    }
}
