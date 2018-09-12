package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Offer;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.OfferDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.OfferService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/6.
 */
@RestController
@RequestMapping("/offer")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("")
    @NeedLogin
    List<OfferDTO> list(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            return offerService.list(user.getCompany().getId());
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

    @PutMapping("")
    @NeedLogin
    void update(HttpServletRequest request, @RequestBody EmailDTO emailDTO) {
        System.out.println(emailDTO.getOfferId());
        System.out.println(emailDTO.getSubject());
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            emailDTO.setContent(emailDTO.getContent() + "\n\n https://sh-stm.paas.workslan/jobhere/#/o/" + emailDTO.getOfferId());
            offerService.update(emailDTO);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }


}

