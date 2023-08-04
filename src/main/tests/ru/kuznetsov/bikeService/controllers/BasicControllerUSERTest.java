package ru.kuznetsov.bikeService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;
import ru.kuznetsov.bikeService.services.modelServices.DocumentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.TestCredentials.TEST_DOCUMENT;
import static ru.kuznetsov.bikeService.TestCredentials.getDefaultMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
class BasicControllerUSERTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private UserService userService;
    private Document testDocFromDb;
    @Mock
    private Document emptyDocument;

    @BeforeEach
    void setUp() {
        this.testDocFromDb = this.documentService.show(1L);
        userService.addCreatedItem(userService.findByUsername("test"),
                new UserEntity("Document", 1L));
        userService.addCreatedItem(userService.findByUsername("test"),
                new UserEntity("Document", 2L));
        userService.addCreatedItem(userService.findByUsername("pavel"),
                new UserEntity("Document", 3L));
        userService.addCreatedItem(userService.findByUsername("pavel"),
                new UserEntity("Document", 4L));
        userService.addCreatedItem(userService.findByUsername("pavel"),
                new UserEntity("Document", 5L));
    }

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/documents"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("objects", aMapWithSize(2)))
                .andExpect(model().attribute("category", "documents"))
                .andExpect(xpath("//div/div/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/1']").exists())
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/2']").exists());
    }

    @Test
    void showOwn() throws Exception {
        this.mockMvc.perform(get("/documents/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("show"))
                .andExpect(model().attribute("access", true))
                .andExpect(model().attribute("category", "documents"))
                .andExpect(model().attribute("object", this.testDocFromDb))
                .andExpect(model().attribute("type", "Showable"))
                .andExpect(content().string(containsString("src=\"/IMG/testImage.jpg\"")))
                .andExpect(content().string(containsString("testDoc1")))
                .andExpect(content().string(containsString("testLink")))
                .andExpect(content().string(containsString("href=\"testLink\"")));
    }

    @Test
    void showWrong() throws Exception {
        this.mockMvc.perform(get("/documents/3"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("show"))
                .andExpect(model().attribute("access", false));
    }

    @Test
    void newItem() throws Exception {
        this.mockMvc.perform(get("/documents/new"))
                .andDo(print())
                .andExpect(view().name("new"))
                .andExpect(model().attribute("allPictures", hasSize(3)))
                .andExpect(model().attribute("category", "documents"))
                .andExpect(model().attribute("object", new Document()))
                .andExpect(model().attribute("type", "Showable"))
                .andExpect(xpath("//header/h1/div").string("test"))
                .andExpect(xpath("//div/div/form[@action='/documents']").exists());
    }

    @Test
    void createWithErrors() throws Exception {
        this.emptyDocument.setName("");

        this.mockMvc.perform(multipart("/documents")
                        .file(getDefaultMultipartFile())
                        .flashAttr("object", this.emptyDocument))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("object"))
                .andExpect(view().name("new"));
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
                .andExpect(model().attribute("objects", aMapWithSize(3)))
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/6']").exists());
    }

    @Test
    void editOwn() throws Exception {
        this.mockMvc.perform(get("/documents/1/edit"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("picture", pictureService.show(this.testDocFromDb.getPicture())))
                .andExpect(model().attribute("object", this.testDocFromDb))
                .andExpect(view().name("edit"))
                .andExpect(xpath("//div/div/form[@action='/documents/1/edit']").exists());
    }

    @Test
    void editWrong() throws Exception {
        this.mockMvc.perform(get("/documents/3"));
        this.mockMvc.perform(get("/documents/3/edit"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/documents/3"));
    }

    @Test
    void updateWithErrorsOwn() throws Exception {
        this.mockMvc.perform(get("/documents/1"));

        this.testDocFromDb.setName("");

        this.mockMvc.perform(multipart("/documents/1/edit")
                        .file(getDefaultMultipartFile())
                        .flashAttr("object", this.testDocFromDb))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("object"))
                .andExpect(view().name("edit"));
    }

    @Test
    void updateNoErrorsOwn() throws Exception {
        this.mockMvc.perform(get("/documents/1"));

        this.testDocFromDb.setName("123654");

        this.mockMvc.perform(multipart("/documents/1/edit")
                        .file(getDefaultMultipartFile())
                        .flashAttr("object", this.testDocFromDb))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/documents"));

        this.mockMvc.perform(get("/documents/1"))
                .andExpect(xpath("//div/div/div/div/h1").string("123654"));

//        this.mockMvc.perform(get("/pictures"))
//                .andExpect(model().attribute("allPictures", hasSize(4)))
//                .andExpect(content().string(containsString("src=\"/preview/testImage.jpg\"")));
//                .andExpect(xpath("//div/div/ul/li/form/img[@src='/preview/testImage.jpg']").exists());
    }

    @Test
    void updateNoErrorsWrong() throws Exception {
        this.mockMvc.perform(get("/documents/3"));
        this.mockMvc.perform(multipart("/documents/3/edit")
                        .file(getDefaultMultipartFile())
                        .flashAttr("object", this.testDocFromDb))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/documents/3"));
    }

    @Test
    void deleteOwn() throws Exception {
        this.mockMvc.perform(get("/documents/1"));

        this.mockMvc.perform(post("/documents/1"));

        this.mockMvc.perform(get("/documents"))
                .andExpect(model().attribute("objects", aMapWithSize(1)))
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/1']").doesNotExist());

        UserEntity entity = new UserEntity("Document", 1L);
        List<UserEntity> entityList = this.userService.findByUsername("test").getCreatedItems();
        assertEquals(1, entityList.size());
        assertThat(entityList).doesNotContain(entity);
    }

    @Test
    void deleteWrong() throws Exception {
        this.mockMvc.perform(get("/documents/3"));

        this.mockMvc.perform(post("/documents/3"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/documents/3"));
    }

    @Test
    void searchTest() throws Exception {
        this.mockMvc.perform(get("/documents/search")
                        .param("value", "doc2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("objects", aMapWithSize(1)));

        this.mockMvc.perform(get("/documents/search")
                        .param("value", "tde"))
                .andExpect(model().attribute("objects", aMapWithSize(1)));
    }

    @Test
    void pdfTest() throws Exception {
        this.mockMvc.perform(get("/parts/1"));
        this.mockMvc.perform(get("/parts/pdf")
                .param("id", "1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

    }
}