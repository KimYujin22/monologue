/*
package com.nerdy.monologue.diary.controller;

import com.nerdy.monologue.diary.domain.entity.Diary;
import com.nerdy.monologue.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Diary> createDiary(@RequestParam Long memberId, @RequestParam String title, @RequestParam String content, @RequestParam(required = false) List<String> images) {
        Diary diary = diaryService.createDiary(memberId, title, content, images);
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<Diary> readDiary(@PathVariable Long diaryId) {
        return ResponseEntity.ok(diaryService.readDiary(diaryId));
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Long diaryId, @RequestParam Long memberId, @RequestParam String title, @RequestParam String content, @RequestParam(required = false) List<String> images) {
        Diary updatediary = diaryService.updateDiary(diaryId, memberId, title, content, images);
        return ResponseEntity.ok(updatediary);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId, Long memberId) {
        diaryService.deleteDiary(diaryId, memberId);
        return ResponseEntity.noContent().build();
    }



}

 */

