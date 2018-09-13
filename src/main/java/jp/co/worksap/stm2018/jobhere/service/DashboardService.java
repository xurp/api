package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;

public interface DashboardService {
    DashboardDTO find(String hrId);
}
