package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.example.domain.Order;

/**
 * OrderRepositoryのテストクラス
 * 
 * @author aya_ito
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void setUp() {
        // id=100のレコードを削除
        String deleteSql = "DELETE FROM orders WHERE id = :id";
        MapSqlParameterSource param1 = new MapSqlParameterSource().addValue("id", 100);
        template.update(deleteSql, param1);

        // id=100のレコード作成
        String insertSql = "INSERT INTO orders (id, user_id, status, total_price, order_date) " +
                "VALUES (:id, :userId, :status, :totalPrice, current_timestamp)";
        MapSqlParameterSource param2 = new MapSqlParameterSource()
                .addValue("id", 100)
                .addValue("userId", 2)
                .addValue("status", 0) // 未入金 or 初期状態を想定
                .addValue("totalPrice", 2980)
                .addValue("orderDate", LocalDateTime.of(2025, 7, 1, 15, 0))
                .addValue("destinationName", "テスト太郎")
                .addValue("destinationEmail", "test@example.com")
                .addValue("destinationZipcode", "1234567")
                .addValue("destinationAddress", "東京都千代田区1-1-1")
                .addValue("destinationTel", "090-1111-2222")
                .addValue("deliveryTime", LocalDateTime.of(2025, 7, 4, 10, 0))
                .addValue("paymentMethod", 1);
        template.update(insertSql, param2);

        String sql = "DELETE FROM order_items WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", 100);
        template.update(sql, param);

        String insertOrderItemSql = "INSERT INTO order_items (id, item_id, order_id, quantity, size, price) " +
                "VALUES (:id, :itemId, :orderId, :quantity, :size, :price)";
        template.update(insertOrderItemSql, new MapSqlParameterSource()
                .addValue("id", 100)
                .addValue("itemId", 1)
                .addValue("orderId", 100)
                .addValue("quantity", 1)
                .addValue("size", "M")
                .addValue("price", 0));
    }

    /**
     * orderの詳細を表示
     */
    @Test
    void testOrderLoad() {
        List<Order> orderList = orderRepository.orderLoad(1);
        assertEquals("山田太郎", orderList.get(0).getDestinationName());
    }

    /**
     * テーブルを結合して出力(データが存在する場合)
     */
    @Test
    void testFindByOrdertable() {
        List<Order> orderList = orderRepository.findByOrdertable(2);
        assertEquals(1, orderList.size());
    }

    /**
     * テーブルを結合して出力(データが存在しない場合)
     */
    @Test
    void testFindByOrdertableNoMatch() {
        List<Order> orderList = orderRepository.findByOrdertable(100);
        assertNull(orderList);
    }

    /**
     * userIdとstatusで検索(該当するデータが存在する場合)
     */
    @Test
    void testFindByStatus() {
        List<Order> orderList = orderRepository.findByStatus(1, 9);
        assertEquals(1, orderList.size());
    }

    /**
     * userIdとstatusで検索(該当するデータが存在しない場合)
     */
    @Test
    void testFindByStatusNoMatch() {
        List<Order> orderList = orderRepository.findByStatus(99, 9);
        assertNull(orderList);
    }

    /**
     * 指定ユーザーの注文履歴を取得する(正常系)
     */
    @Test
    void testFindByUserId() {
        List<Order> orderList = orderRepository.findByUserId(2);
        assertEquals(1, orderList.size());
    }

    /**
     * 指定ユーザーの注文履歴を取得する(異常系)
     */
    @Test
    void testFindByUserIdNoMatch() {
        List<Order> orderList = orderRepository.findByUserId(999);
        assertNotNull(orderList);
        assertTrue(orderList.isEmpty());
    }

    /**
     * 指定ユーザーの未確定注文の件数を取得(該当データが存在した場合)
     */
    @Test
    void testCountCartItemsByUserId() {
        Integer result = orderRepository.countCartItemsByUserId(2);
        assertEquals(1, result);
    }

    /**
     * 指定ユーザーの未確定注文の件数を取得(該当データが存在しなかった場合)
     */
    @Test
    void testCountCartItemsByUserIdNoMatch() {
        Integer result = orderRepository.countCartItemsByUserId(1);
        assertNotNull(result);
        assertEquals(0, result);
    }

    /**
     * レコードを追加
     */
    @Test
    void testOrder() {
        Order order = new Order();
        order.setUserId(1);
        order.setStatus(0); // 未入金
        order.setTotalPrice(3480);
        order.setDestinationName("田中一郎");
        order.setDestinationEmail("tanaka@example.com");
        order.setDestinationZipcode("1500001");
        order.setDestinationAddress("東京都渋谷区道玄坂1-2-3");
        order.setDestinationTel("080-1111-2222");
        LocalDateTime orderDateLdt = LocalDateTime.of(2025, 7, 1, 12, 0);
        order.setDeliveryTime(Timestamp.valueOf(orderDateLdt));
        order.setPaymentMethod(1);
        Integer generatedId = orderRepository.insert(order);

        assertNotNull(generatedId);
        assertEquals(generatedId, order.getId());
    }

    /**
     * 注文情報の更新
     */
    @Test
    void testUpdate() {
        Order order = new Order();
        order.setId(100);
        order.setUserId(2);
        order.setStatus(1);
        order.setTotalPrice(2000);
        order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
        order.setDestinationName("変更後太郎");
        order.setDestinationEmail("change@example.com");
        order.setDestinationZipcode("7654321");
        order.setDestinationAddress("東京都変更区");
        order.setDestinationTel("080-1234-5678");
        order.setDeliveryTime(new Timestamp(System.currentTimeMillis()));
        order.setPaymentMethod(2);

        orderRepository.update(order);
        String sql = "SELECT destination_name FROM orders WHERE id =:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", 100);
        String name = template.queryForObject(sql,
                param, String.class);
        assertEquals("変更後太郎", name);
    }

    /**
     * Total金額の更新
     */
    @Test
    void testUpdateTotalPrice() {
        orderRepository.updateTotlePrice(3000);
        String sql = "SELECT total_price FROM orders WHERE id =:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", 100);
        Integer price = template.queryForObject(sql,
                param, Integer.class);
        assertEquals(3000, price);
    }

    /**
     * 指定ユーザーの未確定注文（カート状態）のステータスをキャンセル（9）に更新する
     */
    @Test
    void testCancelOrdersByUserId() {
        orderRepository.cancelOrdersByUserId(2);
        String sql = "SELECT status FROM orders WHERE user_id =:userId AND status = 9";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", 2);
        Integer status = template.queryForObject(sql, param, Integer.class);
        assertEquals(9, status);
    }

    /**
     * 配達完了日時の更新
     */
    @Test
    void testUpdateCompletionTime() {
        Timestamp ts = Timestamp.valueOf("2025-07-01 10:00:00");
        orderRepository.updateCompletionTime(ts, 100, 4);

        String completionTimeSql = "SELECT completion_time FROM orders WHERE id =:id";
        SqlParameterSource param1 = new MapSqlParameterSource().addValue("id", 100);
        Timestamp result = template.queryForObject(completionTimeSql,
                param1, Timestamp.class);
        assertEquals(ts, result);

        String statusSql = "SELECT status FROM orders WHERE id =:id";
        SqlParameterSource param2 = new MapSqlParameterSource().addValue("id", 100);
        Integer status = template.queryForObject(statusSql,
                param2, Integer.class);
        assertEquals(ts, result);
        assertEquals(4, status);
    }
}
