package com.company.vac.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;

import javax.persistence.*;

@PublishEntityChangedEvents
@Table(name = "VAC_VACATION")
@Entity(name = "vac_Vacation")
@NamePattern("%s|description")
public class Vacation extends StandardEntity {
    private static final long serialVersionUID = 7718922797551818061L;

    @Column(name = "DATE_")
    private String date;

    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "EMPLOYEE_ID")
    private String employeeId;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "DAYS")
    private Integer days;

    @Column(name = "VACATION_TYPE_ID")
    private String vacationTypeId;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICATION_ID")
    private FileDescriptor application;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DECREE_ID")
    private FileDescriptor decree;

    public FileDescriptor getDecree() {
        return decree;
    }

    public void setDecree(FileDescriptor decree) {
        this.decree = decree;
    }

    public FileDescriptor getApplication() {
        return application;
    }

    public void setApplication(FileDescriptor application) {
        this.application = application;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVacationTypeId() {
        return vacationTypeId;
    }

    public void setVacationTypeId(String vacationTypeId) {
        this.vacationTypeId = vacationTypeId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}