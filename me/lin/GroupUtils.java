package me.lin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection分组工具类
 */
public class GroupUtils {


    /**
     * 分组聚合
     *
     * @param listToDeal    待分组的数据，相当于SQL中的原始表
     * @param clazz         带分组数据元素类型
     * @param groupBy       分组的属性名称
     * @param aggregatorMap 聚合器，key为聚合器名称，作为返回结果中聚合值map中的key
     * @param <T>           元素类型Class
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> Map<Object, Map<String, Object>> groupByProperty(
            Collection<T> listToDeal, Class<T> clazz, String groupBy,
            Map<String, Aggregator<T>> aggregatorMap) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {

        Map<Object, Collection<T>> groupResult = new HashMap<Object, Collection<T>>();

        for (T ele : listToDeal) {
            Field field = clazz.getDeclaredField(groupBy);
            field.setAccessible(true);
            Object key = field.get(ele);

            if (!groupResult.containsKey(key)) {
                groupResult.put(key, new ArrayList<T>());
            }
            groupResult.get(key).add(ele);
        }


        return invokeAggregators(groupResult, aggregatorMap);
    }


    public static <T> Map<Object, Map<String, Object>> groupByMethod(
            Collection<T> listToDeal, Class<T> clazz, String groupByMethodName,
            Map<String, Aggregator<T>> aggregatorMap) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Map<Object, Collection<T>> groupResult = new HashMap<Object, Collection<T>>();

        for (T ele : listToDeal) {
            Method groupByMenthod = clazz.getDeclaredMethod(groupByMethodName);
            groupByMenthod.setAccessible(true);
            Object key = groupByMenthod.invoke(ele);

            if (!groupResult.containsKey(key)) {
                groupResult.put(key, new ArrayList<T>());
            }
            groupResult.get(key).add(ele);
        }


        return invokeAggregators(groupResult, aggregatorMap);
    }

    private static <T> Map<Object, Map<String, Object>> invokeAggregators(Map<Object, Collection<T>> groupResult, Map<String, Aggregator<T>> aggregatorMap) {

        Map<Object, Map<String, Object>> aggResults = new HashMap<>();
        for (Object key : groupResult.keySet()) {
            Collection<T> group = groupResult.get(key);
            Map<String, Object> aggValues = doInvokeAggregators(key, group, aggregatorMap);
            if (aggValues != null && aggValues.size() > 0) {
                aggResults.put(key, aggValues);
            }

        }
        return aggResults;

    }


    private static <T> Map<String, Object> doInvokeAggregators(Object key, Collection<T> group, Map<String, Aggregator<T>> aggregatorMap) {
        Map<String, Object> aggResults = new HashMap<String, Object>();

        if (group != null && group.size() > 0) {

            // 调用当前key的每一个聚合函数
            for (String aggKey : aggregatorMap.keySet()) {
                Aggregator<T> aggregator = aggregatorMap.get(aggKey);
                Object aggResult = aggregator.aggregate(key, Collections.unmodifiableList(new ArrayList<T>(group)));
                aggResults.put(aggKey, aggResult);
            }

        }

        return aggResults;

    }

}

