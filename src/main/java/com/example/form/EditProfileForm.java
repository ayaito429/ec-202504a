package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * プロフィール編集用フォーム
 * 
 * @author yukisato
 */
public class EditProfileForm {

    @NotBlank(message = "{NotBlank.form.name}")
    @Size(max = 100, message = "{Name.form.size}")
    private String name;

    @NotBlank(message = "{NotBlank.form.zipcode}")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "{Pattern.form.zipcode}")
    @Size(max = 8, message = "{Post.form.size}")
    private String zipcode;

    @NotBlank(message = "{NotBlank.form.address}")
    @Size(max = 200, message = "{Address.form.size}")
    private String address;

    @NotBlank(message = "{NotBlank.form.tel}")
    @Pattern(regexp = "^[0-9]+-[0-9]+-[0-9]+$", message = "{Pattern.form.tel}")
    @Size(max = 15, message = "{Phone.form.size}")
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
