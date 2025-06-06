package com.example.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.domain.User;
import com.example.domain.Order;

/**
 * 管理者の注文一覧画面に検索結果を表示させるためのリポジトリ
 * 
 * @author aya_ito
 */
@Repository
public class AdminOrderRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Order> ORDER_WITH_USER_ROW_MAPPER = (rs, i) -> {
        Order order = new Order();
        order.setId(rs.getInt("o_id"));
        order.setUserId(rs.getInt("o_user_id"));
        order.setStatus(rs.getInt("o_status"));
        order.setTotalPrice(rs.getInt("o_total_price"));
        order.setOrderDate(rs.getDate("o_order_date"));
        order.setDestinationName(rs.getString("o_destination_name"));
        order.setDestinationEmail(rs.getString("o_destination_email"));
        order.setDestinationZipcode(rs.getString("o_destination_zipcode"));
        order.setDestinationAddress(rs.getString("o_destination_address"));
        order.setDestinationTel(rs.getString("o_destination_tel"));
        order.setDeliveryTime(rs.getTimestamp("o_delivery_time"));
        order.setCompletionTime(rs.getTimestamp("o_completion_time"));
        order.setPaymentMethod(rs.getInt("o_payment_method"));

        User user = new User();
        user.setId(rs.getInt("u_id"));
        user.setName(rs.getString("u_name"));
        user.setPassword(rs.getString("u_password"));
        user.setEmail(rs.getString("u_email"));
        user.setZipcode(rs.getString("u_zipcode"));
        user.setAddress(rs.getString("u_address"));
        user.setTelephone(rs.getString("u_telephone"));

        order.setUser(user);
        return order;
    };

    /**
     * 入力された条件で検索する
     * 
     * @param searchField 検索項目
     * @param searchValue 検索値
     * @return 検索結果
     */
    public List<Order> searchOrders(String searchField, String searchValue) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.id AS o_id, o.user_id AS o_user_id, o.status AS o_status, o.total_price AS o_total_price, " +
                        "o.order_date AS o_order_date, o.destination_name AS o_destination_name, o.destination_email AS o_destination_email, " +
                        "o.destination_zipcode AS o_destination_zipcode, o.destination_address AS o_destination_address, o.destination_tel AS o_destination_tel, " +
                        "o.delivery_time AS o_delivery_time, o.payment_method AS o_payment_method, o.completion_time AS o_completion_time, " +
                        "u.id AS u_id, u.name AS u_name, u.password AS u_password, u.email As u_email, u.zipcode AS u_zipcode, u.address AS u_address, u.telephone AS u_telephone FROM orders o JOIN users u ON u.id = o.user_id WHERE ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        // 検索条件がなければ空リスト返却
        if (searchValue == null || searchValue.isEmpty()) {
            return List.of();
        }

        switch (searchField) {
            // ID
            case "id":
                sql.append("o.id = :o_id");
                params.addValue("o_id", Integer.parseInt(searchValue));
                break;

            // 注文者
            case "name":
                sql.append("u.name LIKE :u_name");
                params.addValue("u_name", "%" + searchValue + "%");
                break;
            // 電話番号
            case "telephone":
                sql.append("u.telephone LIKE :u_telephone");
                params.addValue("u_telephone", "%" + searchValue + "%");
                break;
            // ステータス
            case "status":
                sql.append("o.status = :o_status");
                params.addValue("o_status", Integer.parseInt(searchValue));
                break;

            // 支払方法
            case "payMethod":
                sql.append("o.payment_method =:o_payment_method");
                params.addValue("o_payment_method", Integer.parseInt(searchValue));
                break;

            default:
                break;
        }
        sql.append(" ORDER BY o.id");
        return template.query(sql.toString(), params, ORDER_WITH_USER_ROW_MAPPER);
    }

    /**
     * オーバーロードした検索メソッド
     * 
     * @param searchField      検索項目
     * @param searchValueStart 開始日
     * @param searchValueEnd   終了日
     * @return 検索結果
     */
    public List<Order> searchOrders(String searchField, String searchValueStart, String searchValueEnd) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.id AS o_id, o.user_id AS o_user_id, o.status AS o_status, o.total_price AS o_total_price, " +
                        "o.order_date AS o_order_date, o.destination_name AS o_destination_name, o.destination_email AS o_destination_email, " +
                        "o.destination_zipcode AS o_destination_zipcode, o.destination_address AS o_destination_address, o.destination_tel AS o_destination_tel, " +
                        "o.delivery_time AS o_delivery_time, o.payment_method AS o_payment_method, o.completion_time AS o_completion_time," +
                        "u.id AS u_id, u.name AS u_name, u.password AS u_password, u.email As u_email, u.zipcode AS u_zipcode, u.address AS u_address, u.telephone AS u_telephone FROM orders o JOIN users u ON u.id = o.user_id WHERE ");
        MapSqlParameterSource params = new MapSqlParameterSource();

        boolean hasStart = searchValueStart != null && !searchValueStart.isEmpty();
        boolean hasEnd = searchValueEnd != null && !searchValueEnd.isEmpty();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        switch (searchField) {
            // 注文日
            case "orderDate":

                if (hasStart && hasEnd) {
                    sql.append("o.order_date >= :startDate AND o.order_date < :endDate");
                    params.addValue("startDate", LocalDate.parse(searchValueStart, formatter))
                            .addValue("endDate", LocalDate.parse(searchValueEnd, formatter));
                } else if (hasStart) {
                    sql.append("o.order_date >= :startDate");
                    params.addValue("startDate", LocalDate.parse(searchValueStart, formatter));
                } else if (hasEnd) {
                    sql.append("o.order_date < :endDate");
                    params.addValue("endDate", LocalDate.parse(searchValueEnd, formatter));
                }
                break;

            // 配達希望日時
            case "deliveryTime":

                if (hasStart && hasEnd) {
                    sql.append("o.delivery_time >= :startDate AND o.delivery_time < :endDate");
                    params.addValue("startDate", LocalDate.parse(searchValueStart, formatter))
                            .addValue("endDate", LocalDate.parse(searchValueEnd, formatter));
                } else if (hasStart) {
                    sql.append("o.delivery_time >= :startDate");
                    params.addValue("startDate", LocalDate.parse(searchValueStart, formatter));
                } else if (hasEnd) {
                    sql.append("o.delivery_time < :endDate");
                    params.addValue("endDate", LocalDate.parse(searchValueEnd, formatter));
                }
                break;

            // 配達完了日時
            case "completionTime":

                if (hasStart && hasEnd) {
                    sql.append("o.completion_time >= :startDate AND o.completion_time < :endDate");
                    params.addValue("startDate", LocalDate.parse(searchValueStart, formatter))
                            .addValue("endDate", LocalDate.parse(searchValueEnd, formatter));
                } else if (hasStart) {
                    sql.append("o.completion_time >= :startDate");
                    params.addValue("startDate", LocalDate.parse(searchValueStart, formatter));
                } else if (hasEnd) {
                    sql.append("o.completion_time < :endDate");
                    params.addValue("endDate", LocalDate.parse(searchValueEnd, formatter));
                }
                break;
        }
        sql.append(" ORDER BY o.id");
        return template.query(sql.toString(), params, ORDER_WITH_USER_ROW_MAPPER);

    }
}
