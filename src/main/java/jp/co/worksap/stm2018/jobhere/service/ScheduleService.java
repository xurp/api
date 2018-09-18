package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.response.AssessmentDTO;

import java.util.List;

public interface ScheduleService {
    List<AssessmentDTO> getAssessments(String candidateId);

}
