package com.example.klue_sever.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WeightDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "재질은 필수 입력값입니다.")
        @Size(max = 255, message = "재질은 255자를 초과할 수 없습니다.")
        private String material;

        @Size(max = 255, message = "링크는 255자를 초과할 수 없습니다.")
        private String link;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer id;
        private String material;
        private String link;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Integer id;
        private String material;
        private String link;
        private Response materialLink;
    }
} 