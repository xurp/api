package jp.co.worksap.stm2018.jobhere.controller;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import jp.co.worksap.stm2018.jobhere.service.DashboardService;
import jp.co.worksap.stm2018.jobhere.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/hr")
    @NeedLogin
    DashboardDTO find(HttpServletRequest request) {
        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {

            return dashboardService.find(user.getId());
        } else {
            throw new ValidationException("Permission Denied!");
        }

    }


    /***
     * 批量导出表面质量检验记录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "exportSurface", method = RequestMethod.GET)
    @NeedLogin
    public void exportSurface(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = (User) request.getAttribute("getuser");
        if (user.getRole().equals("hr")) {
            DashboardDTO res = dashboardService.find(user.getId());
        } else {
            throw new ValidationException("Permission Denied!");
        }

        //参数获取及处理，业务相关不展示
        //把要填写的数据放在一个map里
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sequence", "0001");//mock编号
        map.put("chetaihao", "1#");//mock车台号
        map.put("productName", "预应力钢绞线");//mock品名
        map.put("specification", "规格");//mock规格
        map.put("memo", "备注");//mock备注
        ExcelUtils.exportInspectionRecordSurface(request, response, map);
    }

}
