package com.example.kntest.transformer;

import org.springframework.stereotype.Service;

import com.example.kntest.model.CityDto;
import com.example.kntest.persistence.entity.City;
import com.google.common.base.Preconditions;

@Service
public class CityMapper {

    public City mapToEntity(CityDto dto, City entity) {
        Preconditions.checkNotNull(entity);
        if (dto != null) {
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setPictureUrl(dto.getPictureUrl());
        }
        return entity;
    }

    public CityDto mapToDto(City entity) {
        return entity == null ?
                new CityDto() :
                CityDto.builder().id(entity.getId()).name(entity.getName()).pictureUrl(entity.getPictureUrl()).build();
    }

}
