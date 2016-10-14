package com.sinyuk.yukdaily.utils.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Sinyuk on 16/8/25.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
}
