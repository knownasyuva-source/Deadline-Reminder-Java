package com.deadlinereminder.model;

import java.sql.Date;

public abstract class Deadline {
    protected int id;
    protected String title;
    protected String type;
    protected Date dueDate;
    protected String subject;
    protected String teamMembers;

    public Deadline() {}

    public Deadline(String title, String type, Date dueDate, String subject, String teamMembers) {
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.subject = subject;
        this.teamMembers = teamMembers;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTeamMembers() { return teamMembers; }
    public void setTeamMembers(String teamMembers) { this.teamMembers = teamMembers; }

    @Override
    public String toString() {
        return id + ": " + title + " (" + type + ") due " + dueDate;
    }
}
