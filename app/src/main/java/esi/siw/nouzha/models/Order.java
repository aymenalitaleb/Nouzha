package esi.siw.nouzha.models;

public class Order {
    private  String activityId;
    private  String activityName;
    private  String quantity;
    private  String price;
    private  String discount;
    private String image;


    public Order() {
    }

    public Order(String activityId, String activityName, String quantity, String price, String discount) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public Order(String activityId, String activityName, String quantity, String price, String discount, String image) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.image = image;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
