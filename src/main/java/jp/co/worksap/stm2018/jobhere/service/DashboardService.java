package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    DashboardDTO find(String hrId);

    List<Map<String, String>> export(String hrId);
}
