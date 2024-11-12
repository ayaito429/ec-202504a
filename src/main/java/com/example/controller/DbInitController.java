package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/db")
public class DbInitController {
	@Autowired
	private JdbcTemplate template;

	@GetMapping("/init")
	public String init(String file, Model model) {
		String currentDir = System.getProperty("user.dir");
		System.out.println("Current Directory: " + currentDir);
		String message = "DB初期化失敗";
		try {
			String sql = new String(Files.readAllBytes(Paths.get("sql/" + file)), "UTF-8");
			template.execute(sql);
			message = "DB初期化成功";
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("message", message);
		return "finish";
	}
}
