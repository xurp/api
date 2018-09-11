package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.dto.request.AppointedTimeDTO;

import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/11.
 */
public interface AppointedTimeService {
    List<AppointedTime> getByOperationIdAndCooperatorId(String id1, String id2);

    void update(AppointedTimeDTO appointedTimeDTO);
}
