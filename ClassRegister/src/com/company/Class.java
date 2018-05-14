package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kaza on 03.05.2018.
 */
public class Class implements Serializable {
    private ArrayList<Student> students;
    private String name;
    private Map<Register.Subjects, Register> registers;

    public Class(){
        students = new ArrayList<>();
        name = "";
        registers = new HashMap<>();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Register.Subjects, Register> getRegisters() {
        return registers;
    }

    public void setRegisters(Map<Register.Subjects, Register> registers) {
        this.registers = registers;
    }
}
