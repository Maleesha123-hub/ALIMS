package com.alims.londontech.dto;

public class CrewDTO {

    private String id;
    private String initials;
    private String firstName;
    private String lastName;
    private String fullName;
    private String nic;
    private String emergencyContactNo;
    private String email;
    private String dob;
    private String religion;
    private String gender;
    private String race;
    private String crewUser;
    private String crewUserName;
    private String crewRegDate;
    private String crewStatus;
    private String address;

    public CrewDTO() {
    }

    public CrewDTO(String id, String initials, String firstName, String lastName, String fullName, String nic, String emergencyContactNo, String email, String dob, String religion, String gender, String race, String crewUser, String crewUserName, String crewRegDate, String crewStatus, String address) {
        this.id = id;
        this.initials = initials;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.nic = nic;
        this.emergencyContactNo = emergencyContactNo;
        this.email = email;
        this.dob = dob;
        this.religion = religion;
        this.gender = gender;
        this.race = race;
        this.crewUser = crewUser;
        this.crewUserName = crewUserName;
        this.crewRegDate = crewRegDate;
        this.crewStatus = crewStatus;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCrewUser() {
        return crewUser;
    }

    public void setCrewUser(String crewUser) {
        this.crewUser = crewUser;
    }

    public String getCrewUserName() {
        return crewUserName;
    }

    public void setCrewUserName(String crewUserName) {
        this.crewUserName = crewUserName;
    }

    public String getCrewRegDate() {
        return crewRegDate;
    }

    public void setCrewRegDate(String crewRegDate) {
        this.crewRegDate = crewRegDate;
    }

    public String getCrewStatus() {
        return crewStatus;
    }

    public void setCrewStatus(String crewStatus) {
        this.crewStatus = crewStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
