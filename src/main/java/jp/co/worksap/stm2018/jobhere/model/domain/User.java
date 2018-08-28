package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    private String username;

    /**
     * password
     * - bcrypt(sha1(origin))
     * - front-end will encrypt the `sha1` part
     */
    private String password;

    /**
     * admin,hr,candidate
     */
    private String role;
}
