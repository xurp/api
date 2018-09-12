package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.domain.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
public interface AppointedTimeRepository extends JpaRepository<AppointedTime, String> {
    List<AppointedTime> getByApplicationId(String id);

    List<AppointedTime> getByOperationId(String id);

    List<AppointedTime> getByOperationIdAndCooperatorId(String id1, String id2);

    AppointedTime getFirstByStartTime(Timestamp startTime);

}
