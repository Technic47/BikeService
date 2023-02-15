package ru.kuznetsov.bikeService.models.lists;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class PartEntity {
    @Column(name = "part_type")
    private String part_type;
    @Column(name = "type")
    private String type;
    @Column(name = "item_id")
    private Long item_id;
    @Column(name = "amount")
    private int amount;

    public PartEntity(String part_type, String type, Long item_id, int amount) {
        this.part_type = part_type;
        this.type = type;
        this.item_id = item_id;
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

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
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
        if (!(o instanceof PartEntity)) return false;
        PartEntity that = (PartEntity) o;
        return Objects.equals(type, that.type) && Objects.equals(item_id, that.item_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, item_id);
    }
}
