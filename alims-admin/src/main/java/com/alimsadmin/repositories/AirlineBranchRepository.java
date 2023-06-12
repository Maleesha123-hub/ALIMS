package com.alimsadmin.repositories;

import com.alimsadmin.entities.Airline;
import com.alimsadmin.entities.AirlineBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineBranchRepository extends JpaRepository<AirlineBranch, Long> {

    @Query("SELECT ar FROM AirlineBranch ar WHERE ar.name=?1")
    AirlineBranch findAirlineBranchByName(String name);

    @Query("SELECT ar FROM AirlineBranch ar WHERE ar.code=?1")
    AirlineBranch findAirlineBranchByCode(String code);
}
