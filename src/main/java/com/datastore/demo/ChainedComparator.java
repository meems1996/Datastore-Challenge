package com.datastore.demo;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This is a chained comparator that is used to sort a list by multiple
 * attributes by chaining a sequence of comparators of individual fields
 * together.
 *
 */
public class ChainedComparator implements Comparator<DatastoreEntity> {
    private List<Comparator<DatastoreEntity>> listComparators;

    public ChainedComparator(List<Comparator<DatastoreEntity>> comparators) {
        this.listComparators = comparators;
    }

    @Override
    public int compare(DatastoreEntity dt1, DatastoreEntity dt2) {

        for (Comparator<DatastoreEntity> comparator : listComparators) {
            int result = comparator.compare(dt1, dt2);

            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

}
