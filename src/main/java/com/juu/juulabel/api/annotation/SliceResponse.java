package com.juu.juulabel.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SliceResponse 어노테이션은 Slice<T>를 처리하여 isLast, List<T> 형식의 응답으로 변환하는 데 사용됩니다.
 * content() : 처리할 콘텐츠의 클래스 타입을 지정합니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SliceResponse {
    Class<?> content();
}
