package com.example.klue_sever.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class KeyboardCaseDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(max = 255, message = "이름은 255자를 초과할 수 없습니다.")
        private String name;

        private String description;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer id;
        private String name;
        private String description;
        private List<PlateDto.Response> plates;
        private List<PCBDto.Response> pcbs;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Integer id;
        private String name;
        private String description;
        private List<PlateDto.Response> plates;
        private List<PCBDto.Response> pcbs;
    }
} 