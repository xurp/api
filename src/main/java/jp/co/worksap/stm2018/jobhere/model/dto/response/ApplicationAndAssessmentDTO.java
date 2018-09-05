package jp.co.worksap.stm2018.jobhere.model.dto.response;

import jp.co.worksap.stm2018.jobhere.model.domain.Assessment;
import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/5.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationAndAssessmentDTO {
    private String applicationId;
    private Resume resume;
    private Job job;
    private String step;
    private List<Assessment> assessments;
    private List<Step> stepList;
}
