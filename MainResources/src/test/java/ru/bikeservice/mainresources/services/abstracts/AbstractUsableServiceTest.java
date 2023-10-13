package ru.bikeservice.mainresources.services.abstracts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.repositories.modelRepositories.ToolRepository;
import ru.bikeservice.mainresources.services.modelServices.ToolService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.bikeservice.mainresources.TestCredentials.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class AbstractUsableServiceTest {
    private ToolService toolService;
    @MockBean
    private ToolRepository repository;
    @Mock
    private Tool defaultTool;

    @BeforeEach
    void setUp() {
        this.toolService = new ToolService(repository);
        this.defaultTool = new Tool();
        this.defaultTool.setId(TEST_ID);
    }

    @Test
    void update() {
        doReturn(Optional.of(defaultTool))
                .when(repository)
                .findById(TEST_ID);
        toolService.update(TEST_ID, TEST_TOOL);

        assertEquals(TEST_NAME, this.defaultTool.getName());
        assertEquals(TEST_DESCRIPTION, this.defaultTool.getDescription());
        assertEquals(TEST_PICTURE, this.defaultTool.getPicture());
        assertEquals(TEST_LINK, this.defaultTool.getLink());
        assertEquals(TEST_VALUE, this.defaultTool.getValue());
        assertEquals(TEST_MANUFACTURER_ID, this.defaultTool.getManufacturer());
        assertEquals(TEST_MODEL, this.defaultTool.getModel());

        verify(repository, times(1)).findById(TEST_ID);
        verify(repository, times(1)).save(this.defaultTool);
    }
}