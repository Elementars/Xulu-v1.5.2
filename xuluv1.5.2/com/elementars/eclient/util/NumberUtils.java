// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;

public class NumberUtils
{
    public static final Long LONG_ZERO;
    public static final Long LONG_ONE;
    public static final Long LONG_MINUS_ONE;
    public static final Integer INTEGER_ZERO;
    public static final Integer INTEGER_ONE;
    public static final Integer INTEGER_MINUS_ONE;
    public static final Short SHORT_ZERO;
    public static final Short SHORT_ONE;
    public static final Short SHORT_MINUS_ONE;
    public static final Byte BYTE_ZERO;
    public static final Byte BYTE_ONE;
    public static final Byte BYTE_MINUS_ONE;
    public static final Double DOUBLE_ZERO;
    public static final Double DOUBLE_ONE;
    public static final Double DOUBLE_MINUS_ONE;
    public static final Float FLOAT_ZERO;
    public static final Float FLOAT_ONE;
    public static final Float FLOAT_MINUS_ONE;
    
    public static int toInt(final String str) {
        return toInt(str, 0);
    }
    
    public static int toInt(final String str, final int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static long toLong(final String str) {
        return toLong(str, 0L);
    }
    
    public static long toLong(final String str, final long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        }
        catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static float toFloat(final String str) {
        return toFloat(str, 0.0f);
    }
    
    public static float toFloat(final String str, final float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        }
        catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static double toDouble(final String str) {
        return toDouble(str, 0.0);
    }
    
    public static double toDouble(final String str, final double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static byte toByte(final String str) {
        return toByte(str, (byte)0);
    }
    
    public static byte toByte(final String str, final byte defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(str);
        }
        catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static short toShort(final String str) {
        return toShort(str, (short)0);
    }
    
    public static short toShort(final String str, final short defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(str);
        }
        catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static Number createNumber(final String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank((CharSequence)str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        final String[] hex_prefixes = { "0x", "0X", "-0x", "-0X", "#", "-#" };
        int pfxLen = 0;
        for (final String pfx : hex_prefixes) {
            if (str.startsWith(pfx)) {
                pfxLen += pfx.length();
                break;
            }
        }
        if (pfxLen > 0) {
            char firstSigDigit = '\0';
            for (int i = pfxLen; i < str.length(); ++i) {
                firstSigDigit = str.charAt(i);
                if (firstSigDigit != '0') {
                    break;
                }
                ++pfxLen;
            }
            final int hexDigits = str.length() - pfxLen;
            if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) {
                return createBigInteger(str);
            }
            if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) {
                return createLong(str);
            }
            return createInteger(str);
        }
        else {
            final char lastChar = str.charAt(str.length() - 1);
            final int decPos = str.indexOf(46);
            final int expPos = str.indexOf(101) + str.indexOf(69) + 1;
            String dec;
            String mant;
            if (decPos > -1) {
                if (expPos > -1) {
                    if (expPos < decPos || expPos > str.length()) {
                        throw new NumberFormatException(str + " is not a valid number.");
                    }
                    dec = str.substring(decPos + 1, expPos);
                }
                else {
                    dec = str.substring(decPos + 1);
                }
                mant = getMantissa(str, decPos);
            }
            else {
                if (expPos > -1) {
                    if (expPos > str.length()) {
                        throw new NumberFormatException(str + " is not a valid number.");
                    }
                    mant = getMantissa(str, expPos);
                }
                else {
                    mant = getMantissa(str);
                }
                dec = null;
            }
            if (!Character.isDigit(lastChar) && lastChar != '.') {
                String exp;
                if (expPos > -1 && expPos < str.length() - 1) {
                    exp = str.substring(expPos + 1, str.length() - 1);
                }
                else {
                    exp = null;
                }
                final String numeric = str.substring(0, str.length() - 1);
                final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
                switch (lastChar) {
                    case 'S': {
                        if (dec == null && exp == null) {
                            if (numeric.charAt(0) != '-' || !isDigits(numeric.substring(1))) {
                                if (!isDigits(numeric)) {
                                    throw new NumberFormatException(str + " is not a valid number.");
                                }
                            }
                            try {
                                return createShort(numeric);
                            }
                            catch (NumberFormatException ex) {
                                return createBigInteger(numeric);
                            }
                        }
                        throw new NumberFormatException(str + " is not a valid number.");
                    }
                    case 'L':
                    case 'l': {
                        if (dec == null && exp == null) {
                            if (numeric.charAt(0) != '-' || !isDigits(numeric.substring(1))) {
                                if (!isDigits(numeric)) {
                                    throw new NumberFormatException(str + " is not a valid number.");
                                }
                            }
                            try {
                                return createLong(numeric);
                            }
                            catch (NumberFormatException ex2) {
                                return createBigInteger(numeric);
                            }
                        }
                        throw new NumberFormatException(str + " is not a valid number.");
                    }
                    case 'F':
                    case 'f': {
                        try {
                            final Float f = createFloat(str);
                            if (!f.isInfinite() && (f != 0.0f || allZeros)) {
                                return f;
                            }
                        }
                        catch (NumberFormatException ex3) {}
                    }
                    case 'D':
                    case 'd': {
                        try {
                            final Double d = createDouble(str);
                            if (!d.isInfinite() && (d.floatValue() != 0.0 || allZeros)) {
                                return d;
                            }
                        }
                        catch (NumberFormatException ex4) {}
                        try {
                            return createBigDecimal(numeric);
                        }
                        catch (NumberFormatException ex5) {}
                        break;
                    }
                }
                throw new NumberFormatException(str + " is not a valid number.");
            }
            String exp;
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring(expPos + 1, str.length());
            }
            else {
                exp = null;
            }
            if (dec == null && exp == null) {
                try {
                    return createInteger(str);
                }
                catch (NumberFormatException ex6) {
                    try {
                        return createLong(str);
                    }
                    catch (NumberFormatException ex7) {
                        return createBigInteger(str);
                    }
                }
            }
            final boolean allZeros2 = isAllZeros(mant) && isAllZeros(exp);
            try {
                final Float f2 = createFloat(str);
                final Double d = createDouble(str);
                if (!f2.isInfinite() && (f2 != 0.0f || allZeros2) && f2.toString().equals(d.toString())) {
                    return f2;
                }
                if (!d.isInfinite() && (d != 0.0 || allZeros2)) {
                    final BigDecimal b = createBigDecimal(str);
                    if (b.compareTo(BigDecimal.valueOf(d)) == 0) {
                        return d;
                    }
                    return b;
                }
            }
            catch (NumberFormatException ex8) {}
            return createBigDecimal(str);
        }
    }
    
