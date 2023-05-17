package com.example.kntest.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.kntest.model.CityDto;
import com.example.kntest.persistence.entity.City;
import com.example.kntest.service.CityService;

@ExtendWith(MockitoExtension.class)
public class CityMapperTest {
    @InjectMocks
    private CityMapper cityMapper;

    @Test
    void shouldHandleNullEntityToDto() {
        City entity = City.builder().build();
        CityDto dto = CityDto.builder().build();

        CityDto result = cityMapper.mapToDto(entity);

        assertThat(result).usingDefaultComparator().isEqualTo(dto);
    }

    @Test
    void shouldTransformEntityToDto() {
        City entity = City.builder().id(123).name("City Name").pictureUrl("Picture URL").build();
        CityDto dto = CityDto.builder().id(123).name("City Name").pictureUrl("Picture URL").build();

        CityDto result = cityMapper.mapToDto(entity);

        assertThat(result).usingDefaultComparator().isEqualTo(dto);
    }
}
