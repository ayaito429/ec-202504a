package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * ユーザー登録用フォーム
 * 
 * @author matsunagadai
 *
 */
public class InsertForm {

	// ユーザー氏名
	@NotBlank(message = "{NotBlank.form.name}")
	@Size(max = 100, message = "{Name.form.size}")
	private String name;
	// 郵便番号
	@NotBlank(message = "{NotBlank.form.zipcode}")
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "{Pattern.form.zipcode}")
	@Size(max = 8, message = "{Post.form.size}")
	private String zipcode;
	// 住所
	@NotBlank(message = "{NotBlank.form.address}")
	@Size(max = 200, message = "{Address.form.size}")
	private String address;
	// 電話番号
	@NotBlank(message = "{NotBlank.form.tel}")
	@Pattern(regexp = "^[0-9]+-[0-9]+-[0-9]+$", message = "{Pattern.form.tel}")
	@Size(max = 15, message = "{Phone.form.size}")
	private String telephone;
	// パスワード
	@NotBlank(message = "{NotBlank.form.password}")
	@Size(min = 8, max = 16, message = "{Size.form.password}")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{Pattern.form.password}")
	private String password;
	// 確認用パスワード
	@NotBlank(message = "{NotBlank.form.confirmPassword}")
	private String confirmPassword;

	// ゲッターセッター
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	// toStringのオーバーライド
	@Override
	public String toString() {
		return "InsertForm [name=" + name + ",  zipcode=" + zipcode + ", address=" + address
				+ ", telephone=" + telephone + ", password=" + password + ", confirmPassword=" + confirmPassword + "]";
	}
}
