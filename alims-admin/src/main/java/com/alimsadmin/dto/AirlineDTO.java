package com.alimsadmin.dto;

import com.alimsadmin.constants.enums.CommonStatus;

public class AirlineDTO {
    private String id;
    private String name;
    private String code;
    private String contactNo1;
    private String contactNo2;
    private String email;
    private String web;
    private String contactPerson;
    private String country;
    private String comment;
    private String address;
    private CommonStatus status;

    public AirlineDTO() {
    }

    public AirlineDTO(String id, String name, String code, String contactNo1, String contactNo2, String email, String web, String contactPerson, String country, String comment, String address, CommonStatus status) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.contactNo1 = contactNo1;
        this.contactNo2 = contactNo2;
        this.email = email;
        this.web = web;
        this.contactPerson = contactPerson;
        this.country = country;
        this.comment = comment;
        this.address = address;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContactNo1() {
        return contactNo1;
    }

    public void setContactNo1(String contactNo1) {
        this.contactNo1 = contactNo1;
    }

    public String getContactNo2() {
        return contactNo2;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AirlineDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", contactNo1='" + contactNo1 + '\'' +
                ", contactNo2='" + contactNo2 + '\'' +
                ", email='" + email + '\'' +
                ", web='" + web + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", country='" + country + '\'' +
                ", comment='" + comment + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                '}';
    }
}
