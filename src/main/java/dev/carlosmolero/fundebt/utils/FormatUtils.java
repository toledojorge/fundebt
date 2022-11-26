package dev.carlosmolero.fundebt.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class FormatUtils {
    public Float truncate2Decimals(Float value) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.floatValue();
    }
}
