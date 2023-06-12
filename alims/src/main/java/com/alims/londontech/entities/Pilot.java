package com.alims.londontech.entities;

import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.enums.Gender;
import com.alims.londontech.constants.enums.Race;
import com.alims.londontech.constants.enums.Religion;
import com.alims.londontech.utils.LocalDateTimeConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pilot")
public class Pilot {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.alims.londontech.utils.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "name_with_initials")
    private String initials;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "NIC")
    private String nic;

    @Column(name = "dob")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dob;

    @Column(name = "religion")
    private Religion religion;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "race")
    private Race race;

    @Column(name = "reg_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime registrationDate;

    @Column(name = "status", nullable = false)
    private CommonStatus status;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "air_code", nullable = false)
    private String airCode;

    @Column(name = "emergency_contact_number")
    private String emergencyContactNumber;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
            @AttributeOverride(name = "createdOn", column = @Column(name = "created_on")),
            @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by")),
            @AttributeOverride(name = "updatedOn", column = @Column(name = "updated_on"))
    })
    private AuditData auditData;

    @OneToOne
    @JoinColumn(name = "userAccount", nullable = false)
    private UserAccount userAccount;

    @ManyToMany(mappedBy = "pilots")
    private Set<Schedule> schedules;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Set<Schedule> getSchedules() { return schedules; }

    public void setSchedules(Set<Schedule> schedules) { this.schedules = schedules; }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public AuditData getAuditData() {
        return auditData;
    }

    public void setAuditData(AuditData auditData) {
        this.auditData = auditData;
    }
}
