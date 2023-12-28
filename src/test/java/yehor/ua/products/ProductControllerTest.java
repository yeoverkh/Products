package yehor.ua.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import yehor.ua.products.dto.CredentialsDto;
import yehor.ua.products.models.UserEntity;
import yehor.ua.products.services.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void testProductEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/products/all"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/products/add")
                        .content("{ \"testJson\": \"testJson\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testProductEndpointsWithAuthentication() throws Exception {
        CredentialsDto newUserCredentials = new CredentialsDto("productTestUser", "productTestPassword");
        String userJson = new ObjectMapper().writeValueAsString(newUserCredentials);

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        UserDetails userDetails = new UserEntity("productTestUser", "productTestPassword");
        String token = jwtService.generateToken(userDetails);

        mockMvc.perform(get("/products/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        String recordsJson = """
                {
                    "records": [
                        {
                            "entryDate": "03-01-2023",
                            "itemCode": "111111",
                            "itemName": "Test Inventory 1",
                            "itemQuantity": "20",
                            "status": "Paid"
                        },
                        {
                            "entryDate": "03-01-2023",
                            "itemCode": "111111",
                            "itemName": "Test Inventory 2",
                            "itemQuantity": "20",
                            "status": "Paid"
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/products/add")
                        .header("Authorization", "Bearer " + token)
                        .content(recordsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray());
    }
}