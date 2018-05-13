package esi.siw.nouzha.Entities;

public class Activity {

    private String Designation;
    private String NbPlace;
    private String Description;
    private String Date;
    private String Time_from;
    private String Time_to;
    private String Prix;
    private String Image;

    public Activity() {
    }

    public Activity(String designation, String nbPlace, String description, String date, String time_from, String time_to, String prix, String image) {
        Designation = designation;
        NbPlace = nbPlace;
        Description = description;
        Date = date;
        Time_from = time_from;
        Time_to = time_to;
        Prix = prix;
        Image = image;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getNbPlace() {
        return NbPlace;
    }

    public void setNbPlace(String nbPlace) {
        NbPlace = nbPlace;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime_from() {
        return Time_from;
    }

    public void setTime_from(String time_from) {
        Time_from = time_from;
    }

    public String getTime_to() {
        return Time_to;
    }

    public void setTime_to(String time_to) {
        Time_to = time_to;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String prix) {
        Prix = prix;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
