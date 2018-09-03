package jp.co.worksap.stm2018.jobhere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "job",cascade= CascadeType.MERGE,fetch= FetchType.LAZY)
    private List<Application> applications;

    public void addApplication(Application application) {
        if(this.applications==null)
            this.applications=new ArrayList<>();
        this.applications.add(application);
    }
    public void removeApplication(String applicationId) {
        for (int index=0; index < this.applications.size(); index ++ ) {
            if (applications.get(index).getId() .equals(applicationId)) {
                this.applications.remove(index);
                break;
            }
        }
    }
}
