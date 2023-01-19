//package ru.kuznetsov.bikeService.models.abstracts;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.MappedSuperclass;
//import ru.kuznetsov.bikeService.models.service.Usable;
//
//@MappedSuperclass
//public class AbstractUsableEntity extends AbstractShowableEntity implements Usable {
//    @Column(name="manufacturer")
//    protected int manufacturer;
//    @Column(name="model")
//    protected String model;
//
//    public int getManufacturer() {
//        return manufacturer;
//    }
//
//    public void setManufacturer(int manufacturer) {
//        this.manufacturer = manufacturer;
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//}
