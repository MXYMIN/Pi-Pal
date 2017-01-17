package com.example.android.pimemorize.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

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

    public static String boldFirstOccurrenceOfSubstring(String str, String substring) {
        if (str != null && str.length() > 0) {
            str = str.replaceFirst(Pattern.quote(substring), "<b>" + substring + "</b>");
        }
        return str;
    }

    public static String generateMaskedRowString(String str, int numberOfDigits) {
        if (str != null) {
            while (str.length() != numberOfDigits) {
                str += "?";
            }
            str = addSpacesInBetweenCharacters(str);
        }
        return str;
    }

    public static String readFromFile(Context context, String fileName) {

        String ret = "";

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(context.getClass().getSimpleName(), "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(context.getClass().getSimpleName(), "Can not read file: " + e.toString());
        }

        return ret;
    }
}
