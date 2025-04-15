package fr.glerious.uhcmanagerapi.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Methods {

    public static <K, V> HashMap<K, V> MergeList(List<K> l1, List<V> l2) {
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
}
