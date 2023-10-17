package ru.bikeservice.mainresources.services.abstracts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.repositories.modelRepositories.PartRepository;
import ru.bikeservice.mainresources.services.modelServices.PartService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.bikeservice.mainresources.TestCredentials.*;

class AbstractServiceableServiceTest extends AbstractServiceTests {
    private PartService partService;
    @MockBean
    private PartRepository repository;
    @Mock
    private Part defaultPart;

    @BeforeEach
    void setUp() {
        this.partService = new PartService(repository);
        this.defaultPart = new Part();
        this.defaultPart.setId(TEST_ID);
    }

    @Test
    void update() {
        doReturn(Optional.of(defaultPart))
                .when(repository)
                .findById(TEST_ID);
        partService.update(TEST_ID, TEST_PART);

        assertEquals(TEST_NAME, this.defaultPart.getName());
        assertEquals(TEST_DESCRIPTION, this.defaultPart.getDescription());
        assertEquals(TEST_PICTURE, this.defaultPart.getPicture());
        assertEquals(TEST_LINK, this.defaultPart.getLink());
        assertEquals(TEST_VALUE, this.defaultPart.getValue());
        assertEquals(TEST_MANUFACTURER_ID, this.defaultPart.getManufacturer());
        assertEquals(TEST_MODEL, this.defaultPart.getModel());
        verify(repository, times(1)).findById(TEST_ID);
        verify(repository, times(1)).save(this.defaultPart);
    }

    @Test
    void addToLinkedItems() {
        this.defaultPart.setLinkedItems(TEST_ITEM_LIST);
        int size = TEST_ITEM_LIST.size() + 1;
        PartEntity part1 = new PartEntity("Part", "Consumable", 10L, 100);
        partService.addToLinkedItems(defaultPart, part1);
        partService.addToLinkedItems(defaultPart, part1);

        assertThat(defaultPart.getLinkedItems().size()).isEqualTo(size);
        assertTrue(defaultPart.getLinkedItems().contains(part1));
        PartEntity searchPart = defaultPart.getLinkedItems()
                .stream().filter(item -> item.equals(part1))
                .findFirst()
                .get();
        assertEquals(200, searchPart.getAmount());
        verify(repository, times(2)).save(this.defaultPart);
    }

    @Test
    void delFromLinkedItems() {
        this.defaultPart.setLinkedItems(TEST_ITEM_LIST);
        int size = TEST_ITEM_LIST.size() - 1;
        PartEntity part1 = new PartEntity("Part", "Consumable", 3L, 50);
        PartEntity part2 = new PartEntity("Bike", "Tool", 2L, 220);
        partService.delFromLinkedItems(defaultPart, part1);
        partService.delFromLinkedItems(defaultPart, part2);

        assertThat(defaultPart.getLinkedItems().size()).isEqualTo(size);
        assertTrue(defaultPart.getLinkedItems().contains(part1));
        assertFalse(defaultPart.getLinkedItems().contains(part2));

        PartEntity searchPart = defaultPart.getLinkedItems()
                .stream().filter(item -> item.equals(part1))
                .findFirst()
                .get();
        assertEquals(50, searchPart.getAmount());
        verify(repository, times(2)).save(this.defaultPart);
    }
}