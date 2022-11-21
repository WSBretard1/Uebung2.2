package org.texttechnologylab.project.Uebung2.helper;

/**
 * Diese Klasse hilft den String richtig zu formatieren.
 */

public class StringHelper {

    public static String[] FIRSTNAME = {"Dr. h. c.", "Dr."};
    public static String[] REGEXCLEAN = {"\\(.*\\)"};

    public static String replaceList(String sInput, String[] replaceList){

        String rString = sInput;

        rString = rString.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}\\p{Z}]", " ");

        for (String s : replaceList) {
            rString = rString.replace(s, "");
        }
        rString = clean(rString);
        return rString;

    }

    public static String clean(String sInput){

        return sInput.trim();

    }

}
