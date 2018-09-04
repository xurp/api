package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.model.domain.Assessment;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;

import java.util.List;

public interface AssessmentService {

    AssessmentDTO save(String applicationId, String cooperatorId);



}
