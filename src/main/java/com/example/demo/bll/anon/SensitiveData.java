package com.example.demo.bll.anon;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {
}
