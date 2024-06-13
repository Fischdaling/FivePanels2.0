package org.theShire.foundation;


import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Collection;
import java.util.function.Supplier;


public abstract class DomainAssertion<T> {
    //Variable Exceptions
    private static <E extends RuntimeException> E variableException(Class<E> clazz, String message) {
        try {
            java.lang.reflect.Constructor<E> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(message);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate exception", e);
        }
    }
    // Null Assertions -------------------------------------------------------------

    // public static Object isNotNull(Object value, String paramName) {
    public static <T, E extends RuntimeException> T isNotNull(T value, String paramName, Class<E> clazz) {
        if (value == null) {
            throw variableException(clazz, paramName + " cannot be null");
        }
        return value;
    }

    public static <T, E extends RuntimeException> T isEqual(T value1, T value2, String paramName, Class<E> clazz) {
        if (value1 == null || value2 == null) {
            throw variableException(clazz, paramName + " cannot be null");
        }
        if (!value1.equals(value2)) {
            throw variableException(clazz, paramName + " are not equal");
        }
        return value1;
    }

    public static <T, E extends RuntimeException> T isNotEqual(T value1, T value2, String paramName, Class<E> clazz) {
        if (value1 == null || value2 == null) {
            throw variableException(clazz, paramName + " cannot be null");
        }
        if (value1.equals(value2)) {
            throw variableException(clazz, paramName + " are equal");
        }
        return value1;
    }

    // String Assertions -----------------------------------------------------------

    public static <E extends RuntimeException> String isNotBlank(String value, String paramName, Class<E> clazz) {
        isNotNull(value, paramName, clazz);

        if (value.isBlank())
            throw variableException(clazz, paramName + " is blank");

        return value;
    }

    public static <E extends RuntimeException> String hasMaxLength(String value, int maxLength, String paramName, Class<E> clazz) {

        isNotBlank(value, paramName, clazz);

        if (value.length() > maxLength)
            throw variableException(clazz, paramName + " is greater than " + maxLength);

        return value;
    }

    public static <E extends RuntimeException> String hasMinLength(String value, int minLength, String paramName, Class<E> clazz) {
        isNotBlank(value, paramName, clazz);

        if (value.length() <= minLength)
            throw variableException(clazz, paramName + " is smaller than " + minLength);

        return value;
    }

    public static <E extends RuntimeException> String containsSymbol(String value, String paramName, Class<E> clazz, char symbol) {
        isNotBlank(value, paramName, clazz);
        if (!value.contains(String.valueOf(symbol))) {
            throw variableException(clazz, paramName + " does not contain " + symbol);
        }
        return value;
    }

    public static <E extends RuntimeException> String containsSymbols(String value, String paramName, Class<E> clazz, char... symbol) {
        for (int i = 0; i < symbol.length; i++) {
            containsSymbol(value, paramName, clazz, symbol[i]);
        }
        return value;
    }

    // number Assertions --------------------------------------------------------------
    public static <T extends Number & Comparable<T>, E extends RuntimeException> T greaterZero(T value, String paramName, Class<E> clazz) {
        return greaterZero(value, () -> paramName + " is smaller or Equal than 0", clazz);
    }

    public static <T extends Number & Comparable<T>, E extends RuntimeException> T greaterZero(T value, Supplier<String> errorMsg, Class<E> clazz) {

        if (value.doubleValue() <= 0) {
            throw variableException(clazz, errorMsg.get());
        }
        return value;
    }

    public static <T extends Number & Comparable<T>, E extends RuntimeException> T greaterEqualsZero(T value, String paramName, Class<E> clazz) {
        return greaterEqualsZero(value, () -> paramName + "is smaller to Zero", clazz);
    }

    public static <T extends Number & Comparable<T>, E extends RuntimeException> T greaterEqualsZero(T value, Supplier<String> errorMsg, Class<E> clazz) {
        isNotNull(value, errorMsg.get(), clazz);
        if (value.doubleValue() < 0) {
            throw variableException(clazz, errorMsg.get());
        }
        return value;
    }

