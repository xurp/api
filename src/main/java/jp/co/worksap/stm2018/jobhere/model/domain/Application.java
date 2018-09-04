package jp.co.worksap.stm2018.jobhere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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
    @OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER,optional=false)
    @JoinColumn(name = "resume_id")
    @JsonIgnore
    private Resume resume;
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="job_id")
    @JsonIgnore
    private Job job;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=true)//only for alter table
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;//one side, User has not 'applications'

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    @Column(nullable = false)
    private Timestamp updateTime;

    private String step;
}
