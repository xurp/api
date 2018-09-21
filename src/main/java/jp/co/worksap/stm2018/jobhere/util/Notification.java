package jp.co.worksap.stm2018.jobhere.util;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.dao.ApplicationRepository;
import jp.co.worksap.stm2018.jobhere.dao.AssessmentRepository;
import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Assessment;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/17.
 */
@Service
public class Notification {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private Mail mail;


    @Transactional
    @Scheduled(cron = "0 0 11 ? * *" )
    @NeedLogin
    public void reportCurrentTime() {
        List<Assessment> assessmentRepositoryList=assessmentRepository.findAll();
        for(Assessment assessment:assessmentRepositoryList){
            if(assessment.getInterviewTime()!=null){
                int year=new Date(assessment.getInterviewTime().getTime()).getYear();
                int month=new Date(assessment.getInterviewTime().getTime()).getMonth();
                int day=new Date(assessment.getInterviewTime().getTime()).getDate();
                Date now=new Date();
                if(year==now.getYear()&&month==now.getMonth()&&day-1==now.getDate()){
                    Application application=applicationRepository.findById(assessment.getApplicationId()).get();
                    String companyName=companyRepository.findById(assessment.getCooperator().getCompanyId()).get().getCompanyName();
                    mail.send("chorespore@163.com", assessment.getCooperator().getEmail(), "Assessment remind", "Dear "+assessment.getCooperator().getName()+",\n\t Your interview time for "+application.getResume().getName()+" is "+assessment.getInterviewTime()+"(tomorrow). Do not forget it. Thank you.\n\tBest Regards,\n"+companyName);
                    mail.send("chorespore@163.com", assessment.getCooperator().getEmail(), "Interview remind", "Dear "+application.getResume().getName()+",\n\t Your interview time is "+assessment.getInterviewTime()+"(tomorrow). Do not forget it. Thank you.\n\tBest Regards,\n"+companyName);

                }
            }
        }
    }
}
