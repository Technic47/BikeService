package ru.kuznetsov.bikeService.services.abstracts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.repositories.ToolRepository;
import ru.kuznetsov.bikeService.services.ToolService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCridentials.*;

@SpringBootTest
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
        Tool newTool = new Tool(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER, TEST_MODEL);
        doReturn(Optional.of(defaultTool))
                .when(repository)
                .findById(TEST_ID);
        toolService.update(TEST_ID, newTool);

        assertEquals(TEST_NAME, this.defaultTool.getName());
        assertEquals(TEST_DESCRIPTION, this.defaultTool.getDescription());
        assertEquals(TEST_PICTURE, this.defaultTool.getPicture());
        assertEquals(TEST_LINK, this.defaultTool.getLink());
        assertEquals(TEST_VALUE, this.defaultTool.getValue());
        assertEquals(TEST_MANUFACTURER, this.defaultTool.getManufacturer());
        assertEquals(TEST_MODEL, this.defaultTool.getModel());
        verify(repository, times(1)).findById(TEST_ID);
        verify(repository, times(1)).save(this.defaultTool);
    }
}