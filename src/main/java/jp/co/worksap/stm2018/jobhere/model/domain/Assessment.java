package jp.co.worksap.stm2018.jobhere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
@Entity
@Table(name = "assessment")
public class Assessment {
    @Id
    private String id;
    //before interviewer chooses date, it is null
    //spring.jackson.serialization.fail-on-empty-beans=false than...LAZY!!!
    //can not be all, else cooperator will be deleted
    //cascade = CascadeType.DETACH

    @OneToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name = "cooperator_id")
    @JsonIgnore
    private Cooperator cooperator;

    /*@ManyToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER) //CascadeType.MERGE may not need, because it is not necessary to add cooperator from assessment
    @JoinTable(name = "assessmentCooperator", joinColumns = @JoinColumn(name = "assessment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "cooperator_id", referencedColumnName = "id"))
    private List<Cooperator> cooperators;*/

    @Column(nullable = false)
    private String applicationId;

    @Column(nullable = true)
    private Timestamp assessmentTime;

    @Column(nullable = true)
    private Timestamp interviewTime;

    @Column(nullable = true,columnDefinition = "text")
    private String comment;
    @Column(nullable = false)
    private String step;
    @Column(nullable = true)
    private String pass;
    @Column(nullable = true)
    private String score;
}
