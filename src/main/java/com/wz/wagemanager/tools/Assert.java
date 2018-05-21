package com.wz.wagemanager.tools;

import com.wz.wagemanager.exception.HandThrowException;

public class Assert {

    static public void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    public static void assertFalse(String message, boolean condition) {
        if (condition) {
            fail(message);
        }
    }

    static private void fail(String message) {
        if (message == null) {
            throw new HandThrowException ();
        }
        throw new HandThrowException(message);
    }

    static public void assertNotNull(String message, Object object) {
        assertTrue(message, object != null);
    }

    static public void assertNull(String message, Object object) {
        if (object == null) {
            return;
        }
        failNotNull(message, object);
    }

    static private void failNotNull(String message, Object actual) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }
        fail(formatted + "expected null, but was:<" + actual + ">");
    }

}
