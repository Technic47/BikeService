package ru.kuznetsov.bikeService.models.lists;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PartEntityTest {
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
}