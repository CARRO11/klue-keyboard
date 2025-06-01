package com.example.klue_sever.mapper;

import com.example.klue_sever.dto.LubeDto;
import com.example.klue_sever.entity.Lube;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LubeMapper {
    
    public Lube toEntity(LubeDto.Request dto) {
        return Lube.builder()
                .name(dto.getName())
                .enddate(dto.getEnddate())
                .build();
    }

    public LubeDto.Response toResponse(Lube entity) {
        return LubeDto.Response.builder()
                .id(entity.getId())
                .name(entity.getName())
                .enddate(entity.getEnddate())
                .build();
    }

    public LubeDto.DetailResponse toDetailResponse(Lube entity) {
        return LubeDto.DetailResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .enddate(entity.getEnddate())
                .build();
    }

    public List<LubeDto.Response> toResponseList(List<Lube> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 