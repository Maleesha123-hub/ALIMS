package com.alimsadmin.repositories;

import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    @Query("SELECT u FROM UserAccount u WHERE u.username=?1")
    UserAccount findByName(String username);

    @Query("SELECT u FROM UserAccount u WHERE u.token =?1")
    UserAccount findAccountByToken(String token);

    @Query("SELECT u FROM UserAccount u WHERE u.id=?1")
    UserAccount findAccountById(Long id);
}
