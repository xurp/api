package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
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

    private final OutboxRepository outboxRepository;
    private final StepRepository stepRepository;
    private final AppointedTimeRepository appointedTimeRepository;
    private final ApplicationRepository applicationRepository;
    private final CooperatorRepository cooperatorRepository;
    private final AssessmentRepository assessmentRepository;
    private final CompanyRepository companyRepository;


    @Autowired
    AssessmentServiceImpl(CompanyRepository companyRepository, OutboxRepository outboxRepository, StepRepository stepRepository, AppointedTimeRepository appointedTimeRepository, ApplicationRepository applicationRepository, CooperatorRepository cooperatorRepository, AssessmentRepository assessmentRepository) {
        this.companyRepository = companyRepository;
        this.stepRepository = stepRepository;
        this.outboxRepository = outboxRepository;
        this.assessmentRepository = assessmentRepository;
        this.appointedTimeRepository = appointedTimeRepository;
        this.applicationRepository = applicationRepository;
        this.cooperatorRepository = cooperatorRepository;
    }

    @Transactional
    @Override
    public void saveOutboxAndMakeAppointment(EmailDTO emailDto) {
        //now this method only be called ONCE!!
        //NOTICE!!!!!!!application num should >= cooperator num!
        //to do:let interview choose date and time
        int cooperatorNum = emailDto.getCooperatorIds().size();
        if (cooperatorNum == 0)
            return;
        int applicationNum = emailDto.getApplications().size();
        if (applicationNum == 0)
            return;
        int batchindex = 0;
        for (String applicationId : emailDto.getApplications()) {
            //the following does not work(may be transabctuibak). use batchindex
            /*List<AppointedTime> appointedTimeList=appointedTimeRepository.getByOperationId(emailDto.getOperationId());
            int index=0;
            if(appointedTimeList!=null&&appointedTimeList.size()>0)
                index=appointedTimeList.size();*/
            //Outbox:send to candidates to select dates
            Outbox outbox = new Outbox();
            outbox.setId(UUID.randomUUID().toString().replace("-", ""));
            outbox.setOperationId(emailDto.getOperationId());
            outbox.setApplicationId(applicationId);
            outbox.setContent("");
            outbox.setLink("");
            outbox.setSubject("");
            outboxRepository.save(outbox);
            //for(String cooperatorId:emailDto.getCooperatorIds()) {
            int onecooperatordates = 1;
            if (applicationNum == 1)
                onecooperatordates = 3;
            for (int i = 0; i < onecooperatordates; i++) {
                AppointedTime appointedTime = new AppointedTime();
                appointedTime.setId(UUID.randomUUID().toString().replace("-", ""));
                appointedTime.setApplicationId(applicationId);
                //all the cooperator are saved, may occurs more than 1 but ok
                appointedTime.setCooperatorId(emailDto.getCooperatorIds().get(batchindex % cooperatorNum));
                String startdate = emailDto.getStartDate();
                String enddate = emailDto.getEndDate();
                try {
                    Timestamp t1 = Timestamp.valueOf(startdate);
                    Timestamp t2 = Timestamp.valueOf(enddate);
                    appointedTime.setStartDate(t1);
                    appointedTime.setEndDate(t2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                appointedTime.setOperationId(emailDto.getOperationId());
                appointedTimeRepository.save(appointedTime);
            }
            // }

            //subject and content are in the dto
            if (batchindex < cooperatorNum) {//only need send cooperatorNum emails
                String content = emailDto.getContent();
                content = content.replaceAll("\\[assessor_name\\]", cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getName());
                content = content.replaceAll("\\[company_name\\]", companyRepository.findById(cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getCompanyId()).get().getCompanyName());
                content = content.replaceAll("\\[operation_id\\]", emailDto.getOperationId());
                content = content.replaceAll("\\[cooperation_id\\]", emailDto.getCooperatorIds().get(batchindex % cooperatorNum));
                Mail.send("chorespore@163.com", cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getEmail(), emailDto.getSubject(), content);
            }
            //now, creating assessment and updating step of applications will be done immediately
            String newstep = hrUpdate(applicationId);
            Assessment assessment = new Assessment();
            assessment.setId(UUID.randomUUID().toString().replace("-", ""));
            //assessment.setId(emailDto.getAssessId());
            assessment.setApplicationId(applicationId);
            //originally, assessment is onetoone cooperator, but does not need to save cooperator. here , cooperator will be overwrite but it's ok.
            //now, cooperator should be updated after choosing date(but shezhao has only one cooperator so save it)
            if (applicationNum == 1) {
                Cooperator cooperator = cooperatorRepository.findById(emailDto.getCooperatorIds().get(0)).get();
                assessment.setCooperator(cooperator);
            }
            Application application = applicationRepository.findById(applicationId).get();
            assessment.setStep(newstep);
            assessment.setComment(" ");
            assessment.setPass("assessing");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            assessment.setAssessmentTime(timestamp);
            assessmentRepository.save(assessment);
            application.setStep(newstep);
            applicationRepository.save(application);
            batchindex++;
        }
    }

    @Override
    public void schedule(AssessmentDTO assessmentDTO) {
        Assessment assessment = assessmentRepository.getOne(assessmentDTO.getId());
        AppointedTime appointedTime = appointedTimeRepository.getFirstByStartTime(assessmentDTO.getInterviewTime());
        Optional<Cooperator> cooperatorOptional = cooperatorRepository.findById(appointedTime.getCooperatorId());
        if (cooperatorOptional.isPresent())
            assessment.setCooperator(cooperatorOptional.get());
        assessment.setInterviewTime(assessmentDTO.getInterviewTime());

        assessmentRepository.save(assessment);
        appointedTimeRepository.delete(appointedTime);
    }

    public void resendEmail(EmailDTO emailDTO) {
        String applicationId = emailDTO.getApplicationId();
        //1.interviewer has not selected date; 2.selected, but candidate not selected; 3.candidate selected so appointedtime's record is deleted and assessment has cooperator and interview time
        //now only consider case 3
        List<AppointedTime> appointedTimeList = appointedTimeRepository.getByApplicationId(applicationId);
        if (appointedTimeList == null || appointedTimeList.size() == 0) {
            //List<Assessment> assessmentList = assessmentRepository.findByApplicationId(applicationId);
            //List<Assessment> sortedList = assessmentList.stream().sorted((a, b) -> Double.compare(Double.parseDouble(a.getStep()),Double.parseDouble(b.getStep()))).collect(Collectors.toList());
            //Assessment assessment=sortedList.get(sortedList.size()-1);
            Optional<Assessment> assessmentOptional = assessmentRepository.findById(emailDTO.getAssessId());
            if (!assessmentOptional.isPresent())
                throw new ValidationException("The link is wrong, please contact HR.");
            Assessment assessment = assessmentOptional.get();
            if (!assessment.getPass().equals("assessing")) {
                throw new ValidationException("Interviewer has assessed the candidate.");
            }
            if (assessment.getCooperator() != null) {//case 3
                Outbox outbox = new Outbox();
                outbox.setId(UUID.randomUUID().toString().replace("-", ""));
                outbox.setOperationId(emailDTO.getOperationId());
                outbox.setApplicationId(applicationId);
                outbox.setContent("");
                outbox.setLink("");
                outbox.setSubject("");
                outboxRepository.save(outbox);
                for (int i = 0; i < 3; i++) {
                    AppointedTime appointedTime = new AppointedTime();
                    appointedTime.setId(UUID.randomUUID().toString().replace("-", ""));
                    appointedTime.setApplicationId(applicationId);
                    appointedTime.setCooperatorId(emailDTO.getCooperatorId());
                    String startdate = emailDTO.getStartDate();
                    String enddate = emailDTO.getEndDate();
                    try {
                        Timestamp t1 = Timestamp.valueOf(startdate);
                        Timestamp t2 = Timestamp.valueOf(enddate);
                        appointedTime.setStartDate(t1);
                        appointedTime.setEndDate(t2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    appointedTime.setOperationId(emailDTO.getOperationId());
                    appointedTimeRepository.save(appointedTime);
                }
                // }

                String content = emailDTO.getContent();
                content = content.replaceAll("\\[assessor_name\\]", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName());
                content = content.replaceAll("\\[company_name\\]", companyRepository.findById(cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getCompanyId()).get().getCompanyName());
                content = content.replaceAll("\\[operation_id\\]", emailDTO.getOperationId());
                content = content.replaceAll("\\[cooperation_id\\]", emailDTO.getCooperatorId());
                Mail.send("chorespore@163.com", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getEmail(), emailDTO.getSubject(), content);

                assessmentRepository.deleteById(assessment.getId());
                String newstep = hrUpdate(applicationId);
                Assessment assessment1 = new Assessment();
                assessment1.setId(UUID.randomUUID().toString().replace("-", ""));
                assessment1.setApplicationId(applicationId);
                Cooperator cooperator = cooperatorRepository.findById(emailDTO.getCooperatorId()).get();
                assessment1.setCooperator(cooperator);

                Application application = applicationRepository.findById(applicationId).get();
                assessment1.setStep(application.getStep());
                assessment1.setComment(" ");
                assessment1.setPass("assessing");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                assessment1.setAssessmentTime(timestamp);
                assessmentRepository.save(assessment1);


            } else {//case 1

            }
        } else {//case 2

        }
    }

    @Override
    public void reassessment(EmailDTO emailDTO) {
        //id:assessmentId
        Assessment assessment = assessmentRepository.getOne(emailDTO.getAssessId());
        if (assessment != null) {
            assessment.setPass("assessing");
            assessment.setComment("");
            assessmentRepository.save(assessment);

            String content = emailDTO.getContent().replaceAll("\\[company_name\\]", companyRepository.findById(cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getCompanyId()).get().getCompanyName());

            Mail.send("chorespore@163.com", emailDTO.getReceiver(), emailDTO.getSubject(), content);

        } else {
            throw new ValidationException("The link is wrong, please contact HR.");
        }

    }

    @Transactional
    @Override
    public AssessmentDTO save(String applicationId, String cooperatorId, String subject, String content, String assessId) {
        //now. assessment and application will be updated immediately. This method may be used for other things.
        String newstep = hrUpdate(applicationId);
        Assessment assessment = new Assessment();
        assessment.setId(assessId);
        assessment.setApplicationId(applicationId);
        Cooperator cooperator = cooperatorRepository.findById(cooperatorId).get();
        assessment.setCooperator(cooperator);//originally, assessment is onetoone cooperator, but does not need to save cooperator. here , cooperator will be overwrite but it's ok.
        Application application = applicationRepository.findById(applicationId).get();
        assessment.setStep(newstep);
        assessment.setComment(" ");
        assessment.setPass("assessing");
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //assessment.setAssessmentTime(timestamp);
        assessmentRepository.save(assessment);
        application.setStep(newstep);
        applicationRepository.save(application);
        Mail.send("chorespore@163.com", cooperator.getEmail(), subject, content);
        return AssessmentDTO.builder()
                .id(assessment.getId())
                .cooperator(assessment.getCooperator())
                .applicationId(assessment.getApplicationId())
                .assessmentTime(assessment.getAssessmentTime())
                .comment(assessment.getComment())
                .step(assessment.getStep())
                .pass(assessment.getPass())
                //.assessmentTime(timestamp)
                .build();
    }

    /*@Transactional
    @Override
    public AssessmentDTO save(String applicationId, String cooperatorId, String subject, String content,String assessId) {
        //this function is before 'choose date' module
        String newstep=hrUpdate(applicationId);
        Assessment assessment = new Assessment();
        assessment.setId(assessId);
        assessment.setApplicationId(applicationId);
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
    }*/

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
        //originally use findbyid, but assessment to cooperator is one-to-one and cooperator may be null and it is EAGER and findbyid use inner join
        //so assessment exists, cooperator is null, the result is null. so set cooperator in assessment LAZY
        Assessment assessment = assessmentRepository.getOne(id);
        if (assessment != null) {
            if (assessment.getPass().equals("assessing")) {
                String applicationId = assessment.getApplicationId();
                Application application = applicationRepository.findById(applicationId).get();
                List<Assessment> assessmentList = assessmentRepository.findByApplicationId(applicationId);
                List<Assessment> sortedList = assessmentList.stream().sorted((a, b) -> Double.compare(Double.parseDouble(a.getStep()), Double.parseDouble(b.getStep()))).collect(Collectors.toList());
                if (sortedList.size() > 0)
                    sortedList.remove(sortedList.size() - 1);
                List<Step> stepList = stepRepository.findByJobId(application.getJob().getId());
                if (stepList == null || stepList.size() == 0)
                    stepList = stepRepository.findByJobId("-1");
                stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));


                //sortedList's assessment has cooperator, but when converted to JSON, cooperator is...
                List<AssessmentDTO> assessmentDTOList = new ArrayList<>();
                for (Assessment a : sortedList) {
                    assessmentDTOList.add(AssessmentDTO.builder()
                            .id(a.getId())
                            .cooperator(a.getCooperator())
                            .applicationId(a.getApplicationId())
                            .assessmentTime(a.getAssessmentTime())
                            .interviewTime(assessment.getInterviewTime())
                            .comment(a.getComment())
                            .step(a.getStep())
                            .pass(a.getPass()).build());
                }
                /*List<Assessment> assessmentListIncludingCooperator=new ArrayList<>();
                Assessment assessment1=new Assessment();
                if(sortedList.size()>0)
                assessment1.setCooperator(sortedList.get(0).getCooperator());
                assessmentListIncludingCooperator.add(assessment1);*/
                return ApplicationAndAssessmentDTO.builder().applicationId(applicationId)
                        .job(application.getJob())
                        .resume(application.getResume())
                        .step(application.getStep())
                        .assessments(assessmentDTOList)
                        .stepList(stepList)
                        .assessmentTime(assessment.getAssessmentTime())
                        .interviewTime(assessment.getInterviewTime()).build();
            } else {
                throw new ValidationException("You've done it, thank you.");
            }
        } else {
            throw new ValidationException("The link is wrong, please contact HR.");
        }

    }

    @Transactional
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

    public String hrUpdate(String applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            Job job = application.getJob();
            List<Step> stepList = stepRepository.findByJobId(job.getId());
            if (stepList == null || stepList.size() == 0)
                stepList = stepRepository.findByJobId("-1");

            stepList.sort((a, b) -> Double.compare(a.getIndex(), b.getIndex()));
            //  || , former is step 0!
            if (Math.abs(Double.valueOf(application.getStep().replace("+", "").replace("-", "")) - stepList.get(0).getIndex()) < 0.01 || application.getStep().charAt(0) == '+') {
                Optional<Step> stepOptional = stepList.stream().filter(tr -> tr.getIndex() > Double.valueOf(application.getStep().replace("+", ""))).findFirst();
                if (stepOptional.isPresent()) {
                    return stepOptional.get().getIndex() + "";
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


}

