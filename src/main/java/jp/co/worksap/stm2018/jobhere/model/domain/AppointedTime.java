package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by xu_xi-pc on 2018/9/10.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointed_time")
public class AppointedTime {
    @Id
    private String id;
    @Column(nullable = true)
    private String operationId;
    @Column(nullable = true)
    private Timestamp startDate;
    @Column(nullable = true)
    private Timestamp endDate;
    @Column(nullable = true)
    private Timestamp startTime;
    @Column(nullable = true)
    private String cooperatorId;
    @Column(nullable = true)
    private String applicationId;
}
