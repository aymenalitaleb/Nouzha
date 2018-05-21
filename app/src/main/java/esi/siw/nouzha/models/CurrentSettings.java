package esi.siw.nouzha.models;

public class CurrentSettings {
    private String budget;
    private String FreeTime_from;
    private String FreeTime_to;
    private String FreeDate;
    private String Phone;
    private String CurrentDate;

    public CurrentSettings() {
    }

    public CurrentSettings(String budget, String freeTime_from, String freeTime_to, String freeDate, String phone, String currentDate) {
        this.budget = budget;
        FreeTime_from = freeTime_from;
        FreeTime_to = freeTime_to;
        FreeDate = freeDate;
        Phone = phone;
        CurrentDate = currentDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getFreeTime_from() {
        return FreeTime_from;
    }

    public void setFreeTime_from(String freeTime_from) {
        FreeTime_from = freeTime_from;
    }

    public String getFreeTime_to() {
        return FreeTime_to;
    }

    public void setFreeTime_to(String freeTime_to) {
        FreeTime_to = freeTime_to;
    }

    public String getFreeDate() {
        return FreeDate;
    }

    public void setFreeDate(String freeDate) {
        FreeDate = freeDate;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }
}
