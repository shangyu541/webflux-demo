package com.webflux.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/22
 * @since 1.0.0
 */
@Data
public class PasswordDTO {
    private String raw;
    private String secured;

    @JsonCreator
    public PasswordDTO(@JsonProperty("raw") String raw, @JsonProperty("secured") String secured) {
        this.raw = raw;
        this.secured = secured;
    }
}
