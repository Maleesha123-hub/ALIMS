package com.alimsadmin.entities;

import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.utils.LocalDateTimeConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "airline_user")
public class AirlineUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.alimsadmin.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Column(name = "role_name", length = 100, nullable = false)
    private String roleName;

    @Column(name = "status", nullable = false)
    private CommonStatus status;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "airline_id", nullable = false)
    private String airlineId;

    @Column(name = "airline_branch_code", nullable = false)
    private String airlineBranchCode;

    @Column(name = "last_access_time")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastAccessTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on"))
    })
    private AuditData auditData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineBranchCode() {
        return airlineBranchCode;
    }

    public void setAirlineBranchCode(String airlineBranchCode) {
        this.airlineBranchCode = airlineBranchCode;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public AuditData getAuditData() {
        return auditData;
    }

    public void setAuditData(AuditData auditData) {
        this.auditData = auditData;
    }
}
