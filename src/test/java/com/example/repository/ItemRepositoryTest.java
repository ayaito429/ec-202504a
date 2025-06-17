package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.domain.Item;
import com.example.domain.OrderItem;

/**
 * ItemRepositoryのテストクラス
 * 
 * @author aya_ito
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private NamedParameterJdbcTemplate template;


	@BeforeAll
    void setUp() {
        String deleteSql = "DELETE FROM items WHERE name = :name";
        MapSqlParameterSource param1 = new MapSqlParameterSource().addValue("name", "グリーンカレー");
        template.update(deleteSql, param1);
		String updateSql = "UPDATE item_stocks SET stock =:stock WHERE item_id = :itemId";
        MapSqlParameterSource param2 = new MapSqlParameterSource().addValue("itemId", 5).addValue("stock", 100);
        template.update(updateSql, param2);
    }

	/**
	 * 全件取得
	 */
	@Test
	void test_findAll() {
		List<Item> itemList = itemRepository.findAll();
		assertNotNull(itemList);
		assertEquals(18, itemList.size());
	}

	/**
	 * 商品名から検索（正常系）
	 */
	@Test
	void test_findByName() {
		List<Item> itemList = itemRepository.findByName("カレー");
		assertEquals(12, itemList.size());
	}

	/**
	 * 商品名から検索（異常系）
	 * 存在しない商品名で検索
	 */
	@Test
	void test_findByName_noMatch() {
		List<Item> itemList = itemRepository.findByName("存在しない商品名");
		assertNull(itemList);
	}

	/**
	 * 商品詳細のSQLを発行
	 */
	@Test
	void test_showItemDetail() {
		Item item = itemRepository.showItemDetail(1);
		assertEquals("カツカレー", item.getName());
	}

	/**
	 * 商品の追加
	 */
	@Test
	void test_insert() {
		Item newItem = new Item();
		newItem.setName("グリーンカレー");
		newItem.setDescription("");
		newItem.setPriceM(1400);
		newItem.setPriceL(2800);
		newItem.setImagePath("18.jpg");
		newItem.setDeleted(false);

		itemRepository.insert(newItem);

		List<Item> results = itemRepository.findByName("グリーンカレー");
		assertEquals(1400, results.get(0).getPriceM());
		assertEquals(2800, results.get(0).getPriceL());
		assertFalse(results.get(0).getDeleted());
	}

	/**
	 * 商品の名前をすべて返す
	 */
	@Test
	void test_getAllNames() {
		List<String> itemList = itemRepository.getAllNames();
		assertNotNull(itemList);
		assertEquals(19, itemList.size());
	}

	/**
	 * 特定のidが存在するか(真)
	 */
	 @Test
    void 存在するIDを指定した場合_trueが返ること() {
        boolean result = itemRepository.existsById(1);
        assertTrue(result);
    }

	/**
	 * 特定のidが存在するか(偽)
	 */
    @Test
    void 存在しないIDを指定した場合_falseが返ること() {
        boolean result = itemRepository.existsById(999);
        assertFalse(result);
    }

	/**
	 * 在庫情報の取得
	 */
	@Test
	void test_findForStockById() {
		Integer results = itemRepository.findForStockById(3);
		assertEquals(92, results);
	}

	/**
	 * 在庫情報の更新
	 */
	@Test
	void test_updateStock() {
		OrderItem orderItem = new OrderItem();
        orderItem.setItemId(5);     
        orderItem.setQuantity(5);   

		itemRepository.updateStock(orderItem);

		Integer results = itemRepository.findForStockById(5);
		assertEquals(95, results);
	}
}
