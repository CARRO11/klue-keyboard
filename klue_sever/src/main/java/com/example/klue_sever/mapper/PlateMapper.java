package com.example.klue_sever.mapper;

import com.example.klue_sever.dto.PlateDto;
import com.example.klue_sever.entity.Plate;
import com.example.klue_sever.entity.KeyboardCase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlateMapper {
    
    public Plate toEntity(PlateDto.Request dto, KeyboardCase keyboardCase) {
        return Plate.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .keyboardCase(keyboardCase)
                .build();
    }

    public PlateDto.Response toResponse(Plate entity) {
        return PlateDto.Response.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .keyboardCaseId(entity.getKeyboardCase().getId())
                .keyboardCaseName(entity.getKeyboardCase().getName())
                .build();
    }

    public PlateDto.DetailResponse toDetailResponse(Plate entity) {
        return PlateDto.DetailResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public List<PlateDto.Response> toResponseList(List<Plate> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 