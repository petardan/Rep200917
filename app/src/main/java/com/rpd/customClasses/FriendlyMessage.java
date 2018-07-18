package com.rpd.customClasses;

/**
 * Created by Petar on 1/10/2018.
 */

public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String senderID;
    private Long timestamp;
    private String jobID;
    private int status;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoUrl, String senderID, Long timestamp, String jobID, int status) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.senderID = senderID;
        this.timestamp = timestamp;
        this.jobID = jobID;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
