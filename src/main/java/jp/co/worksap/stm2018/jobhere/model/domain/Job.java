package jp.co.worksap.stm2018.jobhere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job {


    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false,columnDefinition = "text")
    private String detail;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    @Column(nullable = false)
    private Timestamp updateTime;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String remark;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=true)
    @JoinColumn(name="company_id")
    @JsonIgnore
    private Company company;
}
