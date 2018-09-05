package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.Assessment;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApplicationAndAssessmentDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;

import java.util.List;

public interface AssessmentService {

    AssessmentDTO save(String applicationId, String cooperatorId);

    List<AssessmentDTO> list(String applicationId);

    ApplicationAndAssessmentDTO getDetail(String id);


}
