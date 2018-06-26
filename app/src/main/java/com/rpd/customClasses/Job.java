package com.rpd.customClasses;

import java.io.Serializable;

/**
 * Created by Petar on 11/15/2017.
 */

public class Job implements Serializable {

    String jobId;
    String repairmanId;
    String userId;
    String jobTitle;
    String jobDesciption;
    String jobSeverity;
    String jobStartDate;
    String jobEndDate;
    String userAddress;
    String userEmail;
    String userPhone;
    String jobStatus; //1- Open, 2 - Confirmed, 3 - Finished, 4 - Cancelled

    public Job() {
    }

    public Job(String jobId, String repairmanId, String userId, String jobTitle, String jobDesciption, String jobSeverity, String jobStartDate, String jobEndDate, String userAddress, String userEmail, String userPhone, String jobStatus) {
        this.jobId = jobId;
        this.repairmanId = repairmanId;
        this.userId = userId;
        this.jobTitle = jobTitle;
        this.jobDesciption = jobDesciption;
        this.jobSeverity = jobSeverity;
        this.jobStartDate = jobStartDate;
        this.jobEndDate = jobEndDate;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.jobStatus = jobStatus;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRepairmanId() {
        return repairmanId;
    }

    public void setRepairmanId(String repairmanId) {
        this.repairmanId = repairmanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDesciption() {
        return jobDesciption;
    }

    public void setJobDesciption(String jobDesciption) {
        this.jobDesciption = jobDesciption;
    }

    public String getJobSeverity() {
        return jobSeverity;
    }

    public void setJobSeverity(String jobSeverity) {
        this.jobSeverity = jobSeverity;
    }

    public String getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public String getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(String jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}
