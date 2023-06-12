package com.alims.londontech.entities;

import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.utils.LocalDateTimeConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.alims.londontech.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "schedule_name", length = 100, nullable = false)
    private String scheduleName;

    @Column(name = "status")
    private CommonStatus status;

    @Column(name = "air_code", nullable = false)
    private String airCode;

    @Column(name = "departure_date", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime departureDate;

    @Column(name = "arrival_date", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime arrivalDate;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on"))
    })
    private AuditData auditData;

    @ManyToOne
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;

    @ManyToMany
    @JoinColumn(name = "pilot_id")
    private Set<Pilot> pilots;

    @ManyToMany
    @JoinColumn(name = "crew_id")
    private Set<Crew> crews;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "country_airport_id")
    private CountryAirport countryAirport;

    @ManyToOne
    @JoinColumn(name = "landing_point_id")
    private CountryAirport landingPoint;

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public CountryAirport getLandingPoint() {
        return landingPoint;
    }

    public void setLandingPoint(CountryAirport landingPoint) {
        this.landingPoint = landingPoint;
    }

    public CountryAirport getCountryAirport() {
        return countryAirport;
    }

    public void setCountryAirport(CountryAirport countryAirport) {
        this.countryAirport = countryAirport;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Crew> getCrews() {
        return crews;
    }

    public void setCrews(Set<Crew> crews) {
        this.crews = crews;
    }

    public Set<Pilot> getPilots() {
        return pilots;
    }

    public void setPilots(Set<Pilot> pilots) {
        this.pilots = pilots;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public AuditData getAuditData() {
        return auditData;
    }

    public void setAuditData(AuditData auditData) {
        this.auditData = auditData;
    }
}
