package ru.kuznetsov.bikeService.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.hasSize;
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
class BasicControllerRESTTest {
    private final ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/api/documents"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void create() throws Exception {
        Long pictureId = TEST_DOCUMENT.getPicture();
        mockMvc.perform(post("/api/documents")
                        .content(om.writeValueAsString(createDtoFrom(TEST_DOCUMENT)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)))
                .andExpect(jsonPath("$.description", is(TEST_DESCRIPTION)))
                .andExpect(jsonPath("$.picture", is(pictureId.intValue())))
                .andExpect(jsonPath("$.link", is(TEST_LINK)))
                .andExpect(jsonPath("$.value", is(TEST_LINK)))
                .andExpect(jsonPath("$.manufacturer").doesNotExist())
                .andExpect(jsonPath("$.model").doesNotExist());
    }

    @Test
    void createErrors() throws Exception {
        mockMvc.perform(post("/api/documents"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShow() throws Exception {
        mockMvc.perform(get("/api/documents/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("testDoc1")))
                .andExpect(jsonPath("$.description", is("testDescription")))
                .andExpect(jsonPath("$.picture", is(1)))
                .andExpect(jsonPath("$.link", is("testLink")))
                .andExpect(jsonPath("$.value", is("testLink")))
                .andExpect(jsonPath("$.manufacturer").doesNotExist())
                .andExpect(jsonPath("$.model").doesNotExist());
    }

    @Test
    void testShowError() throws Exception {
        //Unreal id
        mockMvc.perform(get("/api/documents/111"))
                .andExpect(status().isNotFound());

        //Try to get not own not shared item.
        mockMvc.perform(get("/api/documents/3"))
                .andExpect(status().isForbidden());
    }


    @Test
    void testUpdate() throws Exception {
        Long pictureId = TEST_DOCUMENT.getPicture();
        mockMvc.perform(put("/api/documents/1")
                        .content(om.writeValueAsString(createDtoFrom(TEST_DOCUMENT)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)))
                .andExpect(jsonPath("$.description", is(TEST_DESCRIPTION)))
                .andExpect(jsonPath("$.picture", is(pictureId.intValue())))
                .andExpect(jsonPath("$.link", is(TEST_LINK)))
                .andExpect(jsonPath("$.value", is(TEST_LINK)))
                .andExpect(jsonPath("$.manufacturer").doesNotExist())
                .andExpect(jsonPath("$.model").doesNotExist());
    }

    @Test
    void testUpdateError() throws Exception {
        //Unreal id
        mockMvc.perform(put("/api/documents/111")
                        .content(om.writeValueAsString(createDtoFrom(TEST_DOCUMENT)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //Try to update not own not shared item.
        mockMvc.perform(put("/api/documents/3")
                        .content(om.writeValueAsString(createDtoFrom(TEST_DOCUMENT)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/documents/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.deleted", is("ok")));

        mockMvc.perform(get("/api/documents"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testDeleteError() throws Exception {
        //Unreal id
        mockMvc.perform(delete("/api/documents/111"))
                .andExpect(status().isNotFound());

        //Try to update not own not shared item.
        mockMvc.perform(delete("/api/documents/3"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createPdf() throws Exception {
        mockMvc.perform(get("/api/documents/1/pdf"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    void createPdfErrors() throws Exception {
        //Unreal id
        mockMvc.perform(get("/api/documents/111/pdf"))
                .andExpect(status().isNotFound());

        //Try to update not own not shared item.
        mockMvc.perform(get("/api/documents/3/pdf"))
                .andExpect(status().isForbidden());
    }
}