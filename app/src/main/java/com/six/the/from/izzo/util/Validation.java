package com.six.the.from.izzo.util;


public class Validation {
    public static boolean hasText(IzzoEditText izzoEditText) {
        String text = izzoEditText.getText().toString().trim();
        izzoEditText.setError(null);
        if (text.length() == 0) {
            izzoEditText.setError("");
            return false;
        }
        return true;
    }
}
