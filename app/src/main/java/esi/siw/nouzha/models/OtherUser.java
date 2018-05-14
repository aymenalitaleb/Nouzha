package esi.siw.nouzha.models;

import java.sql.Blob;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Creator on 23/03/2018.
 */

public class OtherUser {

    private String firstName ;
    private String lastName ;
    private String email ;
    private Date birthDay ;
    private String birthPlace ;
    private String profession ;
    private Blob avatar ;
    private float budget;
    private Time freeTimeFrom;
    private Time freeTimeTo;
    private Date freeDays;
    private Adress adress;

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public Time getFreeTimeFrom() {
        return freeTimeFrom;
    }

    public void setFreeTimeFrom(Time freeTimeFrom) {
        this.freeTimeFrom = freeTimeFrom;
    }

    public Time getFreeTimeTo() {
        return freeTimeTo;
    }

    public void setFreeTimeTo(Time freeTimeTo) {
        this.freeTimeTo = freeTimeTo;
    }

    public Date getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(Date freeDays) {
        this.freeDays = freeDays;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }
}
