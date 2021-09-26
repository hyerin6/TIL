package e2018.exam2;

import java.util.Arrays;

public class Exam15 {

    static void sort(int[] a) {
        Arrays.sort(a);
    }

    static int[] insert(int[] a, int value) {
        int index = Arrays.binarySearch(a, value);
        if (index >= 0) return a;
        index = -(index + 1);
        int[] a2 = Arrays.copyOf(a, a.length + 1);
        for (int i = a2.length - 1; i > index; --i)
            a2[i] = a2[i - 1];
        a2[index] = value;
        return a2;
    }

    public static void main(String[] args) {
        int[] a = { 3, 8, 1, 2, 4, 5, 7 };

        sort(a);
        System.out.println(Arrays.toString(a));

        a = insert(a, 2);
        a = insert(a, 6);
        System.out.println(Arrays.toString(a));
    }
}
