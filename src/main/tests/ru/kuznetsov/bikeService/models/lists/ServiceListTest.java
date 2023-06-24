package ru.kuznetsov.bikeService.models.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;

class ServiceListTest {
    private final ServiceList serviceList = new ServiceList();


    @BeforeEach
    void fill(){
        this.serviceList.addToPartMap(TEST_PART, 1);
        this.serviceList.addToDocumentMap(TEST_DOCUMENT, 1);
        this.serviceList.addToConsumableMap(TEST_CONSUMABLE, 100);
        this.serviceList.addToFastenerMap(TEST_FASTENER, 10);
        this.serviceList.addToToolMap(TEST_TOOL, 1);
    }

    @Test
    void serviceListCreationTest() {
        assertNotNull(this.serviceList.getConsumableMap());
        assertNotNull(this.serviceList.getDocsMap());
        assertNotNull(this.serviceList.getFastenerMap());
        assertNotNull(this.serviceList.getToolMap());
        assertNotNull(this.serviceList.getPartMap());
    }

    @Test
    void serviceListAddTest() {
        assertThat(this.serviceList.getPartMap()).isNotEmpty();
        assertTrue(this.serviceList.getPartMap().containsKey(TEST_PART));
        assertTrue(this.serviceList.getPartMap().containsValue(1));

        assertThat(this.serviceList.getDocsMap()).isNotEmpty();
        assertTrue(this.serviceList.getDocsMap().containsKey(TEST_DOCUMENT));
        assertTrue(this.serviceList.getDocsMap().containsValue(1));

        assertThat(this.serviceList.getConsumableMap()).isNotEmpty();
        assertTrue(this.serviceList.getConsumableMap().containsKey(TEST_CONSUMABLE));
        assertTrue(this.serviceList.getConsumableMap().containsValue(100));

        assertThat(this.serviceList.getFastenerMap()).isNotEmpty();
        assertTrue(this.serviceList.getFastenerMap().containsKey(TEST_FASTENER));
        assertTrue(this.serviceList.getFastenerMap().containsValue(10));

        assertThat(this.serviceList.getToolMap()).isNotEmpty();
        assertTrue(this.serviceList.getToolMap().containsKey(TEST_TOOL));
        assertTrue(this.serviceList.getToolMap().containsValue(1));
    }

    @Test
    void combiningListsNewPositions(){
        ServiceList newList = new ServiceList();
        newList.addToPartMap(new Part(), 1);
        newList.addToDocumentMap(new Document(), 1);
        newList.addToConsumableMap(new Consumable(), 100);
        newList.addToFastenerMap(new Fastener(), 10);
        newList.addToToolMap(new Tool(), 1);

        this.serviceList.addAllToList(newList);

        assertEquals(2, this.serviceList.getPartMap().size());
        assertEquals(2, this.serviceList.getToolMap().size());
        assertEquals(2, this.serviceList.getDocsMap().size());
        assertEquals(10, this.serviceList.getFastenerMap().get(TEST_FASTENER));
        assertEquals(100, this.serviceList.getConsumableMap().get(TEST_CONSUMABLE));
    }

    @Test
    void combiningListsDoublePositions(){
        ServiceList newList = new ServiceList();
        newList.addToPartMap(TEST_PART, 1);
        newList.addToDocumentMap(TEST_DOCUMENT, 1);
        newList.addToConsumableMap(TEST_CONSUMABLE, 100);
        newList.addToFastenerMap(TEST_FASTENER, 10);
        newList.addToToolMap(TEST_TOOL, 1);

        this.serviceList.addAllToList(newList);

        assertEquals(1, this.serviceList.getPartMap().size());
        assertEquals(1, this.serviceList.getToolMap().size());
        assertEquals(1, this.serviceList.getDocsMap().size());
        assertEquals(20, this.serviceList.getFastenerMap().get(TEST_FASTENER));
        assertEquals(200, this.serviceList.getConsumableMap().get(TEST_CONSUMABLE));
    }
}