package com.alims.londontech.repositories;

import com.alims.londontech.entities.CountryAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryAirportRepository extends JpaRepository<CountryAirport, Long> {

    @Query("SELECT c FROM CountryAirport c WHERE c.id =?1")
    CountryAirport findCountryAirportById(Long id);
}
