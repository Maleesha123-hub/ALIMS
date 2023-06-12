package com.alimsadmin.repositories;

import com.alimsadmin.entities.AirlineProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface AirlineProfileRepository extends JpaRepository<AirlineProfile, Long> {

    @Query("SELECT ap FROM AirlineProfile ap WHERE ap.profileName=?1")
    AirlineProfile findProfileByName(String profileName);

    @Query("SELECT ap FROM AirlineProfile ap WHERE ap.id=?1")
    AirlineProfile getProfileById(Long profileId);
}
