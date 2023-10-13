package ru.bikeservice.mainresources.services.abstracts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.repositories.modelRepositories.DocumentRepository;
import ru.bikeservice.mainresources.services.modelServices.DocumentService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static ru.bikeservice.mainresources.TestCredentials.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class AbstractShowableServiceTest {
    private DocumentService documentService;
    @MockBean
    private DocumentRepository repository;
    @Mock
    private Document defaultDocument;

    @BeforeEach
    void setUp() {
        this.documentService = new DocumentService(repository);
        this.defaultDocument = new Document();
        this.defaultDocument.setId(TEST_ID);
    }

    @Test
    void update() {
        doReturn(Optional.of(defaultDocument))
                .when(repository)
                .findById(TEST_ID);
        documentService.update(TEST_ID, new Document(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_LINK, 123L));

        assertEquals(TEST_NAME, this.defaultDocument.getName());
        assertEquals(TEST_DESCRIPTION, this.defaultDocument.getDescription());
        assertEquals(TEST_PICTURE, this.defaultDocument.getPicture());
        assertEquals(TEST_LINK, this.defaultDocument.getLink());
        assertEquals(TEST_LINK, this.defaultDocument.getValue());
        verify(repository, times(1)).findById(TEST_ID);
        verify(repository, times(1)).save(this.defaultDocument);
    }

    @Test
    void findByCreator() {
        assertNotNull(documentService.findByCreator(TEST_ID));
    }

    @Test
    void findByNameContainingIgnoreCase() {
        assertNotNull(documentService.findByNameContainingIgnoreCase(TEST_NAME));
        verify(repository, times(1)).findByNameContainingIgnoreCase(TEST_NAME);
    }

    @Test
    void findByDescriptionContainingIgnoreCase() {
        assertNotNull(documentService.findByDescriptionContainingIgnoreCase(TEST_NAME));
        verify(repository, times(1)).findByDescriptionContainingIgnoreCase(TEST_NAME);
    }
}