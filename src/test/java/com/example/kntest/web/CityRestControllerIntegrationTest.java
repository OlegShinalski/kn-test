package com.example.kntest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.kntest.model.CityDto;

@SpringBootTest
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CityRestControllerIntegrationTest {
    @Autowired
    private CityRestController controller;

    @Test
    void shouldReturnPageOfCities() {
        int pageSize = 10;
        int pageNo = 5;
        Page<CityDto> result = controller.getCities("", pageNo, pageSize);

        assertThat(result.getTotalElements()).isEqualTo(1000);
        assertThat(result.getTotalPages()).isEqualTo(100);
        assertThat(result.getNumber()).isEqualTo(pageNo-1);
        assertThat(result.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(result.getContent()).hasSize(pageSize);
    }

    @Test
    void shouldReturnEmptyPageOfCities() {
        int pageSize = 10;
        int pageNo = 2;
        Page<CityDto> result = controller.getCities("Some Unexistant City bla-bla-bla", pageNo, pageSize);

        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getNumberOfElements()).isEqualTo(0);
        assertThat(result.getContent()).hasSize(0);
    }
}
