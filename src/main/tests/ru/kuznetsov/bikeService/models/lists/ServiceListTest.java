package ru.kuznetsov.bikeService.models.lists;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.kuznetsov.bikeService.TestCredentials.*;

class ServiceListTest {
    private final ServiceList serviceList = new ServiceList();

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
        this.serviceList.addToPartMap(TEST_PART, 1);
        this.serviceList.addToDocumentMap(TEST_DOCUMENT, 1);
        this.serviceList.addToConsumableMap(TEST_CONSUMABLE, 100);
        this.serviceList.addToFastenerMap(TEST_FASTENER, 10);
        this.serviceList.addToToolMap(TEST_TOOL, 1);

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
}