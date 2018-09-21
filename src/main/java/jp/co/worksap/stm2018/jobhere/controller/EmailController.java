package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/17.
 */
@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
    @GetMapping("/interviewerDate")
    @NeedLogin
    List<EmailTemplate> list(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("interviewer choose date--formal","Dear [assessor_name]\n" +
                "\tThank you for your support for the recruitment! Please select the time during which you will be available for the interview. The time selection link is below: \n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards\n" +
                "[company_name]"));
        emailList.add(new EmailTemplate("interviewer choose date--informal","Dear [assessor_name]\n" +
                "\tThe interview time selection link is below. Be careful not to choose your time with your EYES CLOSED. ^_^\n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards\n" +
                "[company_name]"));


        return emailList;
    }

    @GetMapping("/decline")
    @NeedLogin
    List<EmailTemplate> list1(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("Rejection Letter--formal","Dear [candidate_name]:\n" +
                "\tThank you for your application for the position [[position_name]]. As you can imagine, we received a large number of applications. I am sorry to inform you that you have not passed this position.\n" +
                "\tWe thanks you for the time you invested in applying for the shipping coordinator position. We encourage you to apply for future openings for which you qualify." +
                "\n\t" +
                "Best wishes for a successful job search. Thank you, again, for your interest in our company.\n\tBest Regards,\n[company_name]"));
        emailList.add(new EmailTemplate("Rejection Letter--informal","Dear [candidate_name]:\n\tThank you for your application for the position [[position_name]]. I am sorry to inform you that you have not passed this position.\n\t" +
                "Believe yourself. You may buy our company in the future. ^_^ \n\tBest Regards,\n[company_name]"));
        return emailList;
    }

    @GetMapping("/offer")
    @NeedLogin
    List<EmailTemplate> list2(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("offer--formal","Dear [candidate_name]:\n\tCongratulations. We will give you the offer!\n\tWe will contact you soon.\n\tPosition name:[position_name]\n\tBest Regards,\n[company_name]"));
        emailList.add(new EmailTemplate("offer--informal","Dear [candidate_name]:\n\tCongratulations! You passed all the assessment. You are sooooooooooooooooooooooo perfect.  Welcome to our company!\n\tWe will contact you soon.\n\tPosition name:[position_name]\n\tBest Regards,\n[company_name]"));
        return emailList;

    }

    @GetMapping("/rearrange")
    @NeedLogin
    List<EmailTemplate> list3(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("rearrange--formal","Dear [assessor_name]\n" +
                "\tFirst of all, thank you for your support for our work. This is your re-picking interview time url. Please select the time again:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));
        emailList.add(new EmailTemplate("rearrange--informal","Dear [assessor_name]\n" +
                "\tYou're the guy! We can't recruit employees without you! This is your re-picking interview time url. Please select the time again:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));
        return emailList;
    }

    @GetMapping("/resend")
    @NeedLogin
    List<EmailTemplate> list4(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("reassess--formal","Dear [assessor_name]\n" +
                "\tFirst of all, thank you for your support for our work. This is your re-assessing url. Please assess the candidate again:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/assess/[assess_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));
        emailList.add(new EmailTemplate("reassess--informal","Dear [assessor_name]\n" +
                "\tYou made a mistake in such an important matter. I want to hit your chest with my little little punch... This is your re-assessing url. Please assess the candidate again:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/assess/[assess_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));

        return emailList;

    }

    @GetMapping("/rejectoffer")
    @NeedLogin
    List<EmailTemplate> list5(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("reject offer-formal","Dear [candidate_name]:\n\tYou are the talent we really want. However, we hear that you reject our offer. Best wishes for a successful job search. \n\tBest Regards,\n[company_name]"));
        emailList.add(new EmailTemplate("reject offer-informal","Dear [candidate_name]:\n\tWhen I heard that you refused our offer, I cried all night :( . Anyway, best wishes for a successful job search. \n\tBest Regards,\n[company_name]"));
        return emailList;

    }


}
