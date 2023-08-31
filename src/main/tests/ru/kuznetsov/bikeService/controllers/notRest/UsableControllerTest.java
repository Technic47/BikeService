package ru.kuznetsov.bikeService.controllers.notRest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
class UsableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void showOwn() throws Exception {
        this.mockMvc.perform(get("/tools/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("show"))
                .andExpect(model().attribute("manufacture", "testManufacture"));
    }

    @Test
    void pdfTest() throws Exception {
        this.mockMvc.perform(get("/tools/1"));
        this.mockMvc.perform(get("/tools/pdf")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }
}