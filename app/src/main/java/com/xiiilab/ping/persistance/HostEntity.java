package com.xiiilab.ping.persistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Sergey on 17.07.2018
 */
@Entity(tableName = "hosts")
public class HostEntity {

    @PrimaryKey
    @NonNull
    private String host;

    private String title;

    private int timeout;

    @Ignore
    private int lastPing;

    public HostEntity(@NonNull String host) {
        this.host = host;
    }

    public HostEntity(@NonNull String host, String title, int lastPing) {
        this(host);
        setTitle(title);
        setLastPing(lastPing);
    }

    @NonNull
    public String getHost() {
        return host;
    }

    public void setHost(@NonNull String host) {
        this.host = host;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getLastPing() {
        return lastPing;
    }

    public void setLastPing(int lastPing) {
        this.lastPing = lastPing;
    }
}
