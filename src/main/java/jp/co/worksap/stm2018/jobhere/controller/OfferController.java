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

    @GetMapping("candidate")
    @NeedLogin
    List<OfferDTO> offersOfCandidate(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("candidate")) {
            return offerService.offersOfCandidate(user.getId());
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

    @PutMapping("")
    @NeedLogin
    void update(HttpServletRequest request, @RequestBody EmailDTO emailDTO) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            //String path = request.getHeader("Referer");
            //emailDTO.setContent(emailDTO.getContent() + "\n\n" + path + "#/o/" + emailDTO.getOfferId());
            offerService.update(emailDTO);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

    @GetMapping("/{offerId}")
    OfferDTO find(@PathVariable("offerId") String offerId) {

        return offerService.find(offerId);
    }


    @PutMapping("/{offerId}")
    void offer(@PathVariable("offerId") String offerId, @RequestParam("result") String result) {
        offerService.offer(offerId, result);
    }

}

