package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Offer;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OfferRepository extends JpaRepository<Offer, String> {
    List<Offer> findByCompanyId(String companyId);

    List<Offer> findByApplicationId(String applicationId);
}
