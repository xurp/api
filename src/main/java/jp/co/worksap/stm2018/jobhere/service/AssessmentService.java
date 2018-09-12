package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApplicationAndAssessmentDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;

import java.util.List;

public interface AssessmentService {

    AssessmentDTO save(String applicationId, String cooperatorId, String subject, String content, String assessId);

    List<AssessmentDTO> list(String applicationId);

    ApplicationAndAssessmentDTO getDetail(String id);

    void update(AssessmentDTO assessmentDTO);

    void saveOutboxAndMakeAppointment(EmailDTO emailDto);

    void schedule(AssessmentDTO assessmentDTO);

}
