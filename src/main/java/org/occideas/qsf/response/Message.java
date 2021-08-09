package org.occideas.qsf.response;

public class Message {

    private String libraryId;
    private String messageId;
    private String messageText;

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "Message{" +
                "libraryId='" + libraryId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", messageText='" + messageText + '\'' +
                '}';
    }
}
