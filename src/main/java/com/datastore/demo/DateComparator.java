package com.datastore.demo;

import java.util.Comparator;

/**
 * This comparator compares two Datastores objects by their dates
 */
public class DateComparator implements Comparator<DatastoreEntity> {

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {

        return dt1.getDate().compareTo(dt2.getDate());
    }
}
