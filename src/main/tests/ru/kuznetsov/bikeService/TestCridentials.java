package ru.kuznetsov.bikeService;

import org.springframework.mock.web.MockMultipartFile;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static ru.kuznetsov.bikeService.controllers.pictures.PictureWorkTest.PATH_WIDE_FILE;


public class TestCridentials {
    public static final Long TEST_ID = 666L;
    public static final String TEST_NAME = "testName";
    public static final String TEST_DESCRIPTION = "testDescription";
    public static final Long TEST_PICTURE = 13L;
    public static final String TEST_LINK = "testLink";
    public static final String TEST_VALUE = "testValue";
    public static final Long TEST_CREATOR = 3L;
    public static final Long TEST_MANUFACTURER_ID = 33L;
    public static final String TEST_MODEL = "testModel";
    public static final String TEST_PASS = "testPass";
    public static final Set<PartEntity> TEST_ITEM_LIST = testLinkedItemsFill();
    public static final Document TEST_DOCUMENT = new Document(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR);
    public static final Fastener TEST_FASTENER = new Fastener(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR);
    ;
    public static final Manufacturer TEST_MANUFACTURER = new Manufacturer(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR);
    public static final Consumable TEST_CONSUMABLE = new Consumable(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER_ID, TEST_MODEL);
    public static final Tool TEST_TOOL = new Tool(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER_ID, TEST_MODEL);
    public static final Part TEST_PART = new Part(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER_ID, TEST_MODEL);
    public static final Bike TEST_BIKE = new Bike(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_PICTURE, TEST_LINK, TEST_VALUE, TEST_CREATOR, TEST_MANUFACTURER_ID, TEST_MODEL);

    private static Set<PartEntity> testLinkedItemsFill() {
        Set<PartEntity> newItemsSet = new HashSet<>();
        PartEntity part1 = new PartEntity("Part", "Fastener", 1L, 5);
        PartEntity part2 = new PartEntity("Part", "Tool", 2L, 220);
        PartEntity part3 = new PartEntity("Part", "Consumable", 3L, 100);
        PartEntity part4 = new PartEntity("Part", "Document", 4L, 1);
        newItemsSet.add(part1);
        newItemsSet.add(part2);
        newItemsSet.add(part3);
        newItemsSet.add(part4);
        return newItemsSet;
    }

    public static MockMultipartFile getMultipartFile() {
        MockMultipartFile multipartFile = null;
        try {
            File initialFile = new File(PATH_WIDE_FILE);
            InputStream targetStream1 = new FileInputStream(initialFile);
            multipartFile = new MockMultipartFile("newImage", initialFile.getName(), "image/jpeg", targetStream1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return multipartFile;
    }
}
