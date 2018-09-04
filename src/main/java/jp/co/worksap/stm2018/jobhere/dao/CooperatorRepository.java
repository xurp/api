package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Cooperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CooperatorRepository extends JpaRepository<Cooperator, String> {
}
