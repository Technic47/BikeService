package ru.kuznetsov.bikeService.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
class ServiceableControllerRESTTest {
    private final ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addItemToItemList() throws Exception {
        mockMvc.perform(put("/api/parts/1/linkedItems/2")
                .param("type", "tool")
                .param("amount", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.linkedItems", hasSize(6)))
                .andExpect(jsonPath("$.linkedItems[0].itemId", is(1)))
                .andExpect(jsonPath("$.linkedItems[0].type", is("Document")))
                .andExpect(jsonPath("$.linkedItems[0].amount", is(1)));
    }

    @Test
    void addItemToItemListErrors() throws Exception {
        //Unreal id
        mockMvc.perform(put("/api/parts/111/linkedItems/2")
                        .param("type", "tool")
                        .param("amount", "1"))
                .andExpect(status().isNotFound());

        //No request params
        mockMvc.perform(put("/api/parts/1/linkedItems/2"))
                .andExpect(status().isBadRequest());

        //Try to update not own not shared item.
        mockMvc.perform(put("/api/parts/3/linkedItems/2")
                        .param("type", "tool")
                        .param("amount", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void delItemFromItemList() throws Exception {
        mockMvc.perform(delete("/api/parts/1/linkedItems/1")
                        .param("type", "Fastener")
                        .param("amount", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.linkedItems", hasSize(4)));
    }

    @Test
    void delItemFromItemListErrors() throws Exception {
        //Unreal id
        mockMvc.perform(delete("/api/parts/111/linkedItems/2")
                        .param("type", "tool")
                        .param("amount", "1"))
                .andExpect(status().isNotFound());

        //No request params
        mockMvc.perform(delete("/api/parts/1/linkedItems/2"))
                .andExpect(status().isBadRequest());

        //Try to update not own not shared item.
        mockMvc.perform(delete("/api/parts/3/linkedItems/2")
                        .param("type", "tool")
                        .param("amount", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createPdfAll() throws Exception {
        mockMvc.perform(get("/api/parts/1/pdfAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    void createPdfErrors() throws Exception {
        //Unreal id
        mockMvc.perform(get("/api/parts/111/pdfAll"))
                .andExpect(status().isNotFound());

        //Try to update not own not shared item.
        mockMvc.perform(get("/api/parts/3/pdfAll"))
                .andExpect(status().isForbidden());
    }
}