package com.example.kntest.model;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDto implements Serializable {

    private Integer id;

    @NotEmpty(message = "Name cannot be empty.")
    @Size(max=100, message = "Name cannot be longer than 100 chars.")
    private String name;

    @NotEmpty(message = "URL cannot be empty.")
    @Size(max=1000)
    private String pictureUrl;

}
