package com.alimsadmin.repositories;

import com.alimsadmin.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.roleName =?1")
    UserRole findUserRoleByUserName(String roleName);

}
