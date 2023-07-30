package ru.kuznetsov.bikeService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.services.modelServices.DocumentService;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.TestCredentials.TEST_DOCUMENT;
import static ru.kuznetsov.bikeService.TestCredentials.getDefaultMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("pavel")
public class BasicControllerADMINTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DocumentService documentService;
    private Document testDocFromDb;

    @BeforeEach
    void setUp() {
        this.testDocFromDb = this.documentService.show(1L);
    }

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/documents"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("objects", aMapWithSize(3)))
                .andExpect(model().attribute("sharedObjects", aMapWithSize(2)))
                .andExpect(xpath("//div/div/table/tbody/tr").nodeCount(5))
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/1']").exists())
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/2']").exists())
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/3']").exists())
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/4']").exists())
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/5']").exists());
    }

    @Test
    void showNotOwn() throws Exception {
        this.mockMvc.perform(get("/documents/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("show"))
                .andExpect(xpath("//div/div/div/ul/li").nodeCount(9))
                .andExpect(content().string(containsString("ID - ")))
                .andExpect(content().string(containsString("IsShared - false")))
                .andExpect(content().string(containsString("Name - ")))
                .andExpect(content().string(containsString("Description - ")))
                .andExpect(content().string(containsString("Link - ")))
                .andExpect(content().string(containsString("ValueName - ")))
                .andExpect(content().string(containsString("Value - ")))
                .andExpect(content().string(containsString("Creator`s ID - ")))
                .andExpect(content().string(containsString("Picture`s ID - ")));
    }


    @Test
    void createNoErrors() throws Exception {
        this.mockMvc.perform(multipart("/documents")
                        .file(getDefaultMultipartFile())
                        .flashAttr("object", TEST_DOCUMENT))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/documents"));

        this.mockMvc.perform(get("/documents"))
                .andDo(print())
                .andExpect(model().attribute("objects", aMapWithSize(4)))
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/6']").exists());
    }
}
