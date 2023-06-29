package com.socompany.felicitashop.model;

public class Users {
    private String Name;
    private String Phone;
    private String Password;
    private String IsAdmin;
    private String Email;
    private String Location;
    private String Image;

    public Users() {}

    public Users(String Name, String Phone, String Password, String IsAdmin, String Email, String Location, String Image) {
        this.Name = Name;
        this.Phone = Phone;
        this.Password = Password;
        this.IsAdmin = IsAdmin;
        this.Email = Email;
        this.Location = Location;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        IsAdmin = isAdmin;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
