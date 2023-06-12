package com.alimsadmin.repositories;

import com.alimsadmin.entities.FinancialYear;
import com.alimsadmin.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface YearRepository extends JpaRepository<FinancialYear, Long> {

    @Query("SELECT y FROM FinancialYear y WHERE y.year=?1")
    FinancialYear findByName(String year);
}
