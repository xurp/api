package jp.co.worksap.stm2018.jobhere.controller;

import io.swagger.annotations.ApiOperation;
import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ChooseDateDTO;
import jp.co.worksap.stm2018.jobhere.service.ApplicationService;
import jp.co.worksap.stm2018.jobhere.service.AppointedTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/11.
 */
@Slf4j
@RestController
@RequestMapping("/appointedTime")
public class AppointedTimeController {
    @Autowired
    private AppointedTimeService applicationService;

    @GetMapping("")
    ChooseDateDTO getArg(HttpServletRequest request) {
        //id:operationId
        String operationId = request.getParameter("operationId");
        String cooperatorId = request.getParameter("cooperatorId");
        List<AppointedTime> appointedTimeList=applicationService.getByOperationIdAndCooperatorId(operationId,cooperatorId);
        ChooseDateDTO chooseDateDTO=new ChooseDateDTO();
        chooseDateDTO.setStartDate(appointedTimeList.get(0).getStartDate());
        chooseDateDTO.setEndDate(appointedTimeList.get(0).getEndDate());
        chooseDateDTO.setCooperatorId(cooperatorId);
        chooseDateDTO.setOperationId(operationId);
        chooseDateDTO.setNumber(appointedTimeList.size());
        return chooseDateDTO;
    }
}
