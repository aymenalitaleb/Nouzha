package esi.siw.nouzha.models;

public class Order {
    private  String ActivityId;
    private  String ActivityName;
    private  String Quantity;
    private  String Price;
    private  String Discount;


    public Order() {
    }

    public Order(String activityId, String activityName, String quantity, String price, String discount) {
        ActivityId = activityId;
        ActivityName = activityName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
