package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * プロフィール編集用フォーム
 * 
 * @author yukisato
 */
public class EditProfileForm {

    @NotBlank(message = "名前を入力してください")
    private String name;

    @NotBlank(message = "郵便番号を入力してください")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "郵便番号はXXX-XXXXの形式で入力してください")
    private String zipcode;

    @NotBlank(message = "住所を入力してください")
    private String address;

    @NotBlank(message = "電話番号を入力してください")
    @Pattern(regexp = "^[0-9]+-[0-9]+-[0-9]+$", message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください")
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
