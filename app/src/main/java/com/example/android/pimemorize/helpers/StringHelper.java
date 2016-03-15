package com.example.android.pimemorize.helpers;

/**
 * Created by michael on 2016-03-13.
 */
public class StringHelper {
    public static String[] splitStringEvery(String str, int interval) {

        // Get final array length and initialize return string array
        int arrayLength = (int) Math.ceil(((str.length() / (double)interval)));
        String[] result = new String[arrayLength];

        // j keeps track of starting index in string for new element of array
        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = str.substring(j, j + interval);
            j += interval;
        }
        // Add the last bit
        result[lastIndex] = str.substring(j);

        return result;
    }

    public static String removeLastCharacter(String str) {
        // Check for null and empty string
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String addSpacesInBetweenCharacters(String str) {
        // Check for null and empty string
        if (str != null && str.length() > 0) {
            // Regex to replace any character that has a following character '.(?=).' with that matched character '$0' and a following space
            str = str.replaceAll(".(?=.)", "$0 ");
        }
        return str;
    }
}
