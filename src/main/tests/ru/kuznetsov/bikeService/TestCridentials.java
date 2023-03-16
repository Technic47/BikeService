package ru.kuznetsov.bikeService;

import ru.kuznetsov.bikeService.models.lists.PartEntity;

import java.util.HashSet;
import java.util.Set;

public class TestCridentials {
    public static final Long TEST_ID = 666L;
    public static final String TEST_NAME = "testName";
    public static final String TEST_DESCRIPTION = "testDescription";
    public static final Long TEST_PICTURE = 13L;
    public static final String TEST_LINK = "testLink";
    public static final String TEST_VALUE = "testValue";
    public static final Long TEST_CREATOR = 3L;
    public static final Long TEST_MANUFACTURER = 33L;
    public static final String TEST_MODEL = "testModel";
    public static final String TEST_PASS = "testPass";
    public static final Set<PartEntity> TEST_ITEM_LIST = testLinkedItemsFill();

    private static Set<PartEntity> testLinkedItemsFill(){
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
}
