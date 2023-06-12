package com.alimsadmin.repositories;

import com.alimsadmin.entities.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    @Query("SELECT ar FROM Airline ar WHERE ar.name=?1")
    Airline findAirlineByName(String name);

    @Query("SELECT ar FROM Airline ar WHERE ar.code=?1")
    Airline findAirlineByCode(String code);
}
