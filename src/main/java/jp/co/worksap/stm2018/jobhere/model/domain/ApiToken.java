package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_token")
public class ApiToken {

    /**
     * also will be used as token
     */
    @Id
    private String id;

    @ManyToOne
    private User user;
}
