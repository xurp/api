package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.ApplicationRepository;
import jp.co.worksap.stm2018.jobhere.dao.AppointedTimeRepository;
import jp.co.worksap.stm2018.jobhere.dao.AssessmentRepository;
import jp.co.worksap.stm2018.jobhere.dao.OutboxRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.domain.Assessment;
import jp.co.worksap.stm2018.jobhere.model.domain.Outbox;
import jp.co.worksap.stm2018.jobhere.model.dto.request.AppointedTimeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ChooseDateDTO;
import jp.co.worksap.stm2018.jobhere.service.AppointedTimeService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointedTimeServiceImpl implements AppointedTimeService {

    @Autowired
    private AppointedTimeRepository appointedTimeRepository;
    @Autowired
    private OutboxRepository outboxRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private Mail mail;

    @Override
    public List<AppointedTime> getByOperationIdAndCooperatorId(String id1, String id2) {
        List<AppointedTime> appointedTimeList = appointedTimeRepository.getByOperationIdAndCooperatorId(id1, id2);
        if (appointedTimeList == null || appointedTimeList.size() == 0) {
            throw new ValidationException("The link is wrong");
        }
        return appointedTimeList;
    }
    @Transactional
    @Override
    public void update(AppointedTimeDTO appointedTimeDTO,String path) {
        List<AppointedTime> appointedTimeList = appointedTimeRepository
                .getByOperationIdAndCooperatorId(appointedTimeDTO.getOperationId(), appointedTimeDTO.getCooperatorId());
        List<AppointedTime> appointedTimeRemainList = appointedTimeRepository
                .getByOperationId(appointedTimeDTO.getOperationId());
        int nullstarttime=0;
        for(AppointedTime a:appointedTimeRemainList){
            if(a.getStartTime()==null)
                nullstarttime++;
        }
        for (int i = 0; i < appointedTimeList.size(); i++) {
            appointedTimeList.get(i).setStartTime(appointedTimeDTO.getStartTimes().get(i));
            appointedTimeList.get(i).setEndTime(appointedTimeDTO.getEndTimes().get(i));
            appointedTimeRepository.save(appointedTimeList.get(i));
        }
        System.out.println(appointedTimeList.size()+" "+nullstarttime);
        if(appointedTimeList.size()==nullstarttime){
            List<Outbox> outboxList=outboxRepository.findByOperationId(appointedTimeDTO.getOperationId());
            if(outboxList!=null&&outboxList.size()>0){
                for(Outbox outbox:outboxList){
                    String applicationId=outbox.getApplicationId();
                    Application application=applicationRepository.findById(applicationId).get();
                    String email=application.getResume().getEmail();
                    //now assessId is needed, but appointedTime only has applicationId
                    //return the newest assessment in this application
                    List<Assessment> assessmentList=assessmentRepository.findByApplicationId(applicationId);
                    //path="https://sh-stm.paas.workslan/jobhere";
                    List<Assessment> sortedList= assessmentList.stream().sorted((a, b) -> Double.compare(Double.parseDouble(a.getStep()),Double.parseDouble(b.getStep()))).collect(Collectors.toList());
                    String content="Please click the link to choose your interview time:  "+path+"/#/schedule/candidate/"+appointedTimeDTO.getOperationId()+"/"+sortedList.get(sortedList.size()-1).getId();
                    mail.send("chorespore@163.com", email, "["+application.getJob().getCompany().getCompanyName()+"] Please choose your interview time",content);
                }
            }

        }

    }

    @Override
    public Set<ChooseDateDTO> getCandidateDate(String id) {
        List<AppointedTime> appointedTimeList=appointedTimeRepository.getByOperationId(id);
        Set<ChooseDateDTO> chooseDateDTOList=new HashSet<>();
        for(AppointedTime appointedTime:appointedTimeList){
            //starttime is different
            chooseDateDTOList.add(ChooseDateDTO.builder()
                    .startTime(appointedTime.getStartTime())
                    .endTime(appointedTime.getEndTime()).build());
        }
        if(chooseDateDTOList.size()==0){
            throw new ValidationException("Sorry, there is no available time now. We will send you another selecting date email soon.");
        }
        return chooseDateDTOList;
    }
}
