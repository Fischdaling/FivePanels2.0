package org.theShire.domain.medicalCase;


import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.foundation.DomainAssertion.greaterEqualsZero;
import static org.theShire.foundation.DomainAssertion.isNotNull;

public class Vote {
    //saves an answer with the verified Rich type Answer
    private Answer answer;
    //determines how much percent a member wants to add to an answer
    private double percent;


    public Vote(Answer answer, double percent) {
        setAnswer(answer);
        setPercent(percent);
    }

    //getter & setter-----------------------
    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = isNotNull(answer, "answer", exTypeCase);
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        greaterEqualsZero(percent, "percent", exTypeCase);
        this.percent = percent;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(answer).append(", ");
        sb.append(percent);
        return sb.toString();
    }

    public String ToCSVString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(answer).append("|");
        sb.append(percent);
        return sb.toString();
    }
}
