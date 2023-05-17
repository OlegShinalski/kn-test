package com.example.kntest.persistence.repositary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.kntest.persistence.entity.City;

public interface CityRepository extends CrudRepository<City, Integer>, PagingAndSortingRepository<City, Integer> {

    Page<City> findByNameContainingIgnoreCase(Pageable pageable, String filter);

}