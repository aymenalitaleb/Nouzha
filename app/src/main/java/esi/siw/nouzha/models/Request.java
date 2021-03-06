package esi.siw.nouzha.models;

import java.util.List;

public class Request {

    private String phone;
    private String name;
    private String city;
    private String total;
    private String status;
    private String date;
    private String comment;
    private List<Order> activities;


    public Request() {
    }

    public Request(String phone, String name, String city, String total,String comment, List<Order> activities) {
        this.phone = phone;
        this.name = name;
        this.city = city;
        this.total = total;
        this.date="";
        this.comment=comment;
        this.activities = activities;
        this.status="0";// 0: few weeks left, 1: less than a week, 2: few days left
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getActivities() {
        return activities;
    }

    public void setActivities(List<Order> activities) {
        this.activities = activities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
