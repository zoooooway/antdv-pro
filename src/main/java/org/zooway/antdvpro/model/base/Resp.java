package org.zooway.antdvpro.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resp {

    private Object data;
    private Meta meta;

    public static Resp ok(Object data) {
        return new Resp(data, new Meta(200, "success"));
    }


    public static Resp ok() {
        return new Resp(null, new Meta(200, "success"));
    }

    public static Resp error(int code, String message) {
        return new Resp(null, new Meta(code, message));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        private int code;
        private String message;
    }
}
