package com.company;

import java.util.*;

/**
 * Created by Kaza on 13.05.2018.
 */
public class RegisterIterator implements Iterator<ArrayList> {

   private Register register;
   private int count;
   private ArrayList<Student> students;

   public RegisterIterator(Register register){
        this.register = register;
        count = 0;
        students = new ArrayList<>();
        students.addAll(register.getMarks().keySet());
        Collections.sort(students);
   }

   public boolean hasNext(){
        return students.size() > count;

   }

   public ArrayList next() throws NoSuchElementException {
        if(count < students.size()) {
            ArrayList<Object> result = new ArrayList<>();
            result.add(0, students.get(count).getName());
            result.add(1, register.getMarksOfStudent(students.get(count)));
            count++;
            return result;
        }
            else throw new NoSuchElementException();

        }
}
