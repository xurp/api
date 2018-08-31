package jp.co.worksap.stm2018.jobhere.model.dto.request;

import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private String id;

    private String name;

    private String detail;

    private int count;

    private String department;

    private String remark;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Company company;
}
