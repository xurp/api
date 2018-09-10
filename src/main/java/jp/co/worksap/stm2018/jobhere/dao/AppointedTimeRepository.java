package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.domain.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AppointedTimeRepository extends JpaRepository<AppointedTime, String> {
}
