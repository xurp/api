package jp.co.worksap.stm2018.jobhere.model.dto.response;

import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
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
public class JobStepDTO {
    private String id;

    private String name;

    private String detail;

    private int count;

    private String department;

    private String remark;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Company company;

    private boolean applied;//true:candidate can apply/false:candidate has applied

    private List<Step> step;

}
