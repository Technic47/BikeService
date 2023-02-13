//package ru.kuznetsov.bikeService.models.lists;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//
//import java.util.Objects;
//
//@Embeddable
//public class PartServiceList {
//    @Column(name = "type")
//    private String type;
//    @Column(name = "item_id")
//    private Long item_id;
//    @Column(name = "amount")
//    private String amount;
//
//    public PartServiceList(String type, Long item_id, String amount) {
//        this.type = type;
//        this.item_id = item_id;
//        this.amount = amount;
//    }
//
//    public PartServiceList() {
//    }
//
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Long getItem_id() {
//        return item_id;
//    }
//
//    public void setItem_id(Long item_id) {
//        this.item_id = item_id;
//    }
//
//    public String getAmount() {
//        return amount;
//    }
//
//    public void setAmount(String amount) {
//        this.amount = amount;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof PartServiceList)) return false;
//        PartServiceList that = (PartServiceList) o;
//        return  Objects.equals(type, that.type) && Objects.equals(item_id, that.item_id) && Objects.equals(amount, that.amount);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(type, item_id, amount);
//    }
//}
