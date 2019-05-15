package com.datastore.demo;

import java.util.Comparator;

/**
 * This comparator compares two Datastores objects by their rev (reviews)
 */
public class RevComparator implements Comparator<DatastoreEntity> {

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {

        return Double.compare(dt1.getRev(), dt2.getRev());
    }
}
