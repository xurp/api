package jp.co.worksap.stm2018.jobhere.model.dto.request;

import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Created by xu_xi-pc on 2018/9/3.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    private String id;
    private Resume resume;
    private Job job;
    private String step;
    private User user;
    private Timestamp createTime;
    private Timestamp updateTime;
}
