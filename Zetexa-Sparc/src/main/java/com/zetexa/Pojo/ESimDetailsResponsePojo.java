package com.zetexa.Pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ESimDetailsResponsePojo {
    private String message;
    private Integer statusCode;
    private Object payload;
    private boolean isSuccess;

}
