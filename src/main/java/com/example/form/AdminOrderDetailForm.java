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
	private String completionTime;
    private Integer status;

    public Timestamp getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		try {
			java.util.Date date = sdf.parse(completionTime);
			Timestamp ts = new Timestamp(date.getTime());
			return ts;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Integer getIntegerDeliveryTime() {
		return Integer.parseInt(completionTime);
	}

    
    public String getCompletionTime() {
        return completionTime;
    }
    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AdminOrderDetailForm [completionTime=" + completionTime + ", status="
                + status + "]";
    }
    
}
