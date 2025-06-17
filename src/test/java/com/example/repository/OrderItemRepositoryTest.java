package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.example.domain.OrderItem;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void setUp() {
        String sql = "DELETE FROM order_items WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", 100);
        template.update(sql, param);
        String insertOrderItemSql = "INSERT INTO order_items (id, item_id, order_id, quantity, size, price) " +
                "VALUES (:id, :itemId, :orderId, :quantity, :size, :price)";
        template.update(insertOrderItemSql, new MapSqlParameterSource()
                .addValue("id", 100)
                .addValue("itemId", 1)
                .addValue("orderId", 10)
                .addValue("quantity", 1)
                .addValue("size", "M")
                .addValue("price", 0));
    }

    /**
     * レコードを追加
     */
    @Test
    void test_order() {
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(1);
        orderItem.setOrderId(1);
        orderItem.setQuantity(2);
        orderItem.setSize("M");
        orderItem.setItemPrice(500);

        Integer generatedId = orderItemRepository.order(orderItem);

        assertNotNull(generatedId);
        assertEquals(generatedId, orderItem.getId());
    }

    /**
     * 注文時の金額を登録
     */
    @Test
    void test_insertPrice() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setItemPrice(1490);

        orderItemRepository.insertPrice(orderItem);

        // 検証
        String sql = "SELECT price FROM order_items WHERE id = :id";
        Integer updatedPrice = template.queryForObject(
                sql,
                new MapSqlParameterSource().addValue("id", orderItem.getId()),
                Integer.class);

        assertEquals(1490, updatedPrice);
    }

    /**
     * カート内商品の重複
     */
    @Test
    void test_addQuantity() {
        orderItemRepository.addQuantity(100, 2);
        Integer quantity = template.queryForObject(
                "SELECT quantity FROM order_items WHERE id = :id",
                new MapSqlParameterSource().addValue("id", 100),
                Integer.class);
        assertEquals(2, quantity);
    }

    /**
     * 注文内の商品情報の削除
     */
    @Test
    void test_delete() {
        orderItemRepository.delete(100);

        Integer count = template.queryForObject(
                "SELECT COUNT(*) FROM order_items WHERE id = :id",
                new MapSqlParameterSource().addValue("id", 100),
                Integer.class);

        assertEquals(0, count);
    }

}
