package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.AppointedTimeRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.dto.request.AppointedTimeDTO;
import jp.co.worksap.stm2018.jobhere.service.AppointedTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointedTimeServiceImpl implements AppointedTimeService {

    @Autowired
    private AppointedTimeRepository appointedTimeRepository;


    @Override
    public List<AppointedTime> getByOperationIdAndCooperatorId(String id1, String id2) {
        List<AppointedTime> appointedTimeList = appointedTimeRepository.getByOperationIdAndCooperatorId(id1, id2);
        if (appointedTimeList == null || appointedTimeList.size() == 0) {
            throw new ValidationException("The link is wrong");
        }
        return appointedTimeList;
    }

    @Override
    public void update(AppointedTimeDTO appointedTimeDTO) {
        List<AppointedTime> appointedTimeList = appointedTimeRepository
                .getByOperationIdAndCooperatorId(appointedTimeDTO.getOperationId(), appointedTimeDTO.getCooperatorId());

        for (int i = 0; i < appointedTimeList.size(); i++) {
            appointedTimeList.get(i).setStartTime(appointedTimeDTO.getStartTimes().get(i));
            appointedTimeRepository.save(appointedTimeList.get(i));
        }
    }
}
