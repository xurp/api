package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(nullable = false)
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
}
