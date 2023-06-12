package com.alims.londontech.repositories;

import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.roleName =?1")
    UserRole findUserRoleByUserName(String roleName);

    @Query("SELECT ur FROM UserRole ur WHERE ur.id=?1")
    UserRole findRoleById(Long id);
}
