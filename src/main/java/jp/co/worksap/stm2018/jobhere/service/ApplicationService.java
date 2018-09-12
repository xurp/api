package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;

import java.util.List;

public interface ApplicationService {

    ApplicationDTO save(String jobId, String resumeId, String userId);

    ApplicationDTO link(String jobId, String resumeId);

    ApplicationDTO find(String applicationId);

    List<ApplicationDTO> list(String jobId, String step);

    void update(AssessmentDTO assessmentDTO);


    public void updateApplicationStep(String applicationId);

    public void decline(EmailDTO emailDTO);

}
