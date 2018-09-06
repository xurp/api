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
 * Created by xu_xi-pc on 2018/9/6.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offer")
public class Offer {
    @Id
    private String id;

    @Column(nullable = true)
    private String result;

    @Column(nullable = false)
    private String sendStatus;

    @Column(nullable = true,columnDefinition = "text")
    private String remark;

    @Column(nullable = false)
    private String applicationId;

    @Column(nullable = false)
    private String companyId;

    @Column(nullable = false)
    private Timestamp offerTime;

    @Column(nullable = true)
    private Timestamp respondTime;
}
