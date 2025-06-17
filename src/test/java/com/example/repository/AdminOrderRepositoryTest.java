package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Order;

/**
 * AdminOrderRepositoryのテストクラス
 * 
 * @author aya_ito
 */
@SpringBootTest
public class AdminOrderRepositoryTest {

    @Autowired
    private AdminOrderRepository adminOrderRepository;

    /**
     * 注文IDで検索
     */
    @Test
    void testSearchOrders_byId() {
        List<Order> results = adminOrderRepository.searchOrders("id", "1");
        assertEquals(1, results.size());
        assertEquals("山田太郎", results.get(0).getDestinationName());
    }

    /**
     * 注文者で検索（正常系）
     */
    @Test
    void testSearchOrders_byName() {
        List<Order> results = adminOrderRepository.searchOrders("name", "テスト");
        assertTrue(results.size() >= 1);
    }

    /**
     * 注文者で検索（異常系）
     */
    @Test
    void testSearchOrders_byName_noMatch() {
        List<Order> results = adminOrderRepository.searchOrders("name", "存在しない名前");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * 電話番号で検索（正常系）
     */
    @Test
    void testSearchOrders_byTelephone() {
        List<Order> results = adminOrderRepository.searchOrders("telephone", "090");
        assertTrue(results.size() >= 1);
    }

    /**
     * 電話番号で検索（異常系）
     */
    @Test
    void testSearchOrders_byTelephone_noMatch() {
        List<Order> results = adminOrderRepository.searchOrders("telephone", "存在しない番号");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * ステータスで検索
     */
    @Test
    void testSearchOrders_byStatus() {
        List<Order> results = adminOrderRepository.searchOrders("status", "1");
        assertNotNull(results);
    }

    /**
     * 支払方法で検索
     */
    @Test
    void testSearchOrders_byPayMethod() {
        List<Order> results = adminOrderRepository.searchOrders("payMethod", "1");
        assertNotNull(results);
    }

    /**
     * 注文日で検索
     * 開始日と終了日を入力
     */
    @Test
    void testSearchOrders_byOrderDate() {
        List<Order> results = adminOrderRepository.searchOrders("orderDate", "2025-04-01", "2025-06-12");
        assertNotNull(results);
    }

    /**
     * 注文日で検索
     * 開始日のみを入力
     */
    @Test
    void testSearchOrders_byOrderDate_start() {
        List<Order> results = adminOrderRepository.searchOrders("orderDate", "2025-04-01", "");
        assertNotNull(results);
    }

    /**
     * 注文日で検索
     * 終了日のみを入力
     */
    @Test
    void testSearchOrders_byOrderDate_end() {
        List<Order> results = adminOrderRepository.searchOrders("orderDate", "", "2025-06-12");
        assertNotNull(results);
    }

    /**
     * 注文日で検索（異常系）
     * 未来の日付を指定
     */
    @Test
    void testSearchOrders_byOrderDate_noMatch() {
        List<Order> results = adminOrderRepository.searchOrders("orderDate", "2026-06-12", "");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * 注文日で検索（異常系）
     * nullで検索
     */
    @Test
    void testSearchOrders_byOrderDate_null() {
        List<Order> results = adminOrderRepository.searchOrders("orderDate", "", "");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * 配達希望日で検索
     * 開始日と終了日を入力
     */
    @Test
    void testSearchOrders_byDeliveryTime() {
        List<Order> results = adminOrderRepository.searchOrders("deliveryTime", "2025-04-01", "2025-06-12");
        assertNotNull(results);
    }

    /**
     * 配達希望日で検索
     * 開始日のみを入力
     */
    @Test
    void testSearchOrders_byDeliveryTime_start() {
        List<Order> results = adminOrderRepository.searchOrders("deliveryTime", "2025-04-01", "");
        assertNotNull(results);
    }

    /**
     * 配達希望日で検索
     * 終了日のみを入力
     */
    @Test
    void testSearchOrders_byDeliveryTime_end() {
        List<Order> results = adminOrderRepository.searchOrders("deliveryTime", "", "2025-06-12");
        assertNotNull(results);
    }

    /**
     * 配達希望日で検索（異常系）
     * 未来の日付を指定
     */
    @Test
    void testSearchOrders_byDeliveryTime_noMatch() {
        List<Order> results = adminOrderRepository.searchOrders("deliveryTime", "2026-06-12", "");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * 配達希望日で検索（異常系）
     * nullで検索
     */
    @Test
    void testSearchOrders_byDeliveryTime_null() {
        List<Order> results = adminOrderRepository.searchOrders("deliveryTime", "", "");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * 配達完了日で検索
     * 開始日と終了日を入力
     */
    @Test
    void testSearchOrders_byCompletionTime() {
        List<Order> results = adminOrderRepository.searchOrders("completionTime", "2025-04-01", "2025-06-12");
        assertNotNull(results);
    }

    /**
     * 配達完了日で検索
     * 開始日のみを入力
     */
    @Test
    void testSearchOrders_byCompletionTime_start() {
        List<Order> results = adminOrderRepository.searchOrders("completionTime", "2025-04-01", "");
        assertNotNull(results);
    }

    /**
     * 配達完了日で検索
     * 終了日のみを入力
     */
    @Test
    void testSearchOrders_byCompletionTime_end() {
        List<Order> results = adminOrderRepository.searchOrders("completionTime", "", "2025-06-12");
        assertNotNull(results);
    }

    /**
     * 配達完了日で検索（異常系）
     * 未来の日付を指定
     */
    @Test
    void testSearchOrders_byCompletionTime_noMatch() {
        List<Order> results = adminOrderRepository.searchOrders("completionTime", "2026-06-12", "");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * 配達完了日で検索（異常系）
     * nullで検索
     */
    @Test
    void testSearchOrders_byCompletionTime_null() {
        List<Order> results = adminOrderRepository.searchOrders("completionTime", "", "");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

}
