package ru.kuznetsov.bikeService.controllers;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.kuznetsov.bikeService.Starter;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.controllers.showable.DocumentController;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.services.DocumentService;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Starter.class)
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {SpringConfig.class})
//@WebAppConfiguration
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class BasicControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private DocumentController documentController;
    @MockBean
    private UserService userService;
    @MockBean
    private PictureService pictureService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private Model model;
    @MockBean
    private Principal principal;
    @Mock
    private UserModel userModel;
    private Logger logger;

    @BeforeEach
    void StartUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.documentController = new DocumentController(documentService);
        this.documentController.setUserService(this.userService);
        this.documentController.setPictureDao(this.pictureService);
        this.logger = AbstractController.logger;
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("documentController"));
    }

    @Test
    public void homePageTest() throws Exception {
        this.mockMvc.perform(get("/documents").principal(this.principal)).andDo(print())
                .andExpect(view().name("show/index"));
    }

    @Test
    void creationTest() {
        assertEquals(Document.class.getSimpleName().toLowerCase(), this.documentController.currentObjectName);
        assertEquals(Document.class.getSimpleName().toLowerCase() + "s", this.documentController.category);
        assertNotNull(this.documentController.thisClassNewObject);
        assertNotNull(this.documentController.getUserService());
        assertNotNull(this.documentController.getPictureDao());
        assertNotNull(this.logger);
    }

    @Test
    void index() {
        Set<UserRole> status = new HashSet<>();
        status.add(UserRole.ROLE_USER);
        this.userModel.setStatus(status);
        this.documentController.setUser(this.userModel);
        this.documentController.index(principal, model);

    }
}