package com.example.klue_sever.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto {
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    
    @NotBlank(message = "작성자는 필수입니다.")
    @Size(max = 100, message = "작성자는 100자를 초과할 수 없습니다.")
    private String author;
    
    @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다.")
    private String category;
    
    private Boolean isPinned = false;
} 