package com.deadlinereminder.model;

import java.sql.Date;

public class Project extends Deadline {
    public Project() { this.type = "Project"; }
    public Project(String title, Date dueDate, String subject, String teamMembers) {
        super(title, "Project", dueDate, subject, teamMembers);
    }
}
