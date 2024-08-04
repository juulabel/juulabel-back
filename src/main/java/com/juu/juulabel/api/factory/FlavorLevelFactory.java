package com.juu.juulabel.api.factory;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.FlavorLevel;
import com.juu.juulabel.domain.dto.alcohol.FlavorTypeInfo;
import com.juu.juulabel.domain.dto.alcohol.Level;
import com.juu.juulabel.domain.enums.Rateable;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorCommonLevel;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class FlavorLevelFactory {
    private final Map<FlavorType, Class<? extends Rateable>> flavorMap;

    public FlavorLevelFactory() {
        flavorMap = new EnumMap<>(FlavorType.class);
        initialize();
    }

    private void initialize() {
        flavorMap.put(FlavorType.SWEETNESS, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.SOURNESS, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.BITTERNESS, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.UMAMI, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.AFTERTASTE, FlavorCommonLevel.class);
        flavorMap.put(FlavorType.BODY, FlavorCommonLevel.class);
    }

    public FlavorLevel getFlavorLevel(FlavorType flavorType) {
        Class<? extends Rateable> enumClass = flavorMap.get(flavorType);
        if (Objects.isNull(enumClass)) {
            throw new InvalidParamException(ErrorCode.INVALID_FLAVOR_TYPE);
        }

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

}
