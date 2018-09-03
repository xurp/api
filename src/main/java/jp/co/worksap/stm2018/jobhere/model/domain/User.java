package jp.co.worksap.stm2018.jobhere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    /**
     * encrypt password with bcrypt
     *
     * ** password need to be sha1 encrypted already at front-end **
     */
    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Id
    private String id;

    @Column(nullable = false,unique = true)
    private String username;

    /**
     * password
     * - bcrypt(sha1(origin))
     * - front-end will encrypt the `sha1` part
     */
    @Column(nullable = false)
    private String password;

    /**
     * admin,hr,candidate,hr-n
     */
    private String role;

    @Column(nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,optional=true)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=true)
    @JoinColumn(name="company_id")
    @JsonIgnore
    private Company company;

    //can not use User.getApplications!
    @OneToMany(mappedBy = "user",cascade= CascadeType.MERGE,fetch= FetchType.LAZY)
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
