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

    public static String convertToHTML(String addWord, String addPron, String addDescription, String type) {
        if (addPron.isEmpty()) {
            addPron = "/No pronunciation/";
        }
        if (addDescription.isEmpty()) {
            addDescription = "/No description/";
        }
        if (type.isEmpty()) {
            type = "/No type/";
        }
        if (addWord.isEmpty()) {
            addWord = "no word";
        }
        StringBuilder convertHTML = new StringBuilder();
        convertHTML.append("<h1>").append(addWord).append("</h1>");
        convertHTML.append("<h3><i>/").append(addPron).append("/</i></h3>");
        convertHTML.append("<h2>").append(type).append("</h2>");
        convertHTML.append("<ul><li>").append(addDescription).append("</li></ul>");
        return convertHTML.toString();
    }
}
