package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.response.InvitationDTO;

public interface InvitationService {
    InvitationDTO create(String resumeId, String jobId);
}
