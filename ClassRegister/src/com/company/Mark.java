package com.company;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kaza on 03.05.2018.
 */
public class Mark implements Serializable {
    private int value;
    private Date date;

    public Mark(int value, Date date){
        this.value = value;
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return value + " ("+ new SimpleDateFormat("dd.MM.yy").format(date)+")";
    }
}
