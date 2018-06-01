package esi.siw.nouzha.models;

public class User {
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Password;
    private String Birthday;
    private String PlaceBirth;
    private String Profession;
    private String Avatar;
    private String Phone;
    private String isStaff;
    private String Budget;
    private String DateFrom;
    private String DateTo;




    public User() {
    }

    public User(String firstname, String lastname, String email, String password, String birthday, String placeBirth, String profession, String avatar) {
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
        Password = password;
        Birthday = birthday;
        PlaceBirth = placeBirth;
        Profession = profession;
        Avatar = avatar;
    }

    public User(String firstname, String lastname, String email, String password, String birthday, String placeBirth, String profession, String avatar, String isStaff, String budget, String dateFrom, String dateTo) {
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
        Password = password;
        Birthday = birthday;
        PlaceBirth = placeBirth;
        Profession = profession;
        Avatar = avatar;
        this.isStaff = isStaff;
        Budget = budget;
        this.DateFrom = dateFrom;
        this.DateTo = dateTo;
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

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getPlaceBirth() {
        return PlaceBirth;
    }

    public void setPlaceBirth(String placeBirth) {
        PlaceBirth = placeBirth;
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

    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.DateFrom = dateFrom;
    }

    public String getDateTo() {
        return DateTo;
    }

    public void setDateTo(String dateTo) {
        this.DateTo = dateTo;
    }
}
