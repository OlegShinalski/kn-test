package com.example.kntest.web;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.kntest.model.CityDto;
import com.example.kntest.service.CityService;

@ExtendWith(MockitoExtension.class)
class CityRestControllerTest {

    @InjectMocks
    private CityRestController controller;
    @Mock
    private CityService cityService;

    @BeforeEach
    void setup() {
    }

    @Test
    void getAllEmployeesAPI() {
        int pageSize = 10;
        int pageNo = 1;
        String cityNameFilter = "";

        Page<CityDto> page = new PageImpl(newArrayList(CityDto.builder().build()), PageRequest.of(pageNo, pageSize), 1000);

        when(cityService.getCities(pageNo, pageSize, cityNameFilter)).thenReturn(page);

        controller.getCities(cityNameFilter, pageNo, pageSize);

        verify(cityService).getCities(pageNo, pageSize, cityNameFilter);
    }

    @Test
    void getCityById() {
        controller.getCity(1);

        verify(cityService).getById(1);
    }

    @Test
    void updateCity() {
        int id = 123;
        CityDto dto = CityDto.builder().id(id).build();

        controller.updateCity(dto, id);

        verify(cityService).save(dto);
    }
}
