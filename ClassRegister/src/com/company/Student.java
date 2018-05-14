package com.company;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kaza on 03.05.2018.
 */
public class Student implements Serializable, Comparable<Student> {
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthday;
    private String phone;
    private String address;

    public Student(String lastName, String firstName, String patronymic, Date birthday, String phone, String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass()!=this.getClass()) return false;
        else {
            Student s = (Student) obj;
            if (this.firstName != s.firstName || this.lastName!=s.lastName ||
                    this.birthday!=s.birthday || this.phone!=s.phone || this.address!=s.address) return false;
            else return true;
        }
    }

    public String getName(){
        return lastName+" "+firstName;
    }

    @Override
    public int compareTo(Student student) {
        try {
            if (this.getName().compareTo(student.getName()) <0) return -1;
            else if (this.getName().compareTo(student.getName()) == 0) return 0;
        }
        catch (Exception e){}
        return 1;
    }
}
