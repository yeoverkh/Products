package yehor.ua.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import yehor.ua.products.dto.CredentialsDto;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthenticationControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testUserRegistrationAndAuthentication() throws Exception {
        CredentialsDto newUserCredentials = new CredentialsDto("testUser", "testPassword");
        String userJson = new ObjectMapper().writeValueAsString(newUserCredentials);

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        CredentialsDto invalidCredentials = new CredentialsDto("testUser", "wrongPassword");
        String invalidCredentialsJson = new ObjectMapper().writeValueAsString(invalidCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCredentialsJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testAbsentUsernameMustBeBadRequest() throws Exception {
        String invalidJson = "{ \"username\": \"testUsername\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testRegisterWithInvalidJson() throws Exception {
        String invalidJson = "{ \"username\": \"testUsername\", }";

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testAuthenticateWithNotExistedUsername() throws Exception {
        CredentialsDto notExistedUsername = new CredentialsDto("testNotExistedUser", "testPassword");
        String notExistedUserJson = new ObjectMapper().writeValueAsString(notExistedUsername);

        mockMvc.perform(post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notExistedUserJson))
                .andExpect(status().isNotFound());
    }
}