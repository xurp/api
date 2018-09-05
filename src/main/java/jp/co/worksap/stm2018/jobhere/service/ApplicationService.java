package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;

import java.util.List;

public interface ApplicationService {

    ApplicationDTO save(String jobId, String resumeId, String userId);

    ApplicationDTO find(String applicationId);

    List<ApplicationDTO> list(String jobId, String step);

    void update(AssessmentDTO assessmentDTO);

    void hrUpdate(String applicationId);

}
