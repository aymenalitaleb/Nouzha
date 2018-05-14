package esi.siw.nouzha.models;

public class User {
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String BirthDay;
    private String BirthPlace;
    private String Profession;
    private String Avatar;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String birthDay, String birthPlace, String profession, String avatar) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
        BirthDay = birthDay;
        BirthPlace = birthPlace;
        Profession = profession;
        Avatar = avatar;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
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
}
