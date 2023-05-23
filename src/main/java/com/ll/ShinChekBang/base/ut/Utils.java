package com.ll.ShinChekBang.base.ut;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {
    public static float round(float number, int pos) {
        BigDecimal value = new BigDecimal(String.valueOf(number));
        BigDecimal rounded = value.setScale(pos, RoundingMode.HALF_UP);
        return rounded.floatValue();
    }
}
