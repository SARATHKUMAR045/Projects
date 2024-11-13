package Project.TaskAutomationTool.model;

import java.time.LocalDateTime;

public class Email {
    private String recipient;
    private String subject;
    private String body;


    private Task task;

    public Email() {
    }

    public Email(String recipient, String subject, String body, Task task) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        if (recipient == null || !recipient.contains("@")) {
            throw new IllegalStateException("Invalid Email!");
        }
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            throw new IllegalStateException("Subject cannot be empty");
        }
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body == null || body.isEmpty()) {
            throw new IllegalStateException("Email body cannot be empty!");
        }
        this.body = body;
    }

    public String toString() {
        return String.format("Email[recipient = '%s', subject = '%s', body = '%s']", recipient, subject, body);
    }

}
