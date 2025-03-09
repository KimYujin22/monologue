/*
package com.nerdy.monologue.diary.service;

import com.nerdy.monologue.diary.domain.entity.Diary;
import com.nerdy.monologue.diary.domain.entity.DiaryImage;
import com.nerdy.monologue.diary.repository.DiaryRepository;
import com.nerdy.monologue.member.domain.entity.Member;
import com.nerdy.monologue.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    // Create
    @Override
    public Diary createDiary(Long memberId, String title, String content, List<String> images) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("No member found with id: " + memberId));

        Diary diary = new Diary(title, content, member);

        if (images != null) {
            for (String image : images) {
                diary.getImages().add(new DiaryImage(null, image, diary));
            }
        }

        return diaryRepository.save(diary);
    }

    @Override
    public Diary readDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("No diary found with id: " + diaryId));
    }

    @Override
    public Diary updateDiary(Long diaryId, Long memberId, String title, String content, List<String> images) {
        Diary diary = diaryRepository.findByIdAndMember(diaryId, new Member(memberId, null, null, null, null))
                .orElseThrow(() -> new EntityNotFoundException("No diary found with id: " + diaryId));

        diary.setTitle(title);
        diary.setContent(content);

        diary.getImages().clear();
        if (images != null) {
            images.forEach(image -> diary.getImages().add(new DiaryImage(null, image, diary)));
        }

        return diary;
    }

    @Override
    public void deleteDiary(Long diaryId, Long memberId) {
        Diary diary = diaryRepository.findByIdAndMember(diaryId, new Member(memberId, null, null, null, null))
                .orElseThrow(() -> new EntityNotFoundException("No diary found with id: " + diaryId));

        diaryRepository.delete(diary);
    }
}

 */
