package me.lin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupByPropertyTest {

    public static void main(String[] args) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {



        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Brandon", 15, 5000));
        persons.add(new Person("Braney", 15, 15000));
        persons.add(new Person("Jack", 10, 5000));
        persons.add(new Person("Robin", 10, 500000));
        persons.add(new Person("Tony", 10, 1400000));

        Map<String, Aggregator<Person>> aggregatorMap = new HashMap<>();
        aggregatorMap.put("count", new CountAggregator<Person>());
        aggregatorMap.put("first", new FirstAggregator<Person>());

        Comparator<Person> comparator = new Comparator<Person>() {
            public int compare(final Person o1, final Person o2) {
                double diff = o1.getSalary() - o2.getSalary();

                if (diff == 0) {
                    return 0;
                }
                return diff > 0 ? -1 : 1;
            }
        };
        aggregatorMap.put("top2", new TopNAggregator<Person>( comparator , 2 ));
        Map<Object, Map<String, Object>> aggResults = GroupUtils.groupByProperty(persons, Person.class, "age", aggregatorMap);


        for (Object key : aggResults.keySet()) {
            System.out.println("Key:" + key);

            Map<String, Object> results = aggResults.get(key);
            for (String aggKey : results.keySet()) {
                System.out.println("     aggkey->" + results.get(aggKey));
            }
        }

    }

}
