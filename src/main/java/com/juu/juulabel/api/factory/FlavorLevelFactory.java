package com.juu.juulabel.api.factory;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.FlavorLevel;
import com.juu.juulabel.domain.dto.alcohol.FlavorTypeInfo;
import com.juu.juulabel.domain.dto.alcohol.Level;
import com.juu.juulabel.domain.embedded.Flavor;
import com.juu.juulabel.domain.enums.Rateable;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorCommonLevel;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class FlavorLevelFactory {
    private final Map<FlavorType, Class<? extends Rateable>> flavorMap;
    private final Map<FlavorType, Method> flavorMethodMap;

    public FlavorLevelFactory() {
        flavorMap = new EnumMap<>(FlavorType.class);
        flavorMethodMap = new EnumMap<>(FlavorType.class);
        initialize();
    }

    private void initialize() {
        flavorMap.put(FlavorType.SWEETNESS, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.SOURNESS, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.BITTERNESS, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.UMAMI, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.AFTERTASTE, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.BODY, FlavorCommonLevel.class);

        try {
            flavorMethodMap.put(FlavorType.SWEETNESS, Flavor.FlavorBuilder.class.getMethod("sweetness", FlavorCommonLevel.class));
            flavorMethodMap.put(FlavorType.SOURNESS, Flavor.FlavorBuilder.class.getMethod("sourness", FlavorCommonLevel.class));
            flavorMethodMap.put(FlavorType.BITTERNESS, Flavor.FlavorBuilder.class.getMethod("bitterness", FlavorCommonLevel.class));
            flavorMethodMap.put(FlavorType.UMAMI, Flavor.FlavorBuilder.class.getMethod("umami", FlavorCommonLevel.class));
            flavorMethodMap.put(FlavorType.AFTERTASTE, Flavor.FlavorBuilder.class.getMethod("aftertaste", FlavorCommonLevel.class));
            flavorMethodMap.put(FlavorType.BODY, Flavor.FlavorBuilder.class.getMethod("body", FlavorCommonLevel.class));
        } catch (NoSuchMethodException e) {
            throw new BaseException(e.getMessage(), ErrorCode.NO_SUCH_METHOD);
        }
    }

    public Rateable getRateableByFlavorTypeAndLevel(FlavorType flavorType, String levelName) {
        Class<? extends Rateable> enumClass = getEnumClass(flavorType);
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getName().equals(levelName))
                .findFirst()
                .orElseThrow(() -> new InvalidParamException("Invalid flavor level : " + levelName, ErrorCode.INVALID_FLAVOR_LEVEL));
    }

    public void buildLevel(Flavor.FlavorBuilder flavorBuilder, FlavorType flavorType, Rateable level) {
        Method method = flavorMethodMap.get(flavorType);
        if (Objects.isNull(method)) {
            throw new InvalidParamException("Unknown flavor type: " + flavorType);
        }
        try {
            method.invoke(flavorBuilder, level);
        } catch (Exception e) {
            throw new InvalidParamException("Failed to build level for flavor type: " + flavorType);
        }
    }

    public FlavorLevel getFlavorLevel(FlavorType flavorType) {
        Class<? extends Rateable> enumClass = getEnumClass(flavorType);

        try {
            Rateable enumInstance = enumClass.getEnumConstants()[0];
            List<Level> levels = enumInstance.getLevels();
            return new FlavorLevel(FlavorTypeInfo.of(flavorType), levels);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BaseException("Failed to get levels for flavor type: " + flavorType, ErrorCode.INVALID_FLAVOR_TYPE);
        }
    }

    public List<FlavorLevel> getAllFlavorLevel(List<FlavorType> flavorTypes) {
        return flavorTypes.stream()
                .map(this::getFlavorLevel)
                .toList();
    }

    private Class<? extends Rateable> getEnumClass(FlavorType flavorType) {
        Class<? extends Rateable> enumClass = flavorMap.get(flavorType);
        if (Objects.isNull(enumClass)) {
            throw new InvalidParamException(ErrorCode.INVALID_FLAVOR_TYPE);
        }
        return enumClass;
    }
}
