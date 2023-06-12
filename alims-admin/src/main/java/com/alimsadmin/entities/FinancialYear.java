package com.alimsadmin.entities;

import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.utils.LocalDateTimeConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "financial_year")
public class FinancialYear implements Comparable<FinancialYear> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.alimsadmin.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "year")
    private String year;

    @Column(name = "status", nullable = false)
    private CommonStatus status;

    @Column(name = "start_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime endDate;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on"))
    })
    private AuditData auditData;

    @OneToMany(mappedBy = "financialYear")
    private Set<AirlineProfile> airlineProfiles;

    public Set<AirlineProfile> getAirlineProfiles() {
        return airlineProfiles;
    }

    public void setAirlineProfiles(Set<AirlineProfile> airlineProfiles) {
        this.airlineProfiles = airlineProfiles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public AuditData getAuditData() {
        return auditData;
    }

    public void setAuditData(AuditData auditData) {
        this.auditData = auditData;
    }

    @Override
    public int compareTo(FinancialYear o) {
        return 0;
    }
}
