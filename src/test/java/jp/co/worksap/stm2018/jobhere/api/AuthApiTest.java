package jp.co.worksap.stm2018.jobhere.api;

import com.google.gson.Gson;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.RegisterDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.ApiTokenDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void unauthorized() throws Exception {
        mockMvc
                .perform(get("/auth").header("Api-Token", "xxx"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginSuccess() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("admin")
                .password("123456")
                .build();
        String responseBody = mockMvc
                .perform(
                        post("/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(loginDTO))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String apiToken = new Gson().fromJson(responseBody, ApiTokenDTO.class).getToken();
        mockMvc
                .perform(
                        get("/auth")
                                .header("Api-Token", apiToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void loginFailed() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("admin")
                .password("123456111")
                .build();
        mockMvc
                .perform(
                        post("/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(loginDTO))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void register() throws Exception {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username("test-register")
                .password("test")
                .companyName("WAP")
                .email("wap@worksap.com")
                .legalPerson("lp")
                .role("hr")
                .build();
        mockMvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(registerDTO))
                )
                .andExpect(status().isOk());
    }
}
