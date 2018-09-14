package jp.co.worksap.stm2018.jobhere.controller;

import io.swagger.annotations.ApiOperation;
import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ForbiddenException;
import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.AppointedTimeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ChooseDateDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.service.AppointedTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by xu_xi-pc on 2018/9/11.
 */
@Slf4j
@RestController
@RequestMapping("/appointedTime")
public class AppointedTimeController {
    @Autowired
    private AppointedTimeService appointedTimeService;

    @GetMapping("")
    ChooseDateDTO getArg(HttpServletRequest request) {
        //id:operationId
        String operationId = request.getParameter("operationId");
        String cooperatorId = request.getParameter("cooperatorId");
        List<AppointedTime> appointedTimeList = appointedTimeService.getByOperationIdAndCooperatorId(operationId, cooperatorId);
        if (appointedTimeList.get(0).getStartTime() != null) {
            throw new ForbiddenException("You have already done the schedule arrange!");
        }

        ChooseDateDTO chooseDateDTO = new ChooseDateDTO();
        chooseDateDTO.setStartDate(appointedTimeList.get(0).getStartDate());
        chooseDateDTO.setEndDate(appointedTimeList.get(0).getEndDate());
        chooseDateDTO.setCooperatorId(cooperatorId);
        chooseDateDTO.setOperationId(operationId);
        chooseDateDTO.setNumber(appointedTimeList.size());
        return chooseDateDTO;
    }

    @GetMapping("/schedule")
    Set<ChooseDateDTO> getCandidate(HttpServletRequest request) {
        String operationId = request.getParameter("operationId");
        return appointedTimeService.getCandidateDate(operationId);
    }

    @PutMapping("")
    void update(HttpServletRequest request,@RequestBody AppointedTimeDTO appointedTimeDTO) {
        appointedTimeService.update(appointedTimeDTO,request.getHeader("Referer"));
    }
}
