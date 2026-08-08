package com.example.menulogapi.controller;

import com.example.menulogapi.entity.Diary;
import com.example.menulogapi.entity.User;
import com.example.menulogapi.repository.DiaryRepository;
import com.example.menulogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. ログイン中のユーザーの日記一覧のみ取得
    @GetMapping
    public List<Diary> getMyDiaries(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return diaryRepository.findByUserId(user.getId());
    }

    // 2. 日記の新規作成・更新
    @PostMapping
    public Diary saveDiary(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        LocalDate date = LocalDate.parse(String.valueOf(request.get("date")));
        String content = String.valueOf(request.get("content"));

        Diary diary = diaryRepository.findByUserIdAndDate(user.getId(), date)
                .orElse(new Diary());

        diary.setUser(user);
        diary.setDate(date);
        diary.setContent(content);

        return diaryRepository.save(diary);
    }

    // 3. 日記の削除
    @DeleteMapping("/{date}")
    public ResponseEntity<?> deleteDiary(@PathVariable String date, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        LocalDate localDate = LocalDate.parse(date);

        Diary diary = diaryRepository.findByUserIdAndDate(user.getId(), localDate).orElse(null);
        if (diary == null) {
            return ResponseEntity.notFound().build();
        }

        diaryRepository.delete(diary);
        return ResponseEntity.ok().build();
    }
}