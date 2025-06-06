package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;

import com.example.domain.User;
import com.example.repository.AdminOrderRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

/**
 * orderに関わる内容を行う
 * 
 * @author naramasato
 *
 */
@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderToppingRepository orderToppingRepository;

	@Autowired
	private MailSender sender;

	@Autowired
	private AdminOrderRepository adminOrderRepository;

	@Value("${custom.mail.from}")
	private String from;

	@Value("${custom.mail.subject}")
	private String subject;

	@Value("${custom.mail.template}")
	private String templatePath;

	/**
	 * 注文詳細一件を取得
	 * 
	 * @param orderId
	 * @return
	 */
	public List<Order> orderLoad(Integer orderId) {
		return orderRepository.orderLoad(orderId);
	}

	/**
	 * 注文詳細全件を取得
	 * 
	 * @param id
	 * @return
	 */
	public List<Order> findByOrder(Integer id) {
		return orderRepository.findByOrdertable(id);
	}

	/**
	 * 注文情報を取得
	 * 
	 * @param userId ユーザーID
	 * @return 注文情報
	 */
	public List<Order> findByUserId(Integer userId) {
		return orderRepository.findByUserId(userId);
	}

	/**
	 * 注文前のorderを取得
	 * 
	 * @param userId ユーザーID
	 * @return 注文情報
	 */
	public List<Order> findByStatus(Integer userId, Integer status) {
		return orderRepository.findByStatus(userId, status);
	}

	/**
	 * orderドメインに足りない物をセット
	 * 
	 * @param order
	 */
	// TODO
	public Integer insert(Order order) {
		return orderRepository.insert(order);
	}

	/**
	 * order_itemsテーブルにINSERTするメゾット
	 * 
	 * @param orderId
	 */
	public void insertOrderItem(OrderItem orderItem) {
		Integer orderItemId = orderItemRepository.order(orderItem);
		for (OrderTopping orderTopping : orderItem.getOrderTopping()) {
			orderTopping.setOrderItemId(orderItemId);
			orderToppingRepository.insert(orderTopping);
		}
	}

	/**
	 * 注文時の金額を登録
	 * 
	 * @param id 注文商品ID
	 */
	public void insertItemPrice(OrderItem orderItem) {
		orderItemRepository.insertPrice(orderItem);
	}

	/**
	 * 注文時の金額を登録
	 * 
	 * @param orderTopping トッピング情報
	 */
	public void insertToppingPrice(OrderTopping orderTopping) {
		orderToppingRepository.insertPrice(orderTopping);
	}

	/**
	 * 注文情報の更新
	 * 
	 * @param order 注文情報
	 */
	public void update(Order order) {
		orderRepository.update(order);
	}

	/**
	 * 引数で受け取ったemailに完了メールを送付
	 * 
	 * @param email
	 */
	public void sendMail(String email, User user, List<OrderItem> orderItemList, Integer totalPrice) {

		String body;
		try {
			body = generateMailBody(user, orderItemList, totalPrice);
		} catch (IOException e) {
			body = "ご注文ありがとうございます。\n" +
					"メール送信システムにエラーが発生しました。\n" +
					"注文処理は完了していますがご心配の方は、\n" +
					"カスタマーサポート(0123-456-789)までお問い合わせください。";
		}

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(email);
		msg.setSubject(subject);
		msg.setText(body);
		sender.send(msg);
	}

	/**
	 * メール本文を生成するメソッド
	 * 
	 * @param user         ユーザー情報
	 * @param cartItemList カート内の商品リスト
	 * @param totalPrice   合計金額
	 * @return 生成されたメール本文
	 * @throws IOException
	 */
	private String generateMailBody(User user, List<OrderItem> orderItemList, int totalPrice) throws IOException {
		String template = Files.readString(Path.of(templatePath));

		String orderSummary = orderItemList.stream()
				.map(item -> {
					StringBuilder sb = new StringBuilder();
					sb.append(item.getItem().getName()) // item名は item から取得
							.append("（").append(item.getSize()).append("）×").append(item.getQuantity()).append("個");

					if (item.getOrderTopping() != null && !item.getOrderTopping().isEmpty()) {
						String toppings = item.getOrderTopping().stream()
								.map(topping -> topping.getTopping().getName()) // トッピング名取得
								.collect(Collectors.joining("・"));
						sb.append("\nトッピング: ").append(toppings);
					}
					return sb.toString();
				})
				.collect(Collectors.joining("\n"));

		return template
				.replace("{userName}", user.getName())
				.replace("{orderSummary}", orderSummary)
				.replace("{totalPrice}", String.valueOf(totalPrice));
	}

	/**
	 * カートの商品情報を削除
	 * 
	 * @param id 商品ID
	 */
	public void deleteForCartItem(Integer id) {
		orderToppingRepository.delete(id);
		orderItemRepository.delete(id);
	}

	/**
	 * カート内の商品の重複
	 * 
	 * @param quantity
	 */
	public void addQuantity(Integer orderItemId, Integer quantity) {
		orderItemRepository.addQuantity(orderItemId, quantity);
	}

	/**
	 * 指定ユーザーのカートに商品（未確定の注文status=0）があるか判定する.
	 * 
	 * @param userId ユーザーID
	 * @return true：商品あり（カートに未確定注文がある）、false：なし
	 */
	public boolean hasCartItems(Integer userId) {
		return orderRepository.countCartItemsByUserId(userId) > 0;
	}

	/**
	 * 指定したユーザーの全てのカートをキャンセルする
	 * 
	 * @param userId
	 */
	public void cancelAllCarts(Integer userId) {
		orderRepository.cancelOrdersByUserId(userId);
	}

	/**
	 * 注文情報を検索
	 * 
	 * @param searchField 検索項目
	 * @param searchValue 検索値
	 * @return 検索結果
	 */
	public List<Order> searchOrders(String searchField, String searchValue) {
		return adminOrderRepository.searchOrders(searchField, searchValue);
	}

	/**
	 * オーバーロードした注文情報検索
	 * 
	 * @param searchField 検索項目
	 * @param searchValue 検索値
	 * @return 検索結果
	 */
	public List<Order> searchOrders(String searchField, String searchValue1, String searchValue2) {
		return adminOrderRepository.searchOrders(searchField, searchValue1, searchValue2);
	}

	/**
	 * ページング機能
	 * 
	 * @param page
	 * @param size     表示させる注文数
	 * @param itemList
	 * @return
	 */
	public Page<Order> showListPaging(int page, int size, List<Order> orderList) {
		// 表示させたいページ数を-1しなければうまく動かない
		page--;
		// どの注文から表示させるかと言うカウント値
		int startItemCount = page * size;
		// 絞り込んだ後の注文リストが入る変数
		List<Order> list;

		if (orderList.size() < startItemCount) {
			// (ありえないが)もし表示させたい従業員カウントがサイズよりも大きい場合は空のリストを返す
			list = Collections.emptyList();
		} else {
			// 該当ページに表示させる注文一覧を作成
			int toIndex = Math.min(startItemCount + size, orderList.size());
			list = orderList.subList(startItemCount, toIndex);
		}

		// 上記で作成した該当ページに表示させる注文一覧をページングできる形に変換して返す
		Page<Order> orderPage = new PageImpl<Order>(list, PageRequest.of(page, size), orderList.size());
		return orderPage;
	}

	/**
	 * 配達完了日時の更新
	 * 
	 * @param completionTime 配達完了日時
	 */
	public void updateCompletionTime(Timestamp completionTime, Integer id, Integer status) {
		orderRepository.updateCompletionTime(completionTime, id, status);
	}
}
