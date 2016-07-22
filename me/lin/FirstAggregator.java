package me.lin;

import java.util.List;

/**
 *
 * 取第一个元素
 *
 * Created by Brandon on 2016/7/21.
 */
public class FirstAggregator<T> implements Aggregator<T> {

    @Override
    public Object aggregate(Object key, List<T> values) {

        if ( values.size() >= 1) {
            return values.get( 0 );
        }else {
            return null;
        }

    }
}
