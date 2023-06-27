package com.example.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.CartItem;
import com.example.domain.Topping;
import com.example.form.ItemCartInForm;

/**
 * カート内に商品を入れる際に使うservice
 * @author naramasato
 *
 */
@Service
@Transactional
public class CartService {
	
	public ItemCartInForm setupForm() {
		return new ItemCartInForm();
	}
	
	/**
	 * サイズに合わせてitemの金額を判定
	 * @param form 
	 * @return itemの金額
	 */
	public Integer getPriceSize(ItemCartInForm form) {
		if(form.getSize().equals("M")) {
			return form.getPriceM();
		}else {
			return form.getPriceL();
		}
	}
	
	/**
	 * 
	 * @param toppingList　applicationスコープ内に格納してあるトッピング一覧
	 * @param toppingIndex　formから送られてきたトッピングの情報
	 * @return トッピング一覧を格納したList　一覧がなければLinkedListで返す
	 * 
	 */
	public List<Topping> getToppingIndex(List<Topping> toppingList ,List<String> toppingIndex) {
		if(toppingIndex == null) {
			return new LinkedList<>();
		}
		List<Topping> toppings = new LinkedList<>();
		for(String index: toppingIndex) {
			Topping topping = toppingList.get(Integer.parseInt(index));
			toppings.add(topping);
		}
		return toppings;
	}
	
	//合計金額を計算するメソッド
	public Integer calcTotal(List<CartItem> totalPriceList) {
			
		Integer totalPrices=0;
		for(CartItem price:totalPriceList) {
			totalPrices += price.getSubTotal();
		}
		return totalPrices;
	}
}
