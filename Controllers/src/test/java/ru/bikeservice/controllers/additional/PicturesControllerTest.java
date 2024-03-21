package ru.bikeservice.controllers.additional;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.controllers.TestCredentials.getDefaultMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("pavel")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PicturesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/pictures"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("allPictures", hasSize(3)))
                .andExpect(view().name("picture_index"))
                .andExpect(content().string(containsString("src=\"/preview/testImage.jpg")))
                .andExpect(content().string(containsString("src=\"/preview/test2")))
                .andExpect(content().string(containsString("src=\"/preview/test3")));
    }

    @Test
    void newPicture() throws Exception {
        this.mockMvc.perform(get("/pictures/new"))
                .andDo(print())
                .andExpect(view().name("picture_new"));
    }


    @Test
    void uploadImage() throws Exception {
        this.mockMvc.perform(multipart("/pictures/upload")
                        .file(getDefaultMultipartFile()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pictures"));
        this.mockMvc.perform(get("/pictures"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("allPictures", hasSize(4)))
                .andExpect(xpath("//div/div/ul/li/form/img[@src='/preview/testImage.jpg']").exists());
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(post("/pictures/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pictures"));
        this.mockMvc.perform(get("/pictures"))
                .andDo(print())
                .andExpect(model().attribute("allPictures", hasSize(2)))
                .andExpect(xpath("//div/form/img[@src='/preview/test']").doesNotExist());
    }
}