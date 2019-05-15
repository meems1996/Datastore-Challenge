package com.datastore.demo;

import java.util.Comparator;

/**
 * This comparator compares two Datastores objects by their stb
 */
public class STBComparator implements Comparator<DatastoreEntity> {

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {
        return dt1.getStb().compareTo(dt2.getStb());
    }
}