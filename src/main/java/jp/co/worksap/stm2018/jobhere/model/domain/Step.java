package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by xu_xi-pc on 2018/9/4.
 */
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "step")
public class Step {
    @Id
    private String id;

    @Column(nullable = false)
    private String jobId;

    @Column(nullable = false)
    private double index;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    public Step(){
        this.id=UUID.randomUUID().toString().replace("-", "");
    }
}
