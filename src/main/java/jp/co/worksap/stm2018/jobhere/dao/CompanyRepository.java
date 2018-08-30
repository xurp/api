package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xu_xi-pc on 2018/8/30.
 */
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company findByCompanyNameAndLegalPerson(String companyName, String legalPerson);

    Company findByCompanyName(String companyName);

    List<Company> findByStatus(String status);

}
