package com.xiiilab.metrix.persistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Sergey on 17.07.2018
 */
@Entity(tableName = "metric")
public class MetricEntity {

    @PrimaryKey
    private int id;

    private String title;

    private int counter;

    public MetricEntity(int id, String title, int counter) {
        this.id = id;
        this.title = title;
        this.counter = counter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
