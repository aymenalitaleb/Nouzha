package esi.siw.nouzha.models;

public class Activity {

    private String Designation;
    private String NbPlaces;
    private String Description;
    private String Date;
    private String Time_from;
    private String Time_to;
    private String Prix;
    private String Image;
    private String Street;
    private String Number;
    private String ZipCode;
    private String City;
    private String Attitude;
    private String Longitude;

    public Activity() {
    }

    public Activity(String designation, String nbPlaces, String description, String date, String time_from, String time_to, String prix, String image, String street, String number, String zipCode, String city, String attitude, String longitude) {
        Designation = designation;
        NbPlaces = nbPlaces;
        Description = description;
        Date = date;
        Time_from = time_from;
        Time_to = time_to;
        Prix = prix;
        Image = image;
        Street = street;
        Number = number;
        ZipCode = zipCode;
        City = city;
        Attitude = attitude;
        Longitude = longitude;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getNbPlaces() {
        return NbPlaces;
    }

    public void setNbPlaces(String nbPlaces) {
        NbPlaces = nbPlaces;
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

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAttitude() {
        return Attitude;
    }

    public void setAttitude(String attitude) {
        Attitude = attitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
