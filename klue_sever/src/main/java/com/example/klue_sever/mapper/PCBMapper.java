package com.example.klue_sever.mapper;

import com.example.klue_sever.dto.PCBDto;
import com.example.klue_sever.entity.PCB;
import com.example.klue_sever.entity.KeyboardCase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PCBMapper {
    
    public PCB toEntity(PCBDto.Request dto, KeyboardCase keyboardCase) {
        return PCB.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .keyboardCase(keyboardCase)
                .build();
    }

    public PCBDto.Response toResponse(PCB entity) {
        return PCBDto.Response.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .keyboardCaseId(entity.getKeyboardCase().getId())
                .keyboardCaseName(entity.getKeyboardCase().getName())
                .build();
    }

    public PCBDto.DetailResponse toDetailResponse(PCB entity) {
        return PCBDto.DetailResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public List<PCBDto.Response> toResponseList(List<PCB> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 