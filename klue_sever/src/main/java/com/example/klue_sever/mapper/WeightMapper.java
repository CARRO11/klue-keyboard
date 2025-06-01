package com.example.klue_sever.mapper;

import com.example.klue_sever.dto.WeightDto;
import com.example.klue_sever.entity.Weight;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeightMapper {
    
    public Weight toEntity(WeightDto.Request dto) {
        return Weight.builder()
                .material(dto.getMaterial())
                .link(dto.getLink())
                .build();
    }

    public WeightDto.Response toResponse(Weight entity) {
        return WeightDto.Response.builder()
                .id(entity.getId())
                .material(entity.getMaterial())
                .link(entity.getLink())
                .build();
    }

    public WeightDto.DetailResponse toDetailResponse(Weight entity) {
        return WeightDto.DetailResponse.builder()
                .id(entity.getId())
                .material(entity.getMaterial())
                .link(entity.getLink())
                .build();
    }

    public List<WeightDto.Response> toResponseList(List<Weight> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 