package ru.kuznetsov.bikeService.models;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModelTests {
    private final Part part = new Part();
    @Test
    @Order(1)
    void creationTest() {
        part.setId(TEST_ID);
        part.setName(TEST_NAME);
        part.setDescription(TEST_DESCRIPTION);
        part.setPicture(TEST_PICTURE);
        part.setLink(TEST_LINK);
        part.setValue(TEST_VALUE);
        part.setCreator(TEST_CREATOR);
        part.setManufacturer(TEST_MANUFACTURER_ID);
        part.setModel(TEST_MODEL);


        assertEquals(TEST_ID, part.getId());
        assertEquals(TEST_NAME, part.getName());
        assertEquals(TEST_DESCRIPTION, part.getDescription());
        assertEquals(TEST_PICTURE, part.getPicture());
        assertEquals(TEST_LINK, part.getLink());
        assertEquals(TEST_VALUE, part.getValue());
        assertEquals(TEST_CREATOR, part.getCreator());
        assertEquals(TEST_MANUFACTURER_ID, part.getManufacturer());
        assertEquals(TEST_MODEL, part.getModel());
        assertFalse(part.getIsShared());
        assertEquals(this.part, TEST_PART);

        this.part.setId(33L);

        assertNotEquals(this.part, TEST_PART);
    }

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
            assertEquals(TEST_PICTURE, showable.getPicture());
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
        assertEquals(TEST_NAME, TEST_DOCUMENT.getCredentials());
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
