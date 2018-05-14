package esi.siw.nouzha.models;

import java.sql.Blob;

/**
 * Created by Creator on 23/03/2018.
 */

public class Establishment {

    private String designation;
    private String email;
    private String description;
    private Blob photo;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }
}
