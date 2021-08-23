package org.occideas.qsf.response;

public class EmailRecipients {

    private String mailingListId;
    private String contactId;
    private String libraryId;
    private String sampleId;

    public String getMailingListId() {
        return mailingListId;
    }

    public void setMailingListId(String mailingListId) {
        this.mailingListId = mailingListId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    @Override
    public String toString() {
        return "EmailRecipients{" +
                "mailingListId='" + mailingListId + '\'' +
                ", contactId='" + contactId + '\'' +
                ", libraryId='" + libraryId + '\'' +
                ", sampleId='" + sampleId + '\'' +
                '}';
    }
}
