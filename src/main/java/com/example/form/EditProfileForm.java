package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * プロフィール編集用フォーム
 * 
 * @author yukisato
 */
public class EditProfileForm {

    @NotBlank(message = "{NotBlank.form.name}")
    private String name;

    @NotBlank(message = "{NotBlank.form.zipcode}")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "{Pattern.form.zipcode}")
    private String zipcode;

    @NotBlank(message = "{NotBlank.form.address}")
    private String address;

    @NotBlank(message = "{NotBlank.form.tel}")
    @Pattern(regexp = "^[0-9]+-[0-9]+-[0-9]+$", message = "{Pattern.form.tel}")
    private String telephone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    
}
