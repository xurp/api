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
 * Created by xu_xi-pc on 2018/9/4.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assessment")
public class Assessment {
    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER,optional=true)
    @JoinColumn(name = "cooperator_id")
    @JsonIgnore
    private Cooperator cooperator;

    @Column(nullable = false)
    private String applicationId;

    @Column(nullable = true)
    private Timestamp assessmentTime;

    @Column(nullable = true,columnDefinition = "text")
    private String comment;
    @Column(nullable = false)
    private String step;
    @Column(nullable = true)
    private String pass;
}
