package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Invitation;
import jp.co.worksap.stm2018.jobhere.model.dto.response.InvitationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
    List<Invitation> getByJobId(String jobId);

    List<Invitation> getByUserId(String userId);

}
