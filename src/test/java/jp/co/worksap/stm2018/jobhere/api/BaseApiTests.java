package jp.co.worksap.stm2018.jobhere.api;

import jp.co.worksap.stm2018.jobhere.dao.ApiTokenRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.model.domain.ApiToken;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Extend this base class
 *
 * mock 3 users with different roles(admin, candidate, hr) before each test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Ignore
public class BaseApiTests {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ApiTokenRepository apiTokenRepository;

    @Before
    public void setup() {
        User adminUser = User.builder()
                .id("user-admin")
                .username("user-admin")
                .role("admin")
                .build();
        adminUser.setPassword("pwd-admin");
        User candidateUser = User.builder()
                .id("user-candidate")
                .username("user-candidate")
                .role("candidate")
                .build();
        candidateUser.setPassword("pwd-candidate");
        User hrUser = User.builder()
                .id("user-hr")
                .username("user-hr")
                .role("hr")
                .build();
        hrUser.setPassword("pwd-hr");
        Mockito
                .when(userRepository.findById(adminUser.getId()))
                .thenReturn(Optional.of(adminUser));
        Mockito
                .when(userRepository.findById(candidateUser.getId()))
                .thenReturn(Optional.of(candidateUser));
        Mockito
                .when(userRepository.findById(hrUser.getId()))
                .thenReturn(Optional.of(hrUser));
        Mockito
                .when(userRepository.findByUsername(adminUser.getUsername()))
                .thenReturn(adminUser);
        Mockito
                .when(userRepository.findByUsername(candidateUser.getUsername()))
                .thenReturn(candidateUser);
        Mockito
                .when(userRepository.findByUsername(hrUser.getUsername()))
                .thenReturn(hrUser);

        ApiToken adminToken = ApiToken.builder()
                .id("token-admin")
                .user(adminUser)
                .build();
        ApiToken candidateToken = ApiToken.builder()
                .id("token-candidate")
                .user(candidateUser)
                .build();
        ApiToken hrToken = ApiToken.builder()
                .id("token-hr")
                .user(hrUser)
                .build();
        Mockito
                .when(apiTokenRepository.findById(adminToken.getId()))
                .thenReturn(Optional.of(adminToken));
        Mockito
                .when(apiTokenRepository.findById(candidateToken.getId()))
                .thenReturn(Optional.of(candidateToken));
        Mockito
                .when(apiTokenRepository.findById(hrToken.getId()))
                .thenReturn(Optional.of(hrToken));
    }
}
