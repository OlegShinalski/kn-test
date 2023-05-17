package com.example.kntest.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.kntest.model.CityDto;
import com.example.kntest.service.CityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CityRestController {

    private final CityService cityService;

    @GetMapping("/cities/page")
    @ResponseBody
    public Page<CityDto> getCities(@RequestParam(required = false) String name,
            @RequestParam(value = "page") int pageNo,
            @RequestParam(value = "pageSize") int pageSize) {
        return cityService.getCities(pageNo, pageSize, name);
    }

    @GetMapping("/cities/{id}")
    @ResponseBody
    public CityDto getCity(@PathVariable Integer id) {
        return cityService.getById(id);
    }

    @PutMapping("/cities/{id}")
    @ResponseBody
    public CityDto updateCity(@RequestBody @Valid CityDto cityDto, @PathVariable Integer id) {
        return cityService.save(cityDto);
    }
    //    public ResponseEntity<CityDto> updateCity(@RequestBody @Valid CityDto cityDto, @PathVariable Integer id) {
    //        CityDto persisted = cityService.save(cityDto);
    //        return ResponseEntity.ok()
    //                .body(persisted);
    //    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
