package com.datastore.demo;

import java.util.Comparator;

/**
 * This comparator compares two Datastores objects by their titles
 */
public class TitleComparator implements Comparator<DatastoreEntity> {

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {
        return dt1.getTitle().compareTo(dt2.getTitle());
    }
}
