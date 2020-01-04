package org.occideas.qsf;

import org.occideas.qsf.payload.DisplayLogic;
import org.occideas.vo.QuestionVO;

public class QSFQuestion implements Comparable<QSFQuestion> {

    private int id;
    private QuestionVO question;
    private String qid;
    private DisplayLogic displayLogic;

    public QSFQuestion() {
    }

    public QSFQuestion(int id, QuestionVO question, String qid, DisplayLogic displayLogic) {
        this.id = id;
        this.question = question;
        this.qid = qid;
        this.displayLogic = displayLogic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuestionVO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionVO question) {
        this.question = question;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public DisplayLogic getDisplayLogic() {
        return displayLogic;
    }

    public void setDisplayLogic(DisplayLogic displayLogic) {
        this.displayLogic = displayLogic;
    }

    @Override
    public int compareTo(QSFQuestion o) {
        if (this.getId() > o.getId()) {
            return 1;
        } else if (this.getId() < o.getId()) {
            return -1;
        }

        return 0;
    }
}
