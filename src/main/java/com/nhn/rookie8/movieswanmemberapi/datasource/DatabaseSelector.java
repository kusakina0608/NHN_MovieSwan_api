package com.nhn.rookie8.movieswanmemberapi.datasource;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSelector {
    public static final String DB01 = "swan_account";
    public static final String DB02 = "swan_account2";

    private ThreadLocal<String> contextHolder = new ThreadLocal<>();
    private List<String> databaseKeys = new ArrayList<>();

    public String getDataSource() {
        return contextHolder.get();
    }

    public void setDataSource(String key) {
        contextHolder.set(key);
    }

    public void indicateDB(String memberId) {
        setDataSource(getDbIndicator(memberId));
    }

    public void removeDataSource() {
        contextHolder.remove();
    }

    public void addDatabaseKey(String key) {
        databaseKeys.add(key);
    }

    public List<String> getDatabaseKeys() {
        return databaseKeys;
    }

    public final String getDbIndicator(String memberId) {
        if (memberId == null) {
            return DB01;
        }
        if (memberId.hashCode() % 2 == 0) {
            return DB01;
        }
        return DB02;
    }
}