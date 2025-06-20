package com.zetexa.Pojo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatesThreadLocal {

    ThreadLocal<String> fromDateThread = new ThreadLocal<>();
    ThreadLocal<String> toDateThread = new ThreadLocal<>();


}
