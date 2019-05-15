package com.datastore.demo;

import java.util.Comparator;

public class ViewTimeComparator implements Comparator<DatastoreEntity> {

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {

        return dt1.getViewTime().compareTo(dt2.getViewTime());
    }
}
