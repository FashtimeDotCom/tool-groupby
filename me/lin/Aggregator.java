package me.lin;

import java.util.List;

/**
 *
 * 聚合操作
 *
 * Created by Brandon on 2016/7/21.
 */
public interface Aggregator<T> {

    /**
     * 每一组的聚合操作
     *
     * @param key
     * @param values
     * @return
     */
    Object aggregate(Object key , List<T> values);
}
