package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.InvitationDTO;
import jp.co.worksap.stm2018.jobhere.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    @Autowired
    InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("")
    @NeedLogin
    InvitationDTO create(HttpServletRequest request, @RequestParam String resumeId, @RequestParam String jobId) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            return invitationService.create(resumeId, jobId);
        } else {
            throw new ValidationException("Permission Denied!");
        }
    }

    @GetMapping("")
    @NeedLogin
    List<InvitationDTO> list(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        return invitationService.list(user);
    }
}
