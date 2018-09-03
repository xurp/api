package jp.co.worksap.stm2018.jobhere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name = "application")
public class Application {
    @Id
    private String id;
    @OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name = "resume_id")
    @JsonIgnore
    private Resume resume;
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="job_id")
    @JsonIgnore
    private Job job;

    private String step;
}
