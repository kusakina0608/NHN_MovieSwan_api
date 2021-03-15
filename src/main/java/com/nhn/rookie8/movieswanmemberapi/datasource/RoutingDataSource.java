package com.nhn.rookie8.movieswanmemberapi.datasource;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Log4j2
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Autowired
    private DatabaseSelector databaseSelector;

    @Override
    protected Object determineCurrentLookupKey() {
        Object dbKey = databaseSelector.getDataSource();
        log.debug("[RoutingDataSource] dbKey:{}", dbKey);
        if (dbKey == null)
            return "swan_account";
        return dbKey;
    }
}
