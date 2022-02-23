package model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Class for creating and managing Appointment objects
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String appointmentType;
    private String startTime;
    private String endTime;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;

    /**
     * Constructor for Appointment objects
     * @param id appointment id
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param appointmentType appointment type
     * @param startTime start time
     * @param endTime end time
     * @param customerId related customer id
     * @param userId related user id
     * @param contactId related contact id
     * @param contactName related contact name
     */
    public Appointment(int id, String title, String description, String location, String appointmentType, String startTime, String endTime, int customerId, int userId, int contactId, String contactName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.appointmentType = appointmentType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Setters for Appointment objects
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) { this.contactName = contactName; }

    /**
     * Getters for Appointment objects
     */
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getAppointmentType() {
        return this.appointmentType;
    }
    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getContactId() {
        return this.contactId;
    }

    public String getContactName() { return this.contactName; }
}


