package ru.kuznetsov.bikeService.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.kuznetsov.bikeService.TestCridentials.*;

@SpringBootTest
public class ModelTests {
    @Test
    void showableInterfaceTest() {
        List<Showable> testList = new ArrayList<>();
        testList.add(TEST_DOCUMENT);
        testList.add(TEST_FASTENER);
        testList.add(TEST_MANUFACTURER);
        for (Showable showable : testList) {
            assertEquals(TEST_ID, showable.getId());
            assertEquals(TEST_NAME, showable.getName());
            if (showable instanceof Document) {
                assertEquals(TEST_LINK, showable.getValue());
                assertEquals(TEST_LINK, showable.getLink());
                showable.setValue("qwerty");
                assertEquals("qwerty", showable.getValue());
                assertEquals("qwerty", showable.getLink());
            } else {
                assertEquals(TEST_VALUE, showable.getValue());
                showable.setValue("qwerty");
                assertEquals("qwerty", showable.getValue());
                assertEquals(TEST_LINK, showable.getLink());
            }
            assertEquals(TEST_DESCRIPTION, showable.getDescription());
            assertEquals(13L, showable.getPicture());
            showable.setPicture(22L);
            assertEquals(22L, showable.getPicture());
            assertEquals(3L, showable.getCreator());
            showable.setCreator(66L);
            assertEquals(66L, showable.getCreator());
        }
    }

    @Test
    void usableInterfaceTest() {
        List<Usable> testList = new ArrayList<>();
        testList.add(TEST_CONSUMABLE);
        testList.add(TEST_TOOL);
        for (Usable usable : testList) {
            assertEquals(TEST_MANUFACTURER_ID, usable.getManufacturer());
            assertEquals(TEST_MODEL, usable.getModel());
        }
    }

    @Test
    void serviceableInterfaceTest() {
        List<Serviceable> testList = new ArrayList<>();
        testList.add(TEST_PART);
        testList.add(TEST_BIKE);
        for (Serviceable serviceable : testList) {
            serviceable.setLinkedItems(TEST_ITEM_LIST);
            assertThat(serviceable.getLinkedItems()).isNotNull();
            assertEquals(TEST_ITEM_LIST, serviceable.getLinkedItems());
        }
        TEST_PART.setLinkedItems(TEST_ITEM_LIST);
        TEST_BIKE.setLinkedItems(TEST_ITEM_LIST);
    }

    private String credentialsBuilder(String valueName) {
        return TEST_NAME +
                ", " +
                valueName +
                ": " +
                TEST_VALUE;
    }

    @Test
    void credentialsDocumentTest() {
        String valueName = "Ссылка";
        TEST_DOCUMENT.setValue(TEST_VALUE);
        assertEquals(valueName, TEST_DOCUMENT.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_DOCUMENT.getCredentials());
    }

    @Test
    void credentialsFastenerTest() {
        String valueName = "Характеристики";
        TEST_FASTENER.setValue(TEST_VALUE);
        assertEquals(valueName, TEST_FASTENER.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_FASTENER.getCredentials());
    }

    @Test
    void credentialsManufacturerTest() {
        String valueName = "Страна";
        TEST_MANUFACTURER.setValue(TEST_VALUE);
        assertEquals(valueName, TEST_MANUFACTURER.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_MANUFACTURER.getCredentials());
    }

    @Test
    void credentialsConsumableTest() {
        String valueName = "Объём";
        assertEquals(valueName, TEST_CONSUMABLE.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_CONSUMABLE.getCredentials());
    }

    @Test
    void credentialsToolTest() {
        String valueName = "Размерность";
        assertEquals(valueName, TEST_TOOL.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_TOOL.getCredentials());
    }

    @Test
    void credentialsPartTest() {
        String valueName = "Заводской номер";
        assertEquals(valueName, TEST_PART.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_PART.getCredentials());
    }

    @Test
    void credentialsBikeTest() {
        String valueName = "Заводской номер";
        assertEquals(valueName, TEST_BIKE.getValueName());
        assertEquals(credentialsBuilder(valueName), TEST_BIKE.getCredentials());
    }
}
