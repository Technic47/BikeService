package ru.kuznetsov.bikeService.controllers.pictures;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.controllers.pictures.PictureWorkTest.PATH_WIDE_FILE;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("test")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/SQL_scripts/create-images-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/create-images-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PicturesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/pictures"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attribute("allPictures", hasSize(3)))
                .andExpect(view().name("pictures/index"));
    }

    @Test
    void newPicture() throws Exception {
        this.mockMvc.perform(get("/pictures/new"))
                .andDo(print())
                .andExpect(view().name("pictures/new"));
    }

    private MockMultipartFile getMultipartFile() {
        MockMultipartFile multipartFile = null;
        try {
            File initialFile = new File(PATH_WIDE_FILE);
            InputStream targetStream1 = new FileInputStream(initialFile);
            multipartFile = new MockMultipartFile("newImage", initialFile.getName(), "image/jpeg", targetStream1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return multipartFile;
    }

    @Test
    void uploadImage() throws Exception {
        this.mockMvc.perform(multipart("/pictures/upload")
                        .file(this.getMultipartFile()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pictures"));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(post("/pictures/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pictures"));
    }

    @Test
    void setPictureDAO() {
    }
}