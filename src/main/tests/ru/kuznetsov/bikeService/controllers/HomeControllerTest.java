package ru.kuznetsov.bikeService.controllers;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.kuznetsov.bikeService.Starter;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;

import java.security.Principal;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Starter.class)
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private HomeController homeController;
    @MockBean
    private UserService userService;
    @MockBean
    private PictureService pictureService;
    @MockBean
    private Principal principal;
    private Logger logger;

    @BeforeEach
    void StartUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.homeController.setUserService(this.userService);
        this.homeController.setPictureDao(this.pictureService);
        this.logger = AbstractController.logger;
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("homeController"));
    }

    @Test
    public void homePageTest() throws Exception {
        this.mockMvc.perform(get("/home")).andDo(print())
                .andExpect(view().name("home"));
    }

    @Test
    public void logInPageTest() throws Exception {
        this.mockMvc.perform(get("/login").principal(this.principal)).andDo(print())
                .andExpect(view().name("title"));
    }

    @Test
    public void homeWhenEmptyPageTest() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(view().name("home"));
    }

    @Test
    public void titlePageTest() throws Exception {
        this.mockMvc.perform(get("/title")).andDo(print())
                .andExpect(view().name("title"));
    }

    @Test
    public void registrationGETPageTest() throws Exception {
        this.mockMvc.perform(get("/registration")).andDo(print())
                .andExpect(view().name("registration"));
    }

//    @Test
//    void logInTest() throws Exception {
//        this.mockMvc.perform(get("/title"))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("http://localhost:8080/login"));
//    }

    @Test
    public void registrationPOSTPageTest() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setUsername("TestName");
        this.mockMvc.perform(post("/registration")
                        .param("username", "Pavel")
                        .param("password", "1999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("is occupied")));

//        MvcResult mvcResult = this.mockMvc.perform(get("/registration"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect()
    }
}