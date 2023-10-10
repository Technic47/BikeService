package ru.kuznetsov.bikeService.models.lists;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public final class PartEntity {
    @Column(name = "part_type")
    private String part_type;
    @Column(name = "type")
    private String type;
    @Column(name = "itemId")
    private Long itemId;
    @Column(name = "amount")
    private int amount;

    public PartEntity(String part_type, String type, Long itemId, int amount) {
        this.part_type = part_type;
        this.type = type;
        this.itemId = itemId;
        this.amount = amount;
    }

    public PartEntity() {
    }

    public String getPart_type() {
        return part_type;
    }

    public void setPart_type(String part_type) {
        this.part_type = part_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long item_id) {
        this.itemId = item_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    /*
    Do not count volume!
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartEntity that)) return false;
        return Objects.equals(type, that.type) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, itemId);
    }
}
