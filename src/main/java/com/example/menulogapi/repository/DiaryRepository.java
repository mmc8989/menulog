package com.example.menulogapi.repository;

import com.example.menulogapi.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // ログイン中ユーザーの日記一覧を取得
    List<Diary> findByUserId(Long userId);

    // ログイン中ユーザーの特定の日付の日記を取得
    Optional<Diary> findByUserIdAndDate(Long userId, LocalDate date);
}