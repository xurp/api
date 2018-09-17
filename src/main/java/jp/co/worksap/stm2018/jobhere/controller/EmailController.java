package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;
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
    List<String> list(HttpServletRequest request) {
        List<String> emailList=new ArrayList<>();
        emailList.add("        Please select the time during which you will be available for the interview. Attention please, once selected, the time table can not be changed! The time selection link is below:\n");
        emailList.add("        Thank you for your support for the recruitment! Please select the time during which you will be available for the interview. The time selection link is below:\n");

        return emailList;
    }

    @GetMapping("/decline")
    @NeedLogin
    List<String> list1(HttpServletRequest request) {
        List<String> emailList=new ArrayList<>();
        emailList.add("Thank you for your application for the position. As you can imagine, we received a large number of applications. I am sorry to inform you that you have not passed this position.\n" +
                "\n" +
                "\tWe thanks you for the time you invested in applying for the shipping coordinator position. We encourage you to apply for future openings for which you qualify.\n" +
                "\n" +
                "Best wishes for a successful job search. Thank you, again, for your interest in our company.");
        emailList.add("Thank you for your application for the position. I am sorry to inform you that you have not passed this position.\n" +
                "\n" +
                "Best wishes for a successful job search. Thank you, again, for your interest in our company.");
        return emailList;
    }

    @GetMapping("/offer")
    @NeedLogin
    List<String> list2(HttpServletRequest request) {
        List<String> emailList=new ArrayList<>();
        emailList.add("Congratulations");
        emailList.add("Congratulations! You passed all the assessment. Welcome to our company! Please click the link to choose whether to accept the offer or not.");
        return emailList;

    }

    @GetMapping("/resend")
    @NeedLogin
    List<String> list3(HttpServletRequest request) {
        List<String> emailList=new ArrayList<>();
        emailList.add("This is your re-picking time url:");
        emailList.add("First of all, thank you for your support for our work. This is your re-picking time url. Please select the time again:");
        return emailList;
    }

    @GetMapping("/rearrange")
    @NeedLogin
    List<String> list4(HttpServletRequest request) {
        List<String> emailList=new ArrayList<>();
        emailList.add("This is your re-assessing url:");
        emailList.add("First of all, thank you for your support for our work. This is your re-assessing url. Please assess the candidate again:");
        return emailList;

    }


}
