package com.example.kntest.web;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.kntest.model.CityDto;
import com.example.kntest.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CityRestController.class)
public class CityRestControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService cityService;

    @Test
    void shouldReturnBadRequestStatusRetrievingCitiesPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/cities/page?page=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(MockMvcRequestBuilders
                        .get("/cities/page?pageSize=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(MockMvcRequestBuilders
                        .get("/cities/page")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetCitiesPage() throws Exception {
        Page<CityDto> page = new PageImpl(newArrayList(
                CityDto.builder().id(1).name("City 1").build(),
                CityDto.builder().id(2).name("City 2").build(),
                CityDto.builder().id(3).name("City 3").build(),
                CityDto.builder().id(4).name("City 4").build(),
                CityDto.builder().id(5).name("City 5").build()
        ), PageRequest.of(1, 10), 1000);
        when(cityService.getCities(1, 10, null)).thenReturn(page);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get("/cities/page?page=1&pageSize=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String s = mvcResult.getResponse().getContentAsString();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(asJsonString(page));
    }

    @Test
    void shouldReturnCityOnGetCity() throws Exception {
        int cityId = 10;
        CityDto dto = CityDto.builder().id(cityId).name("City Name").build();

        when(cityService.getById(cityId)).thenReturn(dto);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get("/cities/{id}", cityId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(cityService).getById(cityId);

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(asJsonString(dto));
    }

    @Test
    void shouldReturnCityOnUpdateCity() throws Exception {
        int cityId = 10;
        CityDto dto = CityDto.builder().id(cityId).name("City Name").pictureUrl("Picture URL").build();

        when(cityService.save(dto)).thenReturn(dto);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put("/cities/{id}", cityId)
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(cityService).save(dto);

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(asJsonString(dto));
    }

    @Test
    void shouldNotCallUpdateCityInCaseEMptyNameAndUrl() throws Exception {
        int cityId = 10;
        CityDto dto = CityDto.builder().id(cityId).build();

        when(cityService.save(dto)).thenReturn(dto);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put("/cities/{id}", cityId)
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(cityService, never()).save(dto);

        JSONObject json = new JSONObject();
        json.put("pictureUrl", "URL cannot be empty.");
        json.put("name", "Name cannot be empty.");
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(json.toString());
    }

    @Test
    void shouldNotCallUpdateCityInCaseLongName() throws Exception {
        int cityId = 10;
        CityDto dto = CityDto.builder().id(cityId)
                .name(StringUtils.repeat("A", 101))
                .pictureUrl("The URL")
                .build();

        when(cityService.save(dto)).thenReturn(dto);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put("/cities/{id}", cityId)
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(cityService, never()).save(dto);

        JSONObject json = new JSONObject();
        json.put("name", "Name cannot be longer than 100 chars.");
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(json.toString());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
