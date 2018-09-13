package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.InvitationDTO;

import java.util.List;

public interface InvitationService {
    InvitationDTO create(String resumeId, String jobId);

    List<InvitationDTO> list(User user);
}
