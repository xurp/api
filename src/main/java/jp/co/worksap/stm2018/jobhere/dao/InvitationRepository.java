package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation,String> {

}
