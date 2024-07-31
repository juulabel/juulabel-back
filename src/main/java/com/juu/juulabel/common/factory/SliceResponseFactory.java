package com.juu.juulabel.common.factory;

import com.juu.juulabel.api.annotation.SliceResponse;
import com.juu.juulabel.common.constants.FileConstants;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SliceResponseFactory {

    public static <T> T create(Class<T> responseClass, boolean isLast, List<?> contentList) {
        if (responseClass.isAnnotationPresent(SliceResponse.class)) {
            SliceResponse annotation = responseClass.getAnnotation(SliceResponse.class);
            Class<?> contentClass = annotation.content();

            if (Objects.isNull(contentList)) {
                throw new InvalidParamException(ErrorCode.IS_NULL);
            }

            if (!contentList.isEmpty()) {
                Object firstElement = contentList.getFirst();
                Class<?> elementClass = firstElement.getClass();
                if (!contentClass.isAssignableFrom(elementClass)) {
                    throw new InvalidParamException(
                            "Invalid content list element type." +
                                    FileConstants.LINE_SEPARATOR +
                                    "Expected : " + contentClass.getSimpleName() +
                                    FileConstants.LINE_SEPARATOR +
                                    "Found : " + elementClass.getSimpleName()
                    );
                }
            }
            try {
                Constructor<T> constructor = responseClass.getDeclaredConstructor(boolean.class, List.class);
                return constructor.newInstance(isLast, contentList);
            } catch (Exception e) {
                throw new BaseException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new InvalidParamException(
                    "Annotations are missing." +
                            FileConstants.LINE_SEPARATOR +
                            "Class : " + responseClass.getSimpleName()
            );
        }

    }
}