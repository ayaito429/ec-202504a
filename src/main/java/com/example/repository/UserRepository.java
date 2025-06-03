package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

@Repository
public class UserRepository {

	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		user.setStatus(rs.getInt("status"));
		return user;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	public User findByMailAddress(String email) {
		String sql = "SELECT * FROM users WHERE email=:email";

		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);

		try {
			User user = template.queryForObject(sql, param, USER_ROW_MAPPER);
			return user;
		} catch (Exception e) {
			return null;
		}

	}

	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "INSERT INTO users (name, email, password, zipcode, address, telephone) "
				+ "VALUES (:name, :email, :password, :zipcode, :address, :telephone);";
		template.update(sql, param);
	}

	/**
	 * ユーザー情報を更新する
	 * 
	 * @param user 更新するユーザー情報
	 */
	public void update(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "UPDATE users SET name=:name, zipcode=:zipcode, address=:address, telephone=:telephone WHERE id=:id";
		template.update(sql, param);
	}

	/**
	 * ユーザーのステータスを退会状態に更新（論理削除）
	 * 
	 * @param userId ユーザーID
	 */
	public void withdrawUser(Integer userId) {
		String sql = "UPDATE users SET status = 9 WHERE id = :userId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		template.update(sql, param);
	}

}
