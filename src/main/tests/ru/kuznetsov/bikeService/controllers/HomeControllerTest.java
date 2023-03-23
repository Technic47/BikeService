package ru.kuznetsov.bikeService.controllers;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
//    @MockBean
//    private Principal principal;
    private Logger logger;

    @BeforeEach
    void StartUp() {
        this.logger = AbstractController.logger;
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesHomeController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("homeController"));
    }

    @Test
    void homeGETPageTest() throws Exception {
        this.mockMvc.perform(get("/home")).andDo(print())
                .andExpect(view().name("home"));
    }

    @Test
    void homeWhenEmptyGETPageTest() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(view().name("home"));
    }

    @Test
    public void registrationGETPageTest() throws Exception {
        this.mockMvc.perform(get("/registration")).andDo(print())
                .andExpect(view().name("registration"));
    }

    @Test
    void logInGETTest() throws Exception {
        this.mockMvc.perform(get("/title"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("pavel")
    public void titleGETPageTest() throws Exception {
        this.mockMvc.perform(get("/title"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//header/h1/div").string("pavel"));
    }

    @Test
    public void logInGETPageTest() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please sign in")))
                .andExpect(content().string(containsString("Username")))
                .andExpect(content().string(containsString("Password")));
    }

    @Test
    @WithUserDetails("pavel")
    public void logInWithCredentialsGETPageTest() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(view().name("title"));
    }

    @Test
    void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("test").password("test"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void badCredentialsTest() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("username", "123")
                        .param("password", "321")
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

//    @Test
//    void registrationPOSTPageTest() throws Exception {
//        UserModel userModel = new UserModel();
//        userModel.setUsername("TestName");
//        this.mockMvc.perform(post("/registration")
//                        .param("username", "pavel")
//                        .param("password", "1999"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("is occupied")));

//        MvcResult mvcResult = this.mockMvc.perform(get("/registration"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect()
//    }
}