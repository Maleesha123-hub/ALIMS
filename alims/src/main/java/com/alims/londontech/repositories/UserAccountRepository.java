package com.alims.londontech.repositories;

import com.alims.londontech.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    @Query("SELECT u FROM UserAccount u WHERE u.username=?1")
    UserAccount findByName(String username);

    @Query("SELECT u FROM UserAccount u WHERE u.token =?1")
    UserAccount findAccountByToken(String token);

    @Query("SELECT u FROM UserAccount u WHERE u.id=?1")
    UserAccount findAccountById(Long id);
}
