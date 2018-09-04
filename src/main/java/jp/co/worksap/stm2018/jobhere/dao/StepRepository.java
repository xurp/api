package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface StepRepository extends JpaRepository<Step, String> {
    List<Step> findByJobId(String id);
}
