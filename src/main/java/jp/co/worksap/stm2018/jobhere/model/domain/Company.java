package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by xu_xi-pc on 2018/8/30.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    private String id;
    @Column(nullable = true, unique = true)
    private String companyName;
    private String legalPerson;
    private String status;//company,company-n
    @OneToMany(mappedBy = "company",cascade= CascadeType.MERGE,fetch= FetchType.LAZY)
    private List<User> users;
    public void addUser(User user) {
        if(this.users==null)
            this.users=new ArrayList<>();
        this.users.add(user);
    }
    public void removeUser(String userId) {
        for (int index=0; index < this.users.size(); index ++ ) {
            if (users.get(index).getId() .equals(userId)) {
                this.users.remove(index);
                break;
            }
        }
    }

}

