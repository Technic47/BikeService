package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
class UserControllerRESTTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username", is("test")))
                .andExpect(jsonPath("$.email", is("test")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.enabled", is(true)))
                .andExpect(jsonPath("$.authorities", hasItem("ROLE_USER")));
    }

    @Test
    void getCreatedItems() throws Exception {
        mockMvc.perform(get("/api/users/created"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.documents", hasSize(2)))
                .andExpect(jsonPath("$.fasteners", hasSize(1)))
                .andExpect(jsonPath("$.manufacturers", hasSize(1)))
                .andExpect(jsonPath("$.consumables", hasSize(1)))
                .andExpect(jsonPath("$.tools", hasSize(1)))
                .andExpect(jsonPath("$.parts", hasSize(2)))
                .andExpect(jsonPath("$.bikes", hasSize(0)));
    }
}