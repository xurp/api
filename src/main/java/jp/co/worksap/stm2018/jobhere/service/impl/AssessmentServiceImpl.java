package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.NotFoundException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ItemDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ScoreDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApplicationAndAssessmentDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.service.AssessmentService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    private final Mail mail;


    @Autowired
    AssessmentServiceImpl(Mail mail, CompanyRepository companyRepository, OutboxRepository outboxRepository, StepRepository stepRepository, AppointedTimeRepository appointedTimeRepository, ApplicationRepository applicationRepository, CooperatorRepository cooperatorRepository, AssessmentRepository assessmentRepository) {
        this.mail = mail;
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
                appointedTime.setPeriods(emailDto.getPeriods());
                appointedTime.setOperationId(emailDto.getOperationId());
                appointedTimeRepository.save(appointedTime);
            }
            // }

            //subject and content are in the dto
            System.out.println(batchindex + " " + cooperatorNum);
            if (batchindex < cooperatorNum) {//only need send cooperatorNum emails
                System.out.println("begin to send email");
                String content = emailDto.getContent();
                String companyName = companyRepository.findById(cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getCompanyId()).get().getCompanyName();
                content = content.replaceAll("\\[assessor_name\\]", cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getName());
                content = content.replaceAll("\\[company_name\\]", companyName);
                content = content.replaceAll("\\[operation_id\\]", emailDto.getOperationId());
                content = content.replaceAll("\\[cooperation_id\\]", emailDto.getCooperatorIds().get(batchindex % cooperatorNum));
                mail.send("chorespore@163.com", cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getEmail(), cooperatorRepository.findById(emailDto.getCooperatorIds().get(batchindex % cooperatorNum)).get().getName()+" -- "+ emailDto.getSubject(), content);
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
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //assessment.setAssessmentTime(timestamp);
            assessmentRepository.save(assessment);
            application.setStep(newstep);
            applicationRepository.save(application);
            batchindex++;
        }
    }

    @Transactional
    @Override
    public void schedule(AssessmentDTO assessmentDTO, String path) {
        Assessment assessment = assessmentRepository.getOne(assessmentDTO.getId());
        List<AppointedTime> appointedTimeList = appointedTimeRepository.getByOperationIdAndStartTime(assessmentDTO.getOperationId(), assessmentDTO.getInterviewTime());
        List<AppointedTime> appointedTimeAllList = appointedTimeRepository.getByOperationId(assessmentDTO.getOperationId());
        if (appointedTimeList == null || appointedTimeList.size() == 0)
            throw new ValidationException("Sorry, this time is selected just now");
        //case1:appointedTimeList=1 and appointedTimeAllList=3 and the same cooperator; ->single   or 3candidate vs 1 cooperator
        //case2:appointedTimeList=1 and appointedTimeAllList=3 and different cooperators selected the same time;
        // case3:other
        AppointedTime appointedTime = appointedTimeList.get(0);
        Optional<Cooperator> cooperatorOptional = cooperatorRepository.findById(appointedTime.getCooperatorId());
        if (cooperatorOptional.isPresent())
            assessment.setCooperator(cooperatorOptional.get());
        assessment.setInterviewTime(assessmentDTO.getInterviewTime());

        assessmentRepository.save(assessment);
        boolean flag = true;
        String cooperatorId = appointedTimeList.get(0).getCooperatorId();
        for (AppointedTime a : appointedTimeAllList) {
            if (!a.getCooperatorId().equals(cooperatorId))//string equals!
                flag = false;
        }
        if (appointedTimeAllList.size() == 3 && flag) {//case1
            if(appointedTimeAllList.get(0).getApplicationId().equals(appointedTimeAllList.get(1).getApplicationId())) {//case 1.1
                List<String> deleteList = new ArrayList<>();
                appointedTimeAllList.forEach(a -> deleteList.add(a.getId()));
                deleteList.forEach(id -> appointedTimeRepository.deleteById(id));
            }
            else{//case 1.2
                appointedTimeRepository.deleteById(appointedTime.getId());
            }
        } else {
            appointedTimeRepository.deleteById(appointedTime.getId());
        }

//        Westgate Mall 9F, No.1038 West Nanjing Road, Jing An District, Shanghai
        Application application = applicationRepository.getOne(assessment.getApplicationId());
        Resume resume = application.getResume();
        Job job = application.getJob();
        Company company = job.getCompany();
        Cooperator cooperator = cooperatorOptional.get();
        String timeStr = assessment.getInterviewTime().toString();

        String content = "Dear Evaluator:\n\t" +
                "Please help to give assessment to this job seeker, detailed information about this person is listed in the link below. " +
                "The assessment can only be make once, so please MADE YOUR DECISION CAUTIOUSLY! \n" +
                "  Interview time:" + timeStr.substring(0, timeStr.lastIndexOf(":")) + "\n" +
                "　Interview place: Westgate Mall 9F, No.1038 West Nanjing Road, Jing An District, Shanghai \n" +
                "                                " + path + "#/assess/" + assessmentDTO.getId() + "\n\tinterview time:"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(assessmentDTO.getInterviewTime()) +
                "\n\tposition:" + job.getName() + "\n\tBest Regards,\n" + company.getCompanyName();
        mail.send("chorespore@163.com", cooperator.getEmail(), cooperator.getName()+ " -- Assessment Invitation", content);


        String msgToCandidate = "Dear " + resume.getName() + ":\n\t" +
                "　Our company human resources department has received your feedback of time preference, thank you for your cooperation.\n" +
                "  We officially inform you to our company to participate in the interview. Specific requirements are as follows.\n" +
                "  Interview time:" + timeStr.substring(0, timeStr.lastIndexOf(":")) + "\n" +
                "　Interview place: Westgate Mall 9F, No.1038 West Nanjing Road, Jing An District, Shanghai" +
                "\n\tposition:" + job.getName() + "\n\tBest Regards,\n" + company.getCompanyName();
        mail.send("chorespore@163.com", resume.getEmail(),  resume.getName()+ " -- Interview Invitation of " + job.getName(), msgToCandidate);

    }

    public void resendEmail(EmailDTO emailDTO) {
        String applicationId = emailDTO.getApplicationId();
        //1.interviewer has not selected date; 2.selected, but candidate not selected; 3.candidate selected so appointedtime's record is deleted and assessment has cooperator and interview time and the status is assessing
        //now only consider case 3

        List<AppointedTime> appointedTimeList = appointedTimeRepository.getByApplicationId(applicationId);
        if (appointedTimeList == null || appointedTimeList.size() == 0) {

            //List<Assessment> assessmentList = assessmentRepository.findByApplicationId(applicationId);
            //List<Assessment> sortedList = assessmentList.stream().sorted((a, b) -> Double.compare(Double.parseDouble(a.getStep()),Double.parseDouble(b.getStep()))).collect(Collectors.toList());
            //Assessment assessment=sortedList.get(sortedList.size()-1);
            System.out.println(emailDTO.getAssessId());
            Optional<Assessment> assessmentOptional = assessmentRepository.findById(emailDTO.getAssessId());
            if (!assessmentOptional.isPresent())
                throw new ValidationException("The link is wrong, please contact HR.");
            Assessment assessment = assessmentOptional.get();
            if (!assessment.getPass().equals("assessing")) {
                throw new ValidationException("Interviewer has assessed the candidate.");
            }

            if (assessment.getCooperator() != null) {//case 3
                //last operationId's outbox has sent, so do not need to delete them. here, save outbox of new operationId
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

                    appointedTime.setPeriods(emailDTO.getPeriods());
                    appointedTime.setOperationId(emailDTO.getOperationId());
                    appointedTimeRepository.save(appointedTime);

                }


                String content = emailDTO.getContent();
                String companyName = companyRepository.findById(cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getCompanyId()).get().getCompanyName();
                content = content.replaceAll("\\[assessor_name\\]", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName());
                content = content.replaceAll("\\[company_name\\]", companyName);
                content = content.replaceAll("\\[operation_id\\]", emailDTO.getOperationId());
                content = content.replaceAll("\\[cooperation_id\\]", emailDTO.getCooperatorId());
                mail.send("chorespore@163.com", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getEmail(),  cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName()+" -- "+ emailDTO.getSubject(), content);
                assessmentRepository.deleteById(assessment.getId());
                Assessment assessment1 = new Assessment();
                assessment1.setId(UUID.randomUUID().toString().replace("-", ""));
                assessment1.setApplicationId(applicationId);
                //Cooperator cooperator = cooperatorRepository.findById(emailDTO.getCooperatorId()).get();
                //assessment1.setCooperator(cooperator);

                Application application = applicationRepository.findById(applicationId).get();
                mail.send("chorespore@163.com", application.getResume().getEmail(), application.getResume().getName()+" -- " + "Please repick your interview time",
                        "Dear " + application.getResume().getName() + ":\n\tWe are sorry to tell you that your interview time will be changed. You will receive another email to choose your new interview date. Thank you for supporting our recruitment!");
                assessment1.setStep(application.getStep());
                assessment1.setComment(" ");
                assessment1.setPass("assessing");//if not assessing, exception has been thrown
                //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                //assessment1.setAssessmentTime(timestamp);
                assessmentRepository.save(assessment1);


            }
        } else {
            //case1 and case 2:appointedtime exists and cooperator&&interview time in assessment is null
            List<String> toDeleteList = new ArrayList<>();
            for (AppointedTime a : appointedTimeList) {
                toDeleteList.add(a.getId());
            }
            Optional<Assessment> assessmentOptional = assessmentRepository.findById(emailDTO.getAssessId());
            if (!assessmentOptional.isPresent())
                throw new ValidationException("The link is wrong, please contact HR.");
            Assessment assessment = assessmentOptional.get();
            if (!assessment.getPass().equals("assessing")) {
                throw new ValidationException("Interviewer has assessed the candidate.");
            }
            //here, old outbox and appointedtime should be deleted
            //delete appointedtime of this operation&&cooperator&&application,delete outbox of this operation&&application
            outboxRepository.deleteByOperationIdAndApplicationId(emailDTO.getOperationId(), emailDTO.getApplicationId());
            Outbox outbox = new Outbox();
            outbox.setId(UUID.randomUUID().toString().replace("-", ""));
            outbox.setOperationId(emailDTO.getOperationId());
            outbox.setApplicationId(applicationId);
            outbox.setContent("");
            outbox.setLink("");
            outbox.setSubject("");
            outboxRepository.save(outbox);
            //if candidate open the first email, exceptions has thrown. so the candidate can open the new email to select date.
            appointedTimeRepository.deleteByOperationIdAndCooperatorIdAndApplicationId(emailDTO.getOperationId(), emailDTO.getCooperatorId(), emailDTO.getApplicationId());
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

                appointedTime.setPeriods(emailDTO.getPeriods());
                appointedTime.setOperationId(emailDTO.getOperationId());
                appointedTimeRepository.save(appointedTime);

            }


            String content = emailDTO.getContent();
            String companyName = companyRepository.findById(cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getCompanyId()).get().getCompanyName();
            content = content.replaceAll("\\[assessor_name\\]", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName());
            content = content.replaceAll("\\[company_name\\]", companyName);
            content = content.replaceAll("\\[operation_id\\]", emailDTO.getOperationId());
            content = content.replaceAll("\\[cooperation_id\\]", emailDTO.getCooperatorId());
            mail.send("chorespore@163.com", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getEmail(), cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName()+" -- "+  emailDTO.getSubject(), content);


        }

    }


    @Override
    public void reassessment(EmailDTO emailDTO) {
        //id:assessmentId
        Assessment assessment = assessmentRepository.getOne(emailDTO.getAssessId());
        System.out.println("reass id:"+emailDTO.getAssessId());
        if (assessment != null) {
            assessment.setPass("assessing");
            assessment.setComment("");
            assessment.setScore("");
            assessmentRepository.save(assessment);
            Optional<Application> applicationOptional = applicationRepository.findById(assessment.getApplicationId());
            if (applicationOptional.isPresent()) {
                Application application = applicationOptional.get();
                application.setStep(application.getStep().replace("+", "").replaceAll("-", ""));
                applicationRepository.save(application);
            }
            String content = emailDTO.getContent();
            String companyName = companyRepository.findById(cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getCompanyId()).get().getCompanyName();
            content = content.replaceAll("\\[assessor_name\\]", cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName());
            content = content.replaceAll("\\[assess_id\\]", emailDTO.getAssessId());
            content = content.replaceAll("\\[company_name\\]", companyName);


            mail.send("chorespore@163.com", emailDTO.getReceiver(),  cooperatorRepository.findById(emailDTO.getCooperatorId()).get().getName()+" -- "+ emailDTO.getSubject(), content);

        } else {
            throw new ValidationException("The link is wrong, please contact HR.");
        }

    }

    //this method may not be used.
    @Transactional
    @Override
    public AssessmentDTO save(String applicationId, String cooperatorId, String subject, String content, String
            assessId) {
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
        String companyName = application.getJob().getCompany().getCompanyName();
        mail.send("chorespore@163.com", cooperator.getEmail(), cooperator.getName() +" -- "+ subject, content);

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
                    .pass(assessment.getPass())
                    .interviewTime(assessment.getInterviewTime()).score(assessment.getScore()).build());
        }
        assessmentDTOList.sort((a, b) -> Double.compare(Double.parseDouble(a.getStep()), Double.parseDouble(b.getStep())));
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
                            .pass(a.getPass()).score(a.getScore()).build());
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
            List<ScoreDTO> itemList = assessmentDTO.getItems();
            String score = "";
            for (ScoreDTO item : itemList) {
                String name = item.getName();
                int s = item.getValue();
                score += name + ":" + s + ";";
            }
            assessment.setScore(score);
            if (assessmentDTO.getComment() == null)
                assessment.setComment(" ");
            else
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

