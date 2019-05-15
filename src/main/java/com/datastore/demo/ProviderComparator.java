package com.datastore.demo;

import java.util.Comparator;

/**
 * This comparator compares two Datastores objects by their provider
 */
public class ProviderComparator implements Comparator<DatastoreEntity> {

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {

        return dt1.getProvider().compareTo(dt2.getProvider());
    }
}