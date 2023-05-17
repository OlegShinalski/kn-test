package com.example.kntest.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.kntest.model.CityDto;
import com.example.kntest.persistence.entity.City;
import com.example.kntest.persistence.repositary.CityRepository;
import com.example.kntest.transformer.CityMapper;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @InjectMocks
    private CityService cityService;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CityMapper cityMapper;

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldSaveNewCity() {
        CityDto cityDto = CityDto.builder().build();
        CityDto persistedDto = CityDto.builder().id(123).build();
        City city = City.builder().build();
        City parsistedCity = City.builder().id(123).build();

        given(cityMapper.mapToEntity(eq(cityDto), any(City.class)))
                .willReturn(city);
        given(cityMapper.mapToDto(parsistedCity))
                .willReturn(persistedDto);
        given(cityRepository.save(city))
                .willReturn(parsistedCity);

        CityDto result = cityService.save(cityDto);

        verify(cityRepository, never()).findById(any());
        verify(cityMapper).mapToEntity(eq(cityDto), any(City.class));
        verify(cityRepository).save(city);
        verify(cityMapper).mapToDto(parsistedCity);
        assertThat(result).isEqualTo(persistedDto);
    }

    @Test
    void shouldSaveExistingCity() {
        int cityId = 123;
        CityDto cityDto = CityDto.builder().id(cityId).build();
        CityDto persistedDto = CityDto.builder().id(cityId).build();
        City city = City.builder().id(cityId).build();
        City parsistedCity = City.builder().id(123).build();

        given(cityMapper.mapToEntity(eq(cityDto), any(City.class)))
                .willReturn(city);
        given(cityMapper.mapToDto(parsistedCity))
                .willReturn(persistedDto);
        given(cityRepository.findById(cityId))
                .willReturn(Optional.of(city));
        given(cityRepository.save(city))
                .willReturn(parsistedCity);

        cityService.save(cityDto);

        verify(cityRepository).findById(cityId);
        verify(cityMapper).mapToEntity(cityDto, city);
        verify(cityRepository).save(city);
        verify(cityMapper).mapToDto(city);
    }

    @Test
    void throwExceptionWhenCustomerIsNotFound() {
        int cityId = 123;
        CityDto cityDto = CityDto.builder().id(cityId).build();

        given(cityRepository.findById(cityId))
                .willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            cityService.save(cityDto);
        });
    }

    @Test
    void shouldFindAllCitiesWhenFilterIsEmpty() {
        int cityId = 123;
        CityDto cityDto = CityDto.builder().id(cityId).build();
        City city = City.builder().id(cityId).build();
        int pageNo = 1;
        int pageSize = 10;
        int totalRecords = 1000;

        Page<City> page = new PageImpl(newArrayList(city), PageRequest.of(pageNo, pageSize), totalRecords);

        when(cityMapper.mapToDto(city)).thenReturn(cityDto);
        when(cityRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CityDto> result = cityService.getCities(1, pageSize, null);

        verify(cityRepository).findAll(any(Pageable.class));
        verify(cityRepository, never()).findByNameContainingIgnoreCase(any(Pageable.class), any());

        assertThat(result.getTotalElements()).isEqualTo(totalRecords);
        assertThat(result.getTotalPages()).isEqualTo(100);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(newArrayList(cityDto));

    }

    @Test
    void shouldFindMathcedCitiesWhenFilterIsNotEmpty() {
        int pageNo = 1;
        int pageSize = 10;
        int totalRecords = 1000;
        String cityNameFilter = "CityNameFilter";
        int cityId = 123;

        CityDto cityDto = CityDto.builder().id(cityId).build();
        City city = City.builder().id(cityId).build();

        Page<City> page = new PageImpl(newArrayList(city), PageRequest.of(pageNo, pageSize), totalRecords);

        when(cityMapper.mapToDto(city)).thenReturn(cityDto);
        when(cityRepository.findByNameContainingIgnoreCase(any(Pageable.class), any())).thenReturn(page);

        Page<CityDto> result = cityService.getCities(1, pageSize, cityNameFilter);

        verify(cityRepository, never()).findAll(any(Pageable.class));
        verify(cityRepository).findByNameContainingIgnoreCase(any(Pageable.class), any());

        assertThat(result.getTotalElements()).isEqualTo(totalRecords);
        assertThat(result.getTotalPages()).isEqualTo(100);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(newArrayList(cityDto));
    }
}
