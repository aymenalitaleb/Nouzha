package esi.siw.nouzha.models;

public class User {
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Password;
    private String BirthDay;
    private String BirthPlace;
    private String Profession;
    private String Avatar;
    private String Phone;
    private String isStaff;

    public User() {
    }

    public User(String firstname, String lastname, String email, String password, String birthDay, String birthPlace, String profession, String avatar) {
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
        Password = password;
        BirthDay = birthDay;
        BirthPlace = birthPlace;
        Profession = profession;
        Avatar = avatar;
    }

    public User(String firstname, String lastname, String email, String password, String birthDay, String birthPlace, String profession, String avatar, String isStaff) {
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
        Password = password;
        BirthDay = birthDay;
        BirthPlace = birthPlace;
        Profession = profession;
        Avatar = avatar;
        this.isStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(String birthDay) {
        BirthDay = birthDay;
    }

    public String getBirthPlace() {
        return BirthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        BirthPlace = birthPlace;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
