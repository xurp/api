package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
public interface ItemRepository extends JpaRepository<Item, String> {

}
