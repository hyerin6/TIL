package e2018.exam2;

import java.util.Arrays;

public class Exam14 {

    static void sort(int[] a) {
        Arrays.sort(a);
    }

    static boolean contains(int[] a, int value) {
        return Arrays.binarySearch(a, value) >= 0;
    }

    public static void main(String[] args) {
        int[] a = { 3, 8, 1, 2, 4, 5, 7 };

        sort(a);
        System.out.println(Arrays.toString(a));

        System.out.println(contains(a, 2));
        System.out.println(contains(a, 6));
    }
}
