package jp.co.worksap.stm2018.jobhere.model.dto.response;

import jp.co.worksap.stm2018.jobhere.model.domain.Cooperator;
import jp.co.worksap.stm2018.jobhere.model.domain.Item;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ItemDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ScoreDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/4.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
    private String id;
    private Cooperator cooperator;
    private String applicationId;
    private Timestamp assessmentTime;
    private Timestamp interviewTime;
    private String comment;
    private String step;
    private String pass;
    private String operationId;
    private String score;
    private List<ScoreDTO> items;

}
