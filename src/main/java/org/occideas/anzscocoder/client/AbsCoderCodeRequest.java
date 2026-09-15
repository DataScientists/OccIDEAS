package org.occideas.anzscocoder.client;

public class AbsCoderCodeRequest {

    private Record record;
    private int numberOfSuggestions;

    public AbsCoderCodeRequest() {
    }

    public AbsCoderCodeRequest(String occpText, String tasksText, int numberOfSuggestions) {
        this.record = new Record(occpText, tasksText);
        this.numberOfSuggestions = numberOfSuggestions;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public int getNumberOfSuggestions() {
        return numberOfSuggestions;
    }

    public void setNumberOfSuggestions(int numberOfSuggestions) {
        this.numberOfSuggestions = numberOfSuggestions;
    }

    public static class Record {
        private String occp_text;
        private String tasks_text;

        public Record() {
        }

        public Record(String occp_text, String tasks_text) {
            this.occp_text = occp_text;
            this.tasks_text = tasks_text;
        }

        public String getOccp_text() {
            return occp_text;
        }

        public void setOccp_text(String occp_text) {
            this.occp_text = occp_text;
        }

        public String getTasks_text() {
            return tasks_text;
        }

        public void setTasks_text(String tasks_text) {
            this.tasks_text = tasks_text;
        }
    }
}
