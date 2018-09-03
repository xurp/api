package jp.co.worksap.stm2018.jobhere.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
