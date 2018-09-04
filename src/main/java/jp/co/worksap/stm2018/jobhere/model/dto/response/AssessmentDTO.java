package jp.co.worksap.stm2018.jobhere.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Cooperator;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private Timestamp assementTime;

    private String comment;

    private String step;

}
