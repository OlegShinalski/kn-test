package com.example.kntest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.util.StringUtils;

import com.example.kntest.model.CityDto;
import com.example.kntest.persistence.entity.City;
import com.example.kntest.persistence.repositary.CityRepository;
import com.example.kntest.transformer.CityMapper;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public Page<CityDto> getCities(int pageNo, int pageSize, String filter) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Order.asc("name").ignoreCase()));

        Page<City> cities = StringUtils.isEmpty(filter) ?
                cityRepository.findAll(pageable) :
                cityRepository.findByNameContainingIgnoreCase(pageable, filter);

        return cities.map(cityMapper::mapToDto);
    }

    public CityDto save(CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto, findOrInit(cityDto));
        City persisted = cityRepository.save(city);
        cityDto = cityMapper.mapToDto(persisted);
        return cityDto;
    }

    private City findOrInit(CityDto cityDto) {
        City city = new City();
        if (cityDto.getId() != null) {
            city = cityRepository.findById(cityDto.getId()).orElseThrow();
        }
        return city;
    }

    public CityDto getById(int id) {
        return cityMapper.mapToDto(cityRepository.findById(id).orElseThrow());
    }

}
