package com.nerdy.monologue.diary.domain.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class DiaryImageDTO {
    private List<MultipartFile> files;
}
