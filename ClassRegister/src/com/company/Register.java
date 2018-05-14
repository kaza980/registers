package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Kaza on 03.05.2018.
 */
public class Register implements Serializable, Iterable<ArrayList> {
   public enum Subjects {Mathematic, NativeLanguage, ForeignLanguage, Biology, Geography, Literature, PhysicalEducation};

    private Subjects subject;
    Map<Student, ArrayList<Mark>> marks;

    public Register(Subjects subject, ArrayList<Student> students){
        this.subject = subject;
        marks = new HashMap<>();
        for(int i = 0; i < students.size(); i++)
            marks.put(students.get(i), new ArrayList<>());
    }

    public Subjects getSubject() {
        return subject;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
    }

    public Map<Student, ArrayList<Mark>> getMarks() {
        return marks;
    }

    public void setMarks(Map<Student, ArrayList<Mark>> marks) {
        this.marks = marks;
    }

    public ArrayList<Mark> getMarksOfStudent(Student student){
        return marks.get(student);
    }

    public void addMark(Student student, Mark mark){
        if(mark.getValue() > 0 && mark.getValue() < 6)
            marks.get(student).add(mark);
    }

    public Iterator<ArrayList> iterator() {
        return new RegisterIterator(this);
    }
}
