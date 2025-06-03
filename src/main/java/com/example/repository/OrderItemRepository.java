package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderItem;

/**
 * order_itemsとやりとりする
 * 
 * @author naramasato
 *
 */
@Repository
public class OrderItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	// private static final RowMapper<OrderItemRepository> ORDER_ITEM_ROW_MAPPER
	// = new BeanPropertyRowMapper<>(OrderItemRepository.class);

	/**
	 * order_itemsにINSERTする
	 * 
	 * @param orderItem
	 * @return 自動採番されたid
	 */
	public Integer order(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);

		String sql = "INSERT INTO order_items(item_id, order_id, quantity, size, price) "
				+ "VALUES(:itemId, :orderId, :quantity, :size, :itemPrice)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		template.update(sql, param, keyHolder, keyColumnNames);

		orderItem.setId(keyHolder.getKey().intValue());

		return orderItem.getId();
	}

	/**
	 * 注文時の金額を登録
	 * 
	 * @param id 注文商品ID
	 */
	public void insertPrice(OrderItem orderItem) {
		String sql = "UPDATE order_items SET price = :itemPrice WHERE id = :id";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		template.update(sql, param);
	}

	/**
	 * 注文内の商品情報の削除
	 * 
	 * @param orderId
	 * @param id
	 */
	public void delete(Integer id) {
		String sql = "DELETE FROM order_items WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}

	/**
	 * カート内の商品の重複
	 * 
	 * @param quantity
	 */
	public void addQuantity(Integer quantity) {
		String sql = "UPDATE order_items SET quantity = :quantity";
		SqlParameterSource param = new MapSqlParameterSource().addValue("quantity", quantity);
		template.update(sql, param);
	}
}
