package ru.kuznetsov.bikeService.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kuznetsov.bikeService.Starter;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.models.usable.Usable;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCridentials.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Starter.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModelTests {
    private Document document;
    private Fastener fastener;
    private Manufacturer manufacturer;
    private Consumable consumable;
    private Tool tool;
    private Part part;
    private Bike bike;
    private UserModel userModel;
    private ServiceList serviceList;

    @BeforeAll
    void SetUp() {
        this.document = new Document(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR);
        this.fastener = new Fastener(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR);
        this.manufacturer = new Manufacturer(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR);
        this.consumable = new Consumable(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER, TEST_MODEL);
        this.tool = new Tool(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER, TEST_MODEL);
        this.part = new Part(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER, TEST_MODEL);
        this.bike = new Bike(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER, TEST_MODEL);
//        PartEntity part1 = new PartEntity("Bike", "Fastener", 1L, 5);
//        PartEntity part2 = new PartEntity("Bike", "Tool", 2L, 220);
//        PartEntity part3 = new PartEntity("Part", "Consumable", 3L, 100);
//        PartEntity part4 = new PartEntity("Part", "Document", 4L, 1);
//        this.testLinkedItems.add(part1);
//        this.testLinkedItems.add(part2);
//        this.testLinkedItems.add(part3);
//        this.testLinkedItems.add(part4);
        this.userModel = new UserModel();
        this.userModel.setId(TEST_ID);
        this.userModel.setUsername(TEST_NAME);
        this.userModel.setActive(true);
        this.userModel.setPassword("TestPass");
        this.serviceList = new ServiceList();
    }

    @Test
    void showableInterfaceTest() {
        List<Showable> testList = new ArrayList<>();
        testList.add(this.document);
        testList.add(this.fastener);
        testList.add(this.manufacturer);
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
        testList.add(this.consumable);
        testList.add(this.tool);
        for (Usable usable : testList) {
            assertEquals(TEST_MANUFACTURER, usable.getManufacturer());
            assertEquals(TEST_MODEL, usable.getModel());
        }
    }

    @Test
    void partEntityTest() {
        PartEntity part1 = new PartEntity();
        part1.setPart_type("Part");
        part1.setType("Document");
        part1.setItem_id(1L);
        part1.setAmount(150);
        PartEntity part2 = new PartEntity("Part", "Document", 1L, 200);

        assertEquals("Part", part1.getPart_type());
        assertEquals("Part", part2.getPart_type());
        assertEquals("Document", part1.getType());
        assertEquals("Document", part2.getType());
        assertEquals(1L, part1.getItem_id());
        assertEquals(1L, part2.getItem_id());
        assertEquals(150, part1.getAmount());
        assertEquals(200, part2.getAmount());

        assertEquals(part1, part2);
        part2.setItem_id(2L);
        assertNotEquals(part1, part2);
    }

    @Test
    void serviceableInterfaceTest() {
        List<Serviceable> testList = new ArrayList<>();
        testList.add(this.part);
        testList.add(this.bike);
        for (Serviceable serviceable : testList) {
            serviceable.setLinkedItems(TEST_ITEM_LIST);
            assertThat(serviceable.getLinkedItems()).isNotNull();
            assertEquals(TEST_ITEM_LIST, serviceable.getLinkedItems());
        }
        this.part.setLinkedItems(TEST_ITEM_LIST);
        this.bike.setLinkedItems(TEST_ITEM_LIST);
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
        this.document.setValue(TEST_VALUE);
        assertEquals(valueName, this.document.getValueName());
        assertEquals(credentialsBuilder(valueName), this.document.getCredentials());
    }

    @Test
    void credentialsFastenerTest() {
        String valueName = "Характеристики";
        this.fastener.setValue(TEST_VALUE);
        assertEquals(valueName, this.fastener.getValueName());
        assertEquals(credentialsBuilder(valueName), this.fastener.getCredentials());
    }

    @Test
    void credentialsManufacturerTest() {
        String valueName = "Страна";
        this.manufacturer.setValue(TEST_VALUE);
        assertEquals(valueName, this.manufacturer.getValueName());
        assertEquals(credentialsBuilder(valueName), this.manufacturer.getCredentials());
    }

    @Test
    void credentialsConsumableTest() {
        String valueName = "Объём";
        assertEquals(valueName, this.consumable.getValueName());
        assertEquals(credentialsBuilder(valueName), this.consumable.getCredentials());
    }

    @Test
    void credentialsToolTest() {
        String valueName = "Размерность";
        assertEquals(valueName, this.tool.getValueName());
        assertEquals(credentialsBuilder(valueName), this.tool.getCredentials());
    }

    @Test
    void credentialsPartTest() {
        String valueName = "Заводской номер";
        assertEquals(valueName, this.part.getValueName());
        assertEquals(credentialsBuilder(valueName), this.part.getCredentials());
    }

    @Test
    void credentialsBikeTest() {
        String valueName = "Заводской номер";
        assertEquals(valueName, this.bike.getValueName());
        assertEquals(credentialsBuilder(valueName), this.bike.getCredentials());
    }

    @Test
    void userEntityTest() {
        UserEntity entity1 = new UserEntity();
        entity1.setType("Document");
        entity1.setId(1L);
        UserEntity entity2 = new UserEntity("Document", 1L);
        assertEquals("Document", entity1.getType());
        assertEquals(1L, entity1.getId());
        assertEquals(entity1, entity2);
        entity2.setId(2L);
        assertNotEquals(entity1, entity2);
    }

    @Test
    void userModelTest() {
        Set<UserRole> status = new HashSet<>();
        status.add(UserRole.ROLE_USER);
        this.userModel.setStatus(status);

        List<UserEntity> createdItems = new ArrayList<>();
        createdItems.add(new UserEntity("Document", 1L));
        createdItems.add(new UserEntity("Fastener", 2L));
        createdItems.add(new UserEntity("Document", 11L));
        this.userModel.setCreatedItems(createdItems);

        assertEquals(TEST_ID, this.userModel.getId());
        assertEquals(TEST_NAME, this.userModel.getUsername());
        assertTrue(this.userModel.isActive());
        assertEquals("TestPass", this.userModel.getPassword());
        assertThat(this.userModel.getStatus()).size().isEqualTo(1);
        assertEquals(status, userModel.getStatus());
    }

    @Test
    void userModelListTest() {
        List<UserEntity> createdItems = new ArrayList<>();
        createdItems.add(new UserEntity("Document", 1L));
        createdItems.add(new UserEntity("Fastener", 2L));
        createdItems.add(new UserEntity("Document", 11L));
        this.userModel.setCreatedItems(createdItems);

        assertEquals(createdItems, userModel.getCreatedItems());
    }

    @Test
    void userModelEqualsTest() {
        UserModel userModel2 = new UserModel();
        userModel2.setId(TEST_ID);
        userModel2.setUsername(TEST_NAME);
        userModel2.setActive(true);
        userModel2.setPassword("TestPass");

        assertEquals(userModel, userModel2);
        userModel2.setPassword("12345");
        assertEquals(userModel, userModel2);
        userModel2.setUsername("qwqw");
        assertNotEquals(userModel, userModel2);
        userModel2.setUsername(TEST_NAME);
        userModel2.setId(22L);
        assertNotEquals(userModel, userModel2);
    }

    @Test
    void pictureTest() {
        Picture picture = new Picture();
        picture.setId(TEST_ID);
        picture.setName(TEST_NAME);
        assertEquals(TEST_ID, picture.getId());
        assertEquals(TEST_NAME, picture.getName());

        Picture picture2 = new Picture();
        picture2.setId(TEST_ID);
        picture2.setName(TEST_NAME);

        assertEquals(picture, picture2);
        picture2.setName("qwe");
        assertNotEquals(picture, picture2);
        picture2.setName(TEST_NAME);
        picture2.setId(111L);
        assertNotEquals(picture, picture2);
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
        this.serviceList.addToPartMap(this.part, 1);
        this.serviceList.addToDocumentMap(this.document, 1);
        this.serviceList.addToConsumableMap(this.consumable, 100);
        this.serviceList.addToFastenerMap(this.fastener, 10);
        this.serviceList.addToToolMap(this.tool, 1);

        assertThat(this.serviceList.getPartMap()).isNotEmpty();
        assertTrue(this.serviceList.getPartMap().containsKey(this.part));
        assertTrue(this.serviceList.getPartMap().containsValue(1));

        assertThat(this.serviceList.getDocsMap()).isNotEmpty();
        assertTrue(this.serviceList.getDocsMap().containsKey(this.document));
        assertTrue(this.serviceList.getDocsMap().containsValue(1));

        assertThat(this.serviceList.getConsumableMap()).isNotEmpty();
        assertTrue(this.serviceList.getConsumableMap().containsKey(this.consumable));
        assertTrue(this.serviceList.getConsumableMap().containsValue(100));

        assertThat(this.serviceList.getFastenerMap()).isNotEmpty();
        assertTrue(this.serviceList.getFastenerMap().containsKey(this.fastener));
        assertTrue(this.serviceList.getFastenerMap().containsValue(10));

        assertThat(this.serviceList.getToolMap()).isNotEmpty();
        assertTrue(this.serviceList.getToolMap().containsKey(this.tool));
        assertTrue(this.serviceList.getToolMap().containsValue(1));
    }
}
