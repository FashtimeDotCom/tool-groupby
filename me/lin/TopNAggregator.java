package me.lin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * 取每组TopN
 *
 * Created by Brandon on 2016/7/21.
 */
public class TopNAggregator<T> implements Aggregator<T> {

    private Comparator<T> comparator;

    private int limit;


    public TopNAggregator(Comparator<T> comparator, int limit) {
        this.limit = limit;
        this.comparator = comparator;
    }

    @Override
    public Object aggregate(Object key, List<T> values) {

        if (values == null || values.size() == 0) {
            return null;
        }

        ArrayList<T> copy = new ArrayList<>( values );
        Collections.sort(copy, comparator);
        int size = values.size();
        int toIndex = Math.min(limit, size);
        return copy.subList(0, toIndex);
    }
}
