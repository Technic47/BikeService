package ru.kuznetsov.bikeService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.services.DocumentService;
import ru.kuznetsov.bikeService.services.PictureService;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.TestCridentials.TEST_DOCUMENT;
import static ru.kuznetsov.bikeService.TestCridentials.getMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test")
class BasicControllerUSERTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PictureService pictureService;
    private Document testDocFromDb;
    @Mock
    private Document emptyDocument;

    @BeforeEach
    void setUp(){
        this.testDocFromDb = this.documentService.show(1L);

    }

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/documents"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("/show/index"))
                .andExpect(model().attribute("objects", aMapWithSize(2)))
                .andExpect(model().attribute("category", "documents"))
                .andExpect(xpath("//div/div/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/1']").exists())
                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/2']").exists());
    }

    /*
    Works while started in single, not works with other tests(((
     */
//    @Test
//    @WithUserDetails("pavel")
//    void indexADMIN() throws Exception {
//        this.mockMvc.perform(get("/documents"))
//                .andDo(print())
//                .andExpect(authenticated())
//                .andExpect(status().isOk())
//                .andExpect(view().name("/show/index"))
//                .andExpect(model().attribute("objects", aMapWithSize(5)))
//                .andExpect(model().attribute("category", "documents"))
//                .andExpect(xpath("//div/div/table/tbody/tr").nodeCount(5))
//                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/1']").exists())
//                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/2']").exists())
//                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/3']").exists())
//                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/4']").exists())
//                .andExpect(xpath("//div/div/table/tbody/tr/td/a[@href='/documents/5']").exists());
//    }

    @Test
    void show() throws Exception {
        this.mockMvc.perform(get("/documents/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("/show/show"))
                .andExpect(model().attribute("category", "documents"))
                .andExpect(model().attribute("object", this.testDocFromDb))
                .andExpect(model().attribute("type", "Showable"))
                .andExpect(xpath("//div/div/div/div/img[@src='/IMG/test']").exists())
                .andExpect(xpath("//div/div/div/div/h1").string("testDoc1"))
                .andExpect(xpath("//div/div/div/div/a").string("testLink"))
                .andExpect(xpath("//div/div/div/div/a[@href='testLink']").exists())
                .andExpect(xpath("//div/div/div/div/p").string("Ссылка - testLink"))
                .andExpect(xpath("//div/div/div/ul").doesNotExist());
    }

    @Test
    void newItem() throws Exception {
        this.mockMvc.perform(get("/documents/new"))
                .andDo(print())
                .andExpect(view().name("/new/new"))
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
                        .file(getMultipartFile())
                        .flashAttr("object", this.emptyDocument))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("object"))
                .andExpect(view().name("/new/new"));
    }

    @Test
    void create() throws Exception {
        this.mockMvc.perform(multipart("/documents")
                        .file(getMultipartFile())
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
    void edit() throws Exception {
        this.mockMvc.perform(get("/documents/1/edit"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("picture", pictureService.show(this.testDocFromDb.getPicture())))
                .andExpect(model().attribute("object", this.testDocFromDb))
                .andExpect(view().name("/edit/edit"))
                .andExpect(xpath("//div/div/form[@action='/documents/1/edit']").exists());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}