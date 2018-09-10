package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xu_xi-pc on 2018/9/10.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "outbox")
public class Outbox {
    @Id
    private String operationId;
    @Column(nullable = true)
    private String link;
    @Column(nullable = true)
    private String subject;
    @Column(nullable = true, columnDefinition = "text")
    private String content;
    @Column(nullable = true)
    private String applicationId;
}
