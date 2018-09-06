package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.dao.ApiTokenRepository;
import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ForbiddenException;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.ApiToken;
import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.RegisterDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import jp.co.worksap.stm2018.jobhere.service.impl.AuthServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AuthServiceTests {

    @Autowired
    protected MockMvc mvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApiTokenRepository apiTokenRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(authService).build();
    }

    @Test
    public void testLoginSuccess() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("candidate-tester")
                .password("candidate-tester")
                .build();
        User user = User.builder()
                .username("candidate-tester")
                .role("candidate")
                .build();
        user.setPassword("candidate-tester");
        Mockito
                .when(userRepository.findByUsername(loginDTO.getUsername()))
                .thenReturn(user);
        ApiToken apiToken = ApiToken.builder()
                .id(user.getUsername() + "stm" + user.getRole())
                .user(user)
                .build();

        Mockito
                .when(apiTokenRepository.findById(apiToken.getId()))
                .thenReturn(Optional.empty());
        try {
            ApiTokenDTO apiTokenDTO = authService.login(loginDTO);
            Assert.assertEquals(apiTokenDTO.getToken(), apiToken.getId());
        } catch (Exception e) {
            Assert.fail();
        }

        Mockito
                .when(apiTokenRepository.findById(apiToken.getId()))
                .thenReturn(Optional.of(apiToken));
        try {
            ApiTokenDTO apiTokenDTO = authService.login(loginDTO);
            Assert.assertEquals(apiTokenDTO.getToken(), apiToken.getId());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testLoginWithWrongUsername() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("candidate-tester")
                .password("candidate-tester")
                .build();
        Mockito
                .when(userRepository.findByUsername(loginDTO.getUsername()))
                .thenReturn(null);
        try {
            authService.login(loginDTO);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ForbiddenException.class);
        }
    }

    @Test
    public void testLoginWithWrongPassword() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("candidate-tester")
                .password("candidate-tester-wrong-password")
                .build();
        User user = User.builder()
                .username("candidate-tester")
                .build();
        user.setPassword("candidate-tester");
        Mockito
                .when(userRepository.findByUsername(loginDTO.getUsername()))
                .thenReturn(user);
        try {
            authService.login(loginDTO);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ForbiddenException.class);
        }
    }

    @Test
    public void testRegisterWithUsernameExisted() {
        User user = User.builder()
                .username("exist")
                .build();
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username("exist")
                .build();
        Mockito
                .when(userRepository.findByUsername("exist"))
                .thenReturn(user);
        try {
            authService.register(registerDTO);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ValidationException.class);
        }
    }

    @Test
    public void testRegisterCandidate() {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username("candidate")
                .password("candidate")
                .role("candidate")
                .build();
        User candidateUser = User.builder()
                .username("candidate")
                .role("candidate")
                .build();
        candidateUser.setPassword("candidate");
        ApiTokenDTO apiTokenDTO = ApiTokenDTO.builder()
                .token(registerDTO.getUsername() + "stm" + registerDTO.getRole())
                .build();
        Mockito
                .when(userRepository.findByUsername("candidate"))
                .thenReturn(null);
        Assert.assertEquals(authService.register(registerDTO), apiTokenDTO);
    }

    @Test
    public void testRegisterHrWithCompanyFirstRegistered() {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username("hr")
                .password("hr")
                .role("hr")
                .email("hr@works.com")
                .companyName("works")
                .legalPerson("Tom")
                .build();
        Mockito
                .when(userRepository.findByUsername("hr"))
                .thenReturn(null);
        Mockito
                .when(companyRepository.findByCompanyNameAndLegalPerson("works", "Tom"))
                .thenReturn(null);
        Assert.assertEquals(authService.register(registerDTO).getToken(), "hrstmhr");


        Company company = Company.builder()
                .companyName("works")
                .legalPerson("Tom")
                .status("company-n")
                .build();
        Mockito
                .when(companyRepository.findByCompanyNameAndLegalPerson("works", "Tom"))
                .thenReturn(company);
        Assert.assertEquals(authService.register(registerDTO).getToken(), "hrstmhr");
    }

    @Test
    public void testRegisterHrWithCompanyVerified() {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username("hr")
                .password("hr")
                .role("hr")
                .email("hr@works.com")
                .companyName("works")
                .legalPerson("Tom")
                .build();
        Company company = Company.builder()
                .companyName("works")
                .legalPerson("Tom")
                .status("company")
                .build();
        Mockito
                .when(userRepository.findByUsername("hr"))
                .thenReturn(null);
        Mockito
                .when(companyRepository.findByCompanyNameAndLegalPerson("works", "Tom"))
                .thenReturn(company);
        Assert.assertEquals(authService.register(registerDTO).getToken(), "hrstmhr");
    }

    @Test
    public void testGetUserByToken() {
        Mockito
                .when(apiTokenRepository.findById("token-not-exists"))
                .thenReturn(Optional.empty());
        Assert.assertNull(authService.getUserByToken("token-not-exists"));

        User testUser = User.builder()
                .id("001")
                .username("test")
                .build();
        ApiToken apiToken = ApiToken.builder()
                .id("token-test")
                .user(testUser)
                .build();
        Mockito
                .when(apiTokenRepository.findById("token-test"))
                .thenReturn(Optional.of(apiToken));
        Assert.assertEquals(authService.getUserByToken("token-test"), testUser);
    }

    @Test
    public void testFindById() {
        Mockito
                .when(apiTokenRepository.findById("token-not-exists"))
                .thenReturn(Optional.empty());
        Optional<ApiToken> token = authService.findById("token-not-exists");
        Assert.assertFalse(token.isPresent());

        Mockito
                .when(apiTokenRepository.findById("token"))
                .thenReturn(Optional.of(ApiToken.builder().id("token").build()));
        token = authService.findById("token");
        Assert.assertTrue(token.isPresent());
        Assert.assertEquals(token.get().getId(), "token");
    }
}
