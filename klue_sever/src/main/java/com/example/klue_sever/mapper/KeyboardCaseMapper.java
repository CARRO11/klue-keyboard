package com.example.klue_sever.mapper;

import com.example.klue_sever.dto.KeyboardCaseDto;
import com.example.klue_sever.entity.KeyboardCase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeyboardCaseMapper {
    
    public KeyboardCase toEntity(KeyboardCaseDto.Request dto) {
        return KeyboardCase.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public KeyboardCaseDto.Response toResponse(KeyboardCase entity) {
        return KeyboardCaseDto.Response.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public KeyboardCaseDto.DetailResponse toDetailResponse(KeyboardCase entity) {
        return KeyboardCaseDto.DetailResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public List<KeyboardCaseDto.Response> toResponseList(List<KeyboardCase> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 