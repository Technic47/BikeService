package ru.kuznetsov.bikeService.controllers.notRest.additional;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kuznetsov.bikeService.TestCredentials.TEST_NAME;
import static ru.kuznetsov.bikeService.TestCredentials.TEST_PASS;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void givenWac_whenServletContext_thenItProvidesHomeController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("authController"));
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
    void noLogInGETTest() throws Exception {
        this.mockMvc.perform(get("/title"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("test")
    public void titleGETPageTest() throws Exception {
        this.mockMvc.perform(get("/title"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("test")));
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

//    @Test
////    @WithUserDetails(value = "test")
//    @WithMockUser(username = "test", password = "test")
//    public void logInWithCredentialsGETPageTest() throws Exception {
//        this.mockMvc.perform(get("/login"))
//                .andDo(print())
//                .andExpect(authenticated())
//                .andExpect(view().name("title"));
//    }

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
                        .param("username", TEST_NAME)
                        .param("password", TEST_PASS)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void registrationGETPageTest() throws Exception {
        this.mockMvc.perform(get("/registration")).andDo(print())
                .andExpect(view().name("registration"));
    }

    @Test
    void registrationOccupiedPOSTPageTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", "pavel")
                        .param("password", "1999")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("уже занято")));
    }

    @Test
    void registrationNewPOSTPageTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", TEST_NAME)
                        .param("password", TEST_PASS)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}