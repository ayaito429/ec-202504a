package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
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
	public void sendMail(String email) {
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setFrom("curry-admin@example.com");
		msg.setTo(email);
		msg.setSubject("注文完了！！！");// タイトルの設定
		msg.setText("ラクラクカリー より　注文完了"); // 本文の設定

		this.sender.send(msg);
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
	 * Total金額の更新
	 * 
	 * @param totlePrice
	 */
	public void updateTotlePrice(Integer totalPrice) {
		orderRepository.updateTotlePrice(totalPrice);
	}

	/**
	 * カート内の商品の重複
	 * 
	 * @param quantity
	 */
	public void addQuantity(Integer quantity) {
		orderItemRepository.addQuantity(quantity);
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
}
