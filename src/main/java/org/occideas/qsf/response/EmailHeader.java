package org.occideas.qsf.response;

public class EmailHeader {

    private String fromEmail;
    private String fromName;
    private String replyToEmail;
    private String subject;

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public void setReplyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "EmailHeader{" +
                "fromEmail='" + fromEmail + '\'' +
                ", fromName='" + fromName + '\'' +
                ", replyToEmail='" + replyToEmail + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
