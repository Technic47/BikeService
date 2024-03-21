package ru.bikeservice.controllers.additional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.kuznetsov.bikeService.models.users.UserModel;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
public class UserControllerNotAdminTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void show() throws Exception {
        this.mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void showToOwner() throws Exception {
        this.mockMvc.perform(get("/users/show"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("owner", true))
                .andExpect(model().attribute("userAccount", any(UserModel.class)))
                .andExpect(model().attribute("documents", hasSize(2)))
                .andExpect(model().attribute("fasteners", hasSize(1)))
                .andExpect(model().attribute("manufacturers", hasSize(1)))
                .andExpect(model().attribute("consumables", hasSize(1)))
                .andExpect(model().attribute("tools", hasSize(1)))
                .andExpect(model().attribute("parts", hasSize(2)))
                .andExpect(model().attribute("bikes", hasSize(0)));
    }

    @Test
    void updateNameOrPasswordTest() throws Exception {
        this.mockMvc.perform(get("/users/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("credentialsChange"))
                .andExpect(model().attribute("user", any(UserModel.class)));
    }

    @Test
    void uploadNewCredentialsTest() throws Exception {
        UserModel userModel = new UserModel("TestTest", "EmailEmail", "PassPass");
        this.mockMvc.perform(post("/users/update/credentialsChange")
                .flashAttr("user", userModel))
                .andDo(print())
                .andExpect(redirectedUrl("/logout"));
    }

    @Test
    void update() throws Exception {
        this.mockMvc.perform(post("/users/update/1")
                        .param("admin", String.valueOf(1)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(post("/users/delete/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void search() throws Exception{
        this.mockMvc.perform(get("/users/search"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
