package esi.siw.nouzha.models;

public class Rating {
    private String userPhone;
    private String activityId;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String activityId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.activityId = activityId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
