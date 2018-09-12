package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {
    private final AuthService authService;
    private final ResumeService resumeService;

    @Autowired
    public ResumeController(AuthService authService, ResumeService resumeService) {
        this.authService = authService;
        this.resumeService = resumeService;
    }

    @PutMapping("/{id}")//userid
    @NeedLogin
    void update(@PathVariable("id") String id, @RequestBody ResumeDTO resumeDTO) {
        resumeService.update(id, resumeDTO);
    }

    @GetMapping("/{id}")//userid
    @NeedLogin
    ResumeDTO find(@PathVariable("id") String id) {
        return resumeService.find(id);
    }

    @GetMapping("")
//    @NeedLogin
    List<ResumeDTO> list(@RequestParam("keyword") String keyword) {
        return resumeService.list(keyword);
    }

}
