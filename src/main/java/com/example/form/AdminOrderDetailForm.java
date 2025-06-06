package com.example.form;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AdminOrderDetailForm {

    // 配達完了日時
    @NotNull(message = "配達日時を入力してください")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}$", message = "配達日時を入力してください")
    private String completionTimestamp;
    private Integer status;
    private Integer id;

    public Timestamp getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            java.util.Date date = sdf.parse(completionTimestamp);
            Timestamp ts = new Timestamp(date.getTime());
            return ts;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    // public Integer getIntegerDeliveryTime() {
    //     return Integer.parseInt(completionTime);
    // }

    public String getCompletionTimestamp() {
        return completionTimestamp;
    }

    public void setCompletionTimestamp(String completionTimestamp) {
        this.completionTimestamp = completionTimestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdminOrderDetailForm [completionTimestamp=" + completionTimestamp + ", status="
                + status + "]";
    }

}
