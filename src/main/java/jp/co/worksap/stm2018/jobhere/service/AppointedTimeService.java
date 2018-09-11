package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.dto.request.AppointedTimeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ChooseDateDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by xu_xi-pc on 2018/9/11.
 */
public interface AppointedTimeService {
    List<AppointedTime> getByOperationIdAndCooperatorId(String id1, String id2);

    void update(AppointedTimeDTO appointedTimeDTO,String path);
    Set<ChooseDateDTO> getCandidateDate(String id);
}
