package jp.co.worksap.stm2018.jobhere.api;

import com.google.gson.Gson;
import jp.co.worksap.stm2018.jobhere.model.dto.request.LoginDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.RegisterDTO;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthApiTests extends BaseApiTests {

    @Test
    public void unauthorized() throws Exception {
        mvc
                .perform(get("/auth").header("Api-Token", "none-existed-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authorized() throws Exception {
        mvc
                .perform(get("/auth").header("Api-Token", "token-admin"))
                .andExpect(status().isOk());
    }

    @Test
    public void loginSuccess() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("user-admin")
                .password("pwd-admin")
                .build();
        mvc
                .perform(
                        post("/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(loginDTO))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void loginFailed() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("admin")
                .password("wrong-password")
                .build();
        mvc
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
        mvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(registerDTO))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void registerWithExistedUsername() throws Exception {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username("user-admin")
                .password("test")
                .role("candidate")
                .build();
        mvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(registerDTO))
                )
                .andExpect(status().isUnprocessableEntity());
    }
}