    public static <T extends Number & Comparable<T>, E extends RuntimeException> T greaterThan(T value, T value1, String paramName, Class<E> clazz) {
        isNotNull(value, paramName, clazz);
        if (value.compareTo(value1) <= 0) {
            throw variableException(clazz, paramName + " is smaller than " + value1);
        }
        return value;
    }

    public static <T extends Number & Comparable<T>, E extends RuntimeException> T lesserThan(T value, T value1, String paramName, Class<E> clazz) {
        isNotNull(value, paramName, clazz);
        if (value.compareTo(value1) >= 0) {
            throw variableException(clazz, paramName + " is smaller than " + value1);
        }
        return value;
    }

    public static <T extends Number & Comparable<T>, E extends RuntimeException> T inRange(T value, T min, T max, String paramName, Class<E> clazz) {
        isNotNull(value, paramName, clazz);
        if (value.compareTo(max) > 0 || value.compareTo(min) < 0) {
            throw variableException(clazz, paramName + " is not in Range: " + min + "-" + max);
        }
        return value;
    }

    // Expression Assertions -------------------------------------------------------

    public static <E extends RuntimeException> void isTrue(boolean expression, Supplier<String> errorMsg, Class<E> clazz) {
        if (!expression)
            throw variableException(clazz, errorMsg.get());
    }

    // list Assertions -------------------------------------------------------------
    // Type erasure
    public static <T, E extends RuntimeException, C extends Collection<T>> T isInCollection(T o, C list, String paramName, Class<E> clazz) {
        return isInCollection(o, list, () -> paramName + " is not in collection", clazz);
    }

    public static <T, E extends RuntimeException, C extends Collection<T>> T isInCollection(T o, C list, Supplier<String> errorMsg, Class<E> clazz) {
        isNotNull(o, errorMsg.toString(), clazz);

        if (!list.contains(o)) {
            throw variableException(clazz, errorMsg.get());
        }
        return o;
    }

    public static <T, E extends RuntimeException, C extends Collection<T>> T isNotInCollection(T o, C list, String paramName, Class<E> clazz) {
        isNotNull(o, paramName, clazz);

        if (list.contains(o)) {
            throw variableException(clazz, paramName + " is existing in list");
        }
        return o;
    }

    // Time Assertions -------------------------------------------------------------
    public static <T extends Instant, E extends RuntimeException> T isBeforeNow(T time, String paramName, Class<E> clazz) {
        isNotNull(time, paramName, clazz);

        if (time.isBefore(Instant.now()))
            throw variableException(clazz, paramName + "is in the past");
        return time;
    }

    public static <T extends Instant, E extends RuntimeException> T isAfterTime(T time1, T time2, String paramName, Class<E> clazz) {
        isNotNull(time1, paramName, clazz);
        isNotNull(time2, paramName, clazz);
        if (time1.isBefore(time2))
            throw variableException(clazz, paramName + " time 1:" + time1 + " is before time 2: " + time2);

        return time1;
    }

    // Password Assertions--------------------------------------------------------------
    public static <E extends RuntimeException> String isZxcvbn3Confirm(String value, Supplier<String> errorMsg, Class<E> clazz) {
        isNotNull(value, errorMsg.get(), clazz);
        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(value);
        if (strength.getScore() < 3) {
            throw variableException(clazz, errorMsg.get());
        }
        return value;
    }

    //Email Assertions--------------------------------------------------------------------
    public static <E extends RuntimeException> String isValidEmail(String email, String paramName, Class<E> clazz) {
        hasMaxLength(email, 30, paramName, clazz);

        if (!email.contains("@")) {
            throw variableException(clazz, paramName + " doesn't contain @");
        }

        //can't be less then 1 bcs above already checked that
        String[] parts = email.split("@");
        if (parts.length != 2) {
            throw variableException(clazz, paramName + " does contain more then one @");
        }

        String leftPart = parts[0];
        String rightPart = parts[1];
        if (leftPart.isBlank()) {
            throw variableException(clazz, paramName + " there must be content before @");
        }

        if (rightPart.isBlank()) {
            throw variableException(clazz, paramName + " there must be content after @");
        }

        if (!rightPart.contains(".")) {
            throw variableException(clazz, paramName + " there must be a . after @");
        }

        return email;
    }
}

