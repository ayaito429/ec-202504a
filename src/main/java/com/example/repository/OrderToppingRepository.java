package com.example.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderTopping;

/**
 * order_toppingsテーブルとやりとりする
 * @author naramasato
 *
 */
@Repository
public class OrderToppingRepository {

	
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	//private static final RowMapper<OrderTopping> ORDER_TOPPING_ROW_MAPPER = (rs,i) -> {
	
	/**
	 * 注文トッピングを追加
	 * @param orderTopping
	 */
	public void insert(OrderTopping orderTopping) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		String insertSql = "INSERT INTO order_toppings (topping_id, order_item_id, price) "
				+ "VALUES (:toppingId, :orderItemId, :price);";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"id"};
		template.update(insertSql, param, keyHolder, keyColumnNames);
	}
}
