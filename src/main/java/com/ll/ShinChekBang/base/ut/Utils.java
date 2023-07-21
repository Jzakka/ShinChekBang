package com.ll.ShinChekBang.base.ut;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.service.BaseService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    public static float round(float number, int pos) {
        BigDecimal value = new BigDecimal(String.valueOf(number));
        BigDecimal rounded = value.setScale(pos, RoundingMode.HALF_UP);
        return rounded.floatValue();
    }

    public static <T> T getData(Long dataId, BaseService<T> service) {
        RsData<T> findResult = service.findOne(dataId);
        if (findResult.isFail()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, findResult.getMsg());
        }
        return findResult.getData();
    }

    @SneakyThrows
    public static String getPGAuthorizations(String secretKey) {
        return new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));
    }
}
