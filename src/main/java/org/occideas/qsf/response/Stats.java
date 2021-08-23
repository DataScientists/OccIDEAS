package org.occideas.qsf.response;

public class Stats {

    private int sent;
    private int failed;
    private int started;
    private int bounced;
    private int opened;
    private int skipped;
    private int finished;
    private int complaints;
    private int blocked;

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getStarted() {
        return started;
    }

    public void setStarted(int started) {
        this.started = started;
    }

    public int getBounced() {
        return bounced;
    }

    public void setBounced(int bounced) {
        this.bounced = bounced;
    }

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getComplaints() {
        return complaints;
    }

    public void setComplaints(int complaints) {
        this.complaints = complaints;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "sent=" + sent +
                ", failed=" + failed +
                ", started=" + started +
                ", bounced=" + bounced +
                ", opened=" + opened +
                ", skipped=" + skipped +
                ", finished=" + finished +
                ", complaints=" + complaints +
                ", blocked=" + blocked +
                '}';
    }
}
