package com.alimsadmin.entities;

import com.alimsadmin.constants.enums.CommonStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "airline_profile")
public class AirlineProfile {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.alimsadmin.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "profile_name", nullable = false)
    private String profileName;

    @Column(name = "status", nullable = false)
    private CommonStatus status;

    @ManyToOne
    @JoinColumn(name = "financialYear", nullable = false)
    private FinancialYear financialYear;

    @ManyToOne
    @JoinColumn(name = "airlineBranch", nullable = false)
    private AirlineBranch airlineBranch;

    @OneToOne
    @JoinColumn(name = "systemAdmin", nullable = false)
    private UserAccount userAccount;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on"))
    })
    private AuditData auditData;

    public AuditData getAuditData() {
        return auditData;
    }

    public void setAuditData(AuditData auditData) {
        this.auditData = auditData;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public AirlineBranch getAirlineBranch() {
        return airlineBranch;
    }

    public void setAirlineBranch(AirlineBranch airlineBranch) {
        this.airlineBranch = airlineBranch;
    }

    public FinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(FinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }
}
