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
        emailList.add(new EmailTemplate("template1","Dear [assessor_name]\n" +
                "\tPlease select the time during which you will be available for the interview. Attention please, once selected, the time table can not be changed! The time selection link is below: \n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards\n" +
                "[company_name]"));
        emailList.add(new EmailTemplate("template2","Dear [assessor_name]\n" +
                "\tThank you for your support for the recruitment! Please select the time during which you will be available for the interview. The time selection link is below: \n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards\n" +
                "[company_name]"));


        return emailList;
    }

    @GetMapping("/decline")
    @NeedLogin
    List<EmailTemplate> list1(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("template1","Dear [candidate_name]:\n" +
                "\tThank you for your application for the position [[position_name]]. As you can imagine, we received a large number of applications. I am sorry to inform you that you have not passed this position.\n" +
                "\tWe thanks you for the time you invested in applying for the shipping coordinator position. We encourage you to apply for future openings for which you qualify.\n" +
                "\n" +
                "Best wishes for a successful job search. Thank you, again, for your interest in our company.\n\tBest Regards,\n[company_name]"));
        emailList.add(new EmailTemplate("template2","Dear [candidate_name]:\n\tThank you for your application for the position [[position_name]]. I am sorry to inform you that you have not passed this position.\n\t" +
                "Best wishes for a successful job search. Thank you, again, for your interest in our company.\n\tBest Regards,\n[company_name]"));
        return emailList;
    }

    @GetMapping("/offer")
    @NeedLogin
    List<EmailTemplate> list2(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("template1","Dear [candidate_name]:\n\tCongratulations. We will give you the offer!\n\tWe will contact you soon.\n\tPosition name:[position_name]\n\tBest Regards,\n[company_name]"));
        emailList.add(new EmailTemplate("template2","Dear [candidate_name]:\n\tCongratulations! You passed all the assessment. Welcome to our company!\n\tWe will contact you soon.\n\tPosition name:[position_name]\n\tBest Regards,\n[company_name]"));
        return emailList;

    }

    @GetMapping("/rearrange")
    @NeedLogin
    List<EmailTemplate> list3(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("template1","Dear [assessor_name]\n" +
                "\tThis is your re-picking time url:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));
        emailList.add(new EmailTemplate("template2","Dear [assessor_name]\n" +
                "\tFirst of all, thank you for your support for our work. This is your re-picking time url. Please select the time again:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/schedule/interview/[operation_id]/[cooperation_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));
        return emailList;
    }

    @GetMapping("/resend")
    @NeedLogin
    List<EmailTemplate> list4(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("template1","Dear [assessor_name]\n" +
                "\tThis is your re-assessing time url:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/assess/[assess_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));
        emailList.add(new EmailTemplate("template2","Dear [assessor_name]\n" +
                "\tFirst of all, thank you for your support for our work. This is your re-assessing url. Please assess the candidate again:\n" +
                "\t\t"+request.getHeader("Referer")+"/#/assess/[assess_id]\n" +
                "\tBest Regards,\n" +
                "[company_name]"));

        return emailList;

    }

    @GetMapping("/rejectoffer")
    @NeedLogin
    List<EmailTemplate> list5(HttpServletRequest request) {
        List<EmailTemplate> emailList=new ArrayList<>();
        emailList.add(new EmailTemplate("template1","Dear [candidate_name]:\n\tUnfortunately, you reject our offer. Best wishes for a successful job search. \n\tBest Regards,\n[company_name]"));
        emailList.add(new EmailTemplate("template2","Dear [candidate_name]:\n\tYou are the talent we really want. However, we hear that you reject our offer. Best wishes for a successful job search. \n\tBest Regards,\n[company_name]"));
        return emailList;

    }


}
