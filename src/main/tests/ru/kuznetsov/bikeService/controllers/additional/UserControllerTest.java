package ru.kuznetsov.bikeService.controllers.additional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("pavel")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("users_index"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(xpath("//div/div/table/tbody/tr").nodeCount(2))
                .andExpect(content().string(containsString("pavel")))
                .andExpect(content().string(containsString("test")));
    }

    @Test
    void show() throws Exception {
        this.mockMvc.perform(get("/users/2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("users_show"))
                .andExpect(content().string(containsString("pavel")))
                .andExpect(content().string(containsString("ID - 2")))
                .andExpect(content().string(containsString("ROLE_ADMIN")));
        this.mockMvc.perform(get("/users/1"))
                .andExpect(content().string(containsString("ROLE_USER")));
    }

    @Test
    void update() throws Exception {
        this.mockMvc.perform(post("/users/update/1")
                        .param("admin", String.valueOf(1)))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/users/1"))
                .andExpect(content().string(containsString("ROLE_ADMIN")));

        this.mockMvc.perform(post("/users/update/1")
                .param("admin", String.valueOf(0)));

        this.mockMvc.perform(get("/users/1"))
                .andExpect(content().string(not(containsString("ROLE_ADMIN"))));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(post("/users/delete/1"));

        this.mockMvc.perform(get("/users"))
                .andExpect(model().attribute("users", hasSize(1)))
                .andExpect(xpath("//div/div/table/tbody/tr").nodeCount(1))
                .andExpect(content().string(containsString("pavel")))
                .andExpect(content().string(not(containsString("test"))));
    }
}