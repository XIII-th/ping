package com.xiiilab.ping.persistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by XIII-th on 17.07.2018
 */
@Entity(tableName = "hosts")
public class HostEntity {

    @PrimaryKey
    @NonNull
    private String host;

    private String title;

    private int frequency;

    private int timeout;

    private long created;

    public HostEntity(@NonNull String host) {
        this.host = host;
        frequency = timeout = 1000;
    }

    @Ignore
    public HostEntity(@NonNull String host, @Nullable String title) {
        this(host);
        setTitle(title);
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", title, host);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostEntity)) return false;

        HostEntity entity = (HostEntity) o;

        if (frequency != entity.frequency) return false;
        if (timeout != entity.timeout) return false;
        if (!host.equals(entity.host)) return false;
        return title != null ? title.equals(entity.title) : entity.title == null;
    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + frequency;
        result = 31 * result + timeout;
        return result;
    }
}
