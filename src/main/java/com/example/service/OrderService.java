package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.CustomUserDetails;
import com.example.domain.CartItem;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.User;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

import jakarta.servlet.http.HttpSession;

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
	private HttpSession session;

	@Autowired
	private MailSender sender;

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
	 * orderドメインに足りない物をセット
	 * 
	 * @param order
	 */
	// TODO
	public void order(Order order) {
		order.setStatus(paymentMethodJudge(order));
		order.setUserId(getUserId());
		Integer orderId = orderRepository.insert(order);
		insertOrderItem(orderId);

	}

	/**
	 * statusを判別するメゾット
	 * 
	 * @param order
	 * @return statusを整数で返す
	 */
	public Integer paymentMethodJudge(Order order) {
		if (order.getPaymentMethod() == 1) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * ユーザーのIdを返すメゾット
	 * 
	 * @return userId
	 */
	public Integer getUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof CustomUserDetails) {
			return ((CustomUserDetails) principal).getUserId();
		} else {
			throw new IllegalStateException("Authenticated user not found");
		}
	}

	/**
	 * order_itemsテーブルにINSERTするメゾット
	 * 
	 * @param orderId
	 */
	private void insertOrderItem(Integer orderId) {
		@SuppressWarnings("unchecked")
		List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("cartItemList");
		for (CartItem cartItem : cartItemList) {
			OrderItem orderItem = new OrderItem();
			BeanUtils.copyProperties(cartItem, orderItem);

			orderItem.setOrderId(orderId);

			InsertOrdertopping(orderItem, cartItem.getToppingList());
		}
	}

	/**
	 * order_toppingsテーブルにセット
	 * 
	 * @param orderItemId 注文商品の主キー
	 * @param toppingList 注文商品が持っているtoppingList
	 */
	private void InsertOrdertopping(OrderItem orderItem, List<Topping> toppingList) {
		Integer orderId = orderItemRepository.order(orderItem);
		for (Topping topping : toppingList) {
			OrderTopping orderTopping = new OrderTopping();
			orderTopping.setOrderItemId(orderId);
			orderTopping.setToppingId(topping.getId());
			orderTopping.setPrice(orderTopping.getSubTotle(orderItem.getSize(), orderItem.getQuantity()));
			orderToppingRepository.insert(orderTopping);
		}
	}

	/**
	 * 引数で受け取ったemailに完了メールを送付
	 * 
	 * @param email
	 */
    public void sendMail(String email){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user;

		if (principal instanceof CustomUserDetails) {
			user = ((CustomUserDetails) principal).getUser();
		} else {
			throw new IllegalStateException("User not authenticated");
		}
		List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("cartItemList");
		Integer totalPrice = (Integer) session.getAttribute("totalPrice");

		String body;
		try {
			body = generateMailBody(user, cartItemList, totalPrice);
		} catch (IOException e) {
			body = "ご注文ありがとうございます。\n" +
				   "メール送信システムにエラーが発生しました\n" +
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
	 * @param user ユーザー情報
	 * @param cartItemList カート内の商品リスト
	 * @param totalPrice 合計金額
	 * @return 生成されたメール本文
	 * @throws IOException 
	 */
	private String generateMailBody(User user, List<CartItem> cartItemList, int totalPrice) throws IOException {
		String template = Files.readString(Path.of(templatePath));

		String orderSummary = cartItemList.stream()
        .map(item -> {
            StringBuilder sb = new StringBuilder();
            sb.append(item.getName())
              .append("（").append(item.getSize()).append("）×").append(item.getQuantity()).append("個");

            if (item.getToppingList() != null && !item.getToppingList().isEmpty()) {
                String toppings = item.getToppingList().stream()
                    .map(Topping::getName)
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
}
