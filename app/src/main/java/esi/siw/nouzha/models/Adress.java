package esi.siw.nouzha.models;

/**
 * Created by Creator on 23/03/2018.
 */

public class Adress {

    private String Street;
    private String Number;
    private String ZipCode;
    private String City;
    private double Attitude;
    private double Longitude;

    public Adress() {
    }

    public Adress(String street, String number, String zipCode, String city, double attitude, double longitude) {
        Street = street;
        Number = number;
        ZipCode = zipCode;
        City = city;
        Attitude = attitude;
        Longitude = longitude;
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

    public double getAttitude() {
        return Attitude;
    }

    public void setAttitude(double attitude) {
        Attitude = attitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

