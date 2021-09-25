package com.zephyr.golfzone.model;

import java.io.Serializable;

public class User implements Serializable {
    String unique_user_id, firstname, lastname, email, created_at;

    public User() {}

    public User (
            String unique_user_id,
            String firstname,
            String lastname,
            String email,
            String created_at) {
        this.unique_user_id = unique_user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.created_at = created_at;
    }

    public String getUnique_user_id() {
        return unique_user_id;
    }

    public void setUnique_user_id(String unique_user_id) {
        this.unique_user_id = unique_user_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