    private static String getMantissa(final String str) {
        return getMantissa(str, str.length());
    }
    
    private static String getMantissa(final String str, final int stopPos) {
        final char firstChar = str.charAt(0);
        final boolean hasSign = firstChar == '-' || firstChar == '+';
        return hasSign ? str.substring(1, stopPos) : str.substring(0, stopPos);
    }
    
    private static boolean isAllZeros(final String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; --i) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        return str.length() > 0;
    }
    
    public static Float createFloat(final String str) {
        if (str == null) {
            return null;
        }
        return Float.valueOf(str);
    }
    
    public static Short createShort(final String str) {
        if (str == null) {
            return null;
        }
        return Short.valueOf(str);
    }
    
    public static Double createDouble(final String str) {
        if (str == null) {
            return null;
        }
        return Double.valueOf(str);
    }
    
    public static Integer createInteger(final String str) {
        if (str == null) {
            return null;
        }
        return Integer.decode(str);
    }
    
    public static Long createLong(final String str) {
        if (str == null) {
            return null;
        }
        return Long.decode(str);
    }
    
    public static BigInteger createBigInteger(final String str) {
        if (str == null) {
            return null;
        }
        int pos = 0;
        int radix = 10;
        boolean negate = false;
        if (str.startsWith("-")) {
            negate = true;
            pos = 1;
        }
        if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) {
            radix = 16;
            pos += 2;
        }
        else if (str.startsWith("#", pos)) {
            radix = 16;
            ++pos;
        }
        else if (str.startsWith("0", pos) && str.length() > pos + 1) {
            radix = 8;
            ++pos;
        }
        final BigInteger value = new BigInteger(str.substring(pos), radix);
        return negate ? value.negate() : value;
    }
    
    public static BigDecimal createBigDecimal(final String str) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank((CharSequence)str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        if (str.trim().startsWith("--")) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        return new BigDecimal(str);
    }
    
    public static long min(final long... array) {
        validateArray(array);
        long min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static int min(final int... array) {
        validateArray(array);
        int min = array[0];
        for (int j = 1; j < array.length; ++j) {
            if (array[j] < min) {
                min = array[j];
            }
        }
        return min;
    }
    
    public static short min(final short... array) {
        validateArray(array);
        short min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static byte min(final byte... array) {
        validateArray(array);
        byte min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static double min(final double... array) {
        validateArray(array);
        double min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (Double.isNaN(array[i])) {
                return Double.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static float min(final float... array) {
        validateArray(array);
        float min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (Float.isNaN(array[i])) {
                return Float.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static long max(final long... array) {
        validateArray(array);
        long max = array[0];
        for (int j = 1; j < array.length; ++j) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }
    
    public static int max(final int... array) {
        validateArray(array);
        int max = array[0];
        for (int j = 1; j < array.length; ++j) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }
    
    public static short max(final short... array) {
        validateArray(array);
        short max = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    public static byte max(final byte... array) {
        validateArray(array);
        byte max = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    public static double max(final double... array) {
        validateArray(array);
        double max = array[0];
        for (int j = 1; j < array.length; ++j) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }
    
    public static float max(final float... array) {
        validateArray(array);
        float max = array[0];
        for (int j = 1; j < array.length; ++j) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }
    
    private static void validateArray(final Object array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        Validate.isTrue(Array.getLength(array) != 0, "Array cannot be empty.", new Object[0]);
    }
    
    public static long min(long a, final long b, final long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }
    
    public static int min(int a, final int b, final int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }
    
    public static short min(short a, final short b, final short c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }
    
    public static byte min(byte a, final byte b, final byte c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }
    
    public static double min(final double a, final double b, final double c) {
        return Math.min(Math.min(a, b), c);
    }
    
    public static float min(final float a, final float b, final float c) {
        return Math.min(Math.min(a, b), c);
    }
    
    public static long max(long a, final long b, final long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }
    
    public static int max(int a, final int b, final int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }
    
    public static short max(short a, final short b, final short c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }
    
    public static byte max(byte a, final byte b, final byte c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }
    
    public static double max(final double a, final double b, final double c) {
        return Math.max(Math.max(a, b), c);
    }
    
    public static float max(final float a, final float b, final float c) {
        return Math.max(Math.max(a, b), c);
    }
    
    public static boolean isDigits(final String str) {
        return StringUtils.isNumeric((CharSequence)str);
    }
    
    @Deprecated
    public static boolean isNumber(final String str) {
        return isCreatable(str);
    }
    
    public static boolean isCreatable(final String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        final int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
        final boolean hasLeadingPlusSign = start == 1 && chars[0] == '+';
        if (sz > start + 1 && chars[start] == '0') {
            if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
                int i = start + 2;
                if (i == sz) {
                    return false;
                }
                while (i < chars.length) {
                    if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                    ++i;
                }
                return true;
            }
            else if (Character.isDigit(chars[start + 1])) {
                for (int i = start + 1; i < chars.length; ++i) {
                    if (chars[i] < '0' || chars[i] > '7') {
                        return false;
                    }
                }
                return true;
            }
        }
        --sz;
        int i;
        for (i = start; i < sz || (i < sz + 1 && allowSigns && !foundDigit); ++i) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;
            }
            else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                hasDecPoint = true;
            }
            else if (chars[i] == 'e' || chars[i] == 'E') {
                if (hasExp) {
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            }
            else {
                if (chars[i] != '+' && chars[i] != '-') {
                    return false;
                }
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
            }
        }
        if (i >= chars.length) {
            return !allowSigns && foundDigit;
        }
        if (chars[i] >= '0' && chars[i] <= '9') {
            return !SystemUtils.IS_JAVA_1_6 || !hasLeadingPlusSign || hasDecPoint;
        }
        if (chars[i] == 'e' || chars[i] == 'E') {
            return false;
        }
        if (chars[i] == '.') {
            return !hasDecPoint && !hasExp && foundDigit;
        }
        if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
            return foundDigit;
        }
        return (chars[i] == 'l' || chars[i] == 'L') && foundDigit && !hasExp && !hasDecPoint;
    }
    
    public static boolean isParsable(final String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return false;
        }
        if (str.charAt(str.length() - 1) == '.') {
            return false;
        }
        if (str.charAt(0) == '-') {
            return str.length() != 1 && withDecimalsParsing(str, 1);
        }
        return withDecimalsParsing(str, 0);
    }
    
    private static boolean withDecimalsParsing(final String str, final int beginIdx) {
        int decimalPoints = 0;
        for (int i = beginIdx; i < str.length(); ++i) {
            final boolean isDecimalPoint = str.charAt(i) == '.';
            if (isDecimalPoint) {
                ++decimalPoints;
            }
            if (decimalPoints > 1) {
                return false;
            }
            if (!isDecimalPoint && !Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static int compare(final int x, final int y) {
        if (x == y) {
            return 0;
        }
        return (x < y) ? -1 : 1;
    }
    
    public static int compare(final long x, final long y) {
        if (x == y) {
            return 0;
        }
        return (x < y) ? -1 : 1;
    }
    
    public static int compare(final short x, final short y) {
        if (x == y) {
            return 0;
        }
        return (x < y) ? -1 : 1;
    }
    
    public static int compare(final byte x, final byte y) {
        return x - y;
    }
    
    static {
        LONG_ZERO = 0L;
        LONG_ONE = 1L;
        LONG_MINUS_ONE = -1L;
        INTEGER_ZERO = 0;
        INTEGER_ONE = 1;
        INTEGER_MINUS_ONE = -1;
        SHORT_ZERO = 0;
        SHORT_ONE = 1;
        SHORT_MINUS_ONE = -1;
        BYTE_ZERO = 0;
        BYTE_ONE = 1;
        BYTE_MINUS_ONE = -1;
        DOUBLE_ZERO = 0.0;
        DOUBLE_ONE = 1.0;
        DOUBLE_MINUS_ONE = -1.0;
        FLOAT_ZERO = 0.0f;
        FLOAT_ONE = 1.0f;
        FLOAT_MINUS_ONE = -1.0f;
    }
}
