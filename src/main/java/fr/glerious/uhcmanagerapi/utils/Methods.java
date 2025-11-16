package fr.glerious.uhcmanagerapi.utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Methods {

    public static <K, V> HashMap<K, V> list2Hash(List<K> l1, List<V> l2) {
        HashMap<K, V> h = new HashMap<>();
        Iterator<K> i1  = l1.iterator();
        Iterator<V> i2 = l2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            h.put(i1.next(), i2.next());
        }
        return h;
    }

    @SafeVarargs
    public static <A> boolean isOneOf(A a, A... othersA) {
        return Arrays.asList(othersA).contains(a);
    }

    public static String stylized(String string) {
        return string.replace("&", "§");
    }

    public static List<Integer> rangedList(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

    public static <A> List<A> concatList(List<A> list, List<A> otherList) {
        return Stream.concat(list.stream(), otherList.stream()).collect(Collectors.toList());
    }

    public static <A> A filter(List<A> aList, Predicate<A> aPredicate) {
        return aList.stream().filter(aPredicate).collect(Collectors.toList()).get(0);
    }

    public static Integer seconds2ticks(float seconds) {
        float returned = 20 * seconds;
        return Math.round(returned);
    }

    public static Integer seconds2ticks(double seconds) {
        double returned = 20 * seconds;
        return Math.toIntExact(Math.round(returned));
    }
}
