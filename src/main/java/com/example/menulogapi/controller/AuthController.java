package com.example.menulogapi.controller;

import com.example.menulogapi.entity.User;
import com.example.menulogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 新規ユーザー登録 API
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        String username = String.valueOf(request.get("username"));
        String password = String.valueOf(request.get("password"));

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("このユーザー名は既に使用されています。");
        }

        User user = new User();
        user.setUsername(username);
        // パスワードを暗号化して保存
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok("ユーザー登録が完了しました！");
    }

    // ログイン確認 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = String.valueOf(request.get("username"));
        String password = String.valueOf(request.get("password"));

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("ユーザー名またはパスワードが間違っています。");
        }

        return ResponseEntity.ok(Map.of("message", "ログイン成功", "username", username));
    }
}