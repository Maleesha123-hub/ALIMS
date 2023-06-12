package com.alimsadmin.ServiceTest;

import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.validation.UserRoleValidationMessages;
import com.alimsadmin.dto.UserRoleDTO;
import com.alimsadmin.entities.AuditData;
import com.alimsadmin.entities.UserRole;
import com.alimsadmin.repositories.UserRoleRepository;

import static org.mockito.Mockito.when;

import com.alimsadmin.service.UserRoleService;
import com.alimsadmin.utils.CommonResponse;
import com.alimsadmin.utils.DateTimeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRoleServiceTest {

    @Autowired
    private UserRoleService userRoleService;

    @MockBean
    private UserRoleRepository userRoleRepository;

    public String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWh1cnUiLCJpYXQiOjE2ODYyODY4OTUsImV4cCI6MTY4NjMwNDg5NX0.FWW33sFmZLNOIi1UM-ns48DFJjV23LgYrQLeVs6VrbwkK2YTGv3iyIfSx6sRx-nKzuCRgFRxusHSrBYjXXL1_w";

    @Test
    public void getAllActiveUserRolesTest() {
        AuditData auditData = new AuditData();
        auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
        auditData.setCreatedBy(Long.parseLong("1"));
        when(userRoleRepository.findAll()).thenReturn(Stream
                .of(new UserRole(Long.parseLong("1729318259814400"), "SUPER_ADMIN", "This is super admin.", CommonStatus.ACTIVE, auditData),
                        new UserRole(Long.parseLong("1729318259814401"), "PILOT", "This is super admin.", CommonStatus.ACTIVE, auditData),
                        new UserRole(Long.parseLong("1729318259814402"), "CREW", "This is super admin.", CommonStatus.ACTIVE, auditData),
                        new UserRole(Long.parseLong("1729318259814403"), "SYSTEM_ADMIN", "This is super admin.", CommonStatus.ACTIVE, auditData),
                        new UserRole(Long.parseLong("1729318259814404"), "PASSENGER", "This is super admin.", CommonStatus.ACTIVE, auditData)).collect(Collectors.toList()));
        Assert.assertEquals(5, userRoleService.getAllActiveUserRoles().size());
    }

    @Test
    public void saveUpdateUserRole() {
        AuditData auditData = new AuditData();
        auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
        auditData.setCreatedBy(Long.parseLong("1"));
        UserRole userRole = new UserRole("SUPER_ADMIN", "This is super admin.", CommonStatus.ACTIVE, auditData);
        UserRoleDTO dto = new UserRoleDTO("", "SUPER_ADMIN", "This is super admin.", CommonStatus.ACTIVE.toString());
        when(userRoleRepository.save(userRole)).thenReturn(userRole);
        Assert.assertEquals(true, userRoleService.saveUpdateUserRole(token, dto));
    }

    @Test
    public void saveUpdateUserRoleWithoutRoleName() {
        AuditData auditData = new AuditData();
        auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
        auditData.setCreatedBy(Long.parseLong("1"));
        UserRole userRole = new UserRole("", "This is super admin.", CommonStatus.ACTIVE, auditData);
        UserRoleDTO dto = new UserRoleDTO("", "", "This is super admin.", CommonStatus.ACTIVE.toString());
        when(userRoleRepository.save(userRole)).thenReturn(userRole);
        Assert.assertEquals(UserRoleValidationMessages.EMPTY_USER_ROLE_NAME, userRoleService.saveUpdateUserRole(token, dto));
    }

    @Test
    public void saveUpdateUserRoleWithoutDescription() {
        AuditData auditData = new AuditData();
        auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
        auditData.setCreatedBy(Long.parseLong("1"));
        UserRole userRole = new UserRole("SUPER_ADMIN", "", CommonStatus.ACTIVE, auditData);
        UserRoleDTO dto = new UserRoleDTO("", "SUPER_ADMIN", "", CommonStatus.ACTIVE.toString());
        when(userRoleRepository.save(userRole)).thenReturn(userRole);
        Assert.assertEquals(UserRoleValidationMessages.EMPTY_USER_ROLE_NAME, userRoleService.saveUpdateUserRole(token, dto));
    }

}
