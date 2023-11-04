package app.dictionary;

import java.util.ArrayList;

public class HelperAlgorithm {
    public static boolean BinarySearch(ArrayList<String> array, String key) {
        int left = 0;
        int right = array.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array.get(mid).equals(key)) {
                return true;
            } else if (array.get(mid).compareTo(key) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return false;
    }
}
