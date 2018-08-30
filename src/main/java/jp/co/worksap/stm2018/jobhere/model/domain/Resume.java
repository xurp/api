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


@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "resume")
public class Resume {

    @Id
    private String id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private int age;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String degree;

    @Column(nullable = true)
    private String school;

    @Column(nullable = true)
    private String major;

    @Column(nullable = true,columnDefinition = "text")
    private String intro;

    @Column(nullable = true)
    private boolean open;

    public Resume(){
        this.id= UUID.randomUUID().toString().replace("-", "");
    }
}
