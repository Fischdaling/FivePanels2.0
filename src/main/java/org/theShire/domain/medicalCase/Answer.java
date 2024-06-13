package org.theShire.domain.medicalCase;

import org.theShire.domain.richType.Name;

import java.util.Objects;


public class Answer {
    //the name of an answer
    private Name answerName;

    public Answer(String name) {
        this.answerName = new Name(name);
    }


    //getter & setter--------------------------


    public Name getName() {
        return answerName;
    }

    public void setName(Name name) {
        this.answerName = name;
    }

    @Override
    public String toString() {
        return answerName.value();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(answerName, answer.answerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerName);
    }
}