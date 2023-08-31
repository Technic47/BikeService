package ru.kuznetsov.bikeService.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;
import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.createDtoFrom;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
class UsableControllerRESTTest {
    private final ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void create() throws Exception {
        TEST_TOOL.setManufacturer(1L);
        mockMvc.perform(post("/api/tools")
                        .content(om.writeValueAsString(createDtoFrom(TEST_TOOL)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)))
                .andExpect(jsonPath("$.description", is(TEST_DESCRIPTION)))
                .andExpect(jsonPath("$.picture", is(13)))
                .andExpect(jsonPath("$.link", is(TEST_LINK)))
                .andExpect(jsonPath("$.value", is(TEST_VALUE)))
                .andExpect(jsonPath("$.manufacturer", is(1)))
                .andExpect(jsonPath("$.model", is(TEST_MODEL)));
    }

    @Test
    void createErrors() throws Exception {
        mockMvc.perform(post("/api/tools"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        TEST_TOOL.setManufacturer(1L);
        mockMvc.perform(put("/api/tools/1")
                        .content(om.writeValueAsString(createDtoFrom(TEST_TOOL)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)))
                .andExpect(jsonPath("$.description", is(TEST_DESCRIPTION)))
                .andExpect(jsonPath("$.picture", is(13)))
                .andExpect(jsonPath("$.link", is(TEST_LINK)))
                .andExpect(jsonPath("$.value", is(TEST_VALUE)))
                .andExpect(jsonPath("$.manufacturer", is(1)))
                .andExpect(jsonPath("$.model", is(TEST_MODEL)));
    }

    @Test
    void testUpdateError() throws Exception {
        //Unreal id
        mockMvc.perform(put("/api/tools/111")
                        .content(om.writeValueAsString(createDtoFrom(TEST_TOOL)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //Try to update not own not shared item.
        mockMvc.perform(put("/api/tools/2")
                        .content(om.writeValueAsString(createDtoFrom(TEST_TOOL)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void createPdf() throws Exception {
        mockMvc.perform(get("/api/tools/1/pdf"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    void createPdfErrors() throws Exception {
        //Unreal id
        mockMvc.perform(get("/api/tools/111/pdf"))
                .andExpect(status().isNotFound());

        //Try to update not own not shared item.
        mockMvc.perform(get("/api/tools/2/pdf"))
                .andExpect(status().isForbidden());
    }
}