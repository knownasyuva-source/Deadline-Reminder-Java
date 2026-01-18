package com.deadlinereminder.model;

import java.sql.Date;

public class Assignment extends Deadline {
    public Assignment() { this.type = "Assignment"; }
    public Assignment(String title, Date dueDate, String subject, String teamMembers) {
        super(title, "Assignment", dueDate, subject, teamMembers);
    }
}
