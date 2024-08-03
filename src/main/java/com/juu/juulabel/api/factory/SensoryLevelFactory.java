package com.juu.juulabel.api.factory;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.Level;
import com.juu.juulabel.domain.dto.alcohol.SensoryLevel;
import com.juu.juulabel.domain.dto.alcohol.SensoryTypeInfo;
import com.juu.juulabel.domain.enums.Rateable;
import com.juu.juulabel.domain.enums.alcohol.sensory.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SensoryLevelFactory {
    private final Map<SensoryType, Class<? extends Rateable>> sensoryMap;

    public SensoryLevelFactory() {
        sensoryMap = new EnumMap<>(SensoryType.class);
        initialize();
    }

    private void initialize() {
        sensoryMap.put(SensoryType.CARBONATION, CarbonationLevel.class);
        sensoryMap.put(SensoryType.CLARITY, ClarityLevel.class);
        sensoryMap.put(SensoryType.DENSITY, DensityLevel.class);
        sensoryMap.put(SensoryType.SEDIMENT, SedimentLevel.class);
        sensoryMap.put(SensoryType.VISCOSITY, ViscosityLevel.class);
        sensoryMap.put(SensoryType.TURBIDITY, TurbidityLevel.class);
    }

//    public SensoryLevel getSensoryLevel(SensoryType sensoryType) {
//        Class<? extends Rateable> enumClass = sensoryMap.get(sensoryType);
//        if (Objects.isNull(enumClass)) {
//            throw new InvalidParamException(ErrorCode.INVALID_SENSORY_TYPE);
//        }
//        try {
//            Method getLevelsMethod = enumClass.getMethod("getLevels");
//            @SuppressWarnings("unchecked")
//            List<Level> levels = (List<Level>) getLevelsMethod.invoke(null);
//            return new SensoryLevel(sensoryType, levels);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            throw new BaseException("Failed to get levels for sensory type: " + sensoryType, ErrorCode.INVALID_SENSORY_TYPE);
//        }
//    }

    /**
     * 트러블 슈팅 (24.08.04) 정리 후 주석 제거
     * List<Level> levels = (List<Level>) getLevelsMethod.invoke(null);
     * Cannot invoke "Object.getClass()" because "obj" is null 발생
     * Rateable getLevels() 실행시 this.getClass().getEnumConstants()가 실행되기 위해 EnumConstants 부여
     */
    public SensoryLevel getSensoryLevel(SensoryType sensoryType) {
        Class<? extends Rateable> enumClass = sensoryMap.get(sensoryType);
        if (Objects.isNull(enumClass)) {
            throw new InvalidParamException(ErrorCode.INVALID_SENSORY_TYPE);
        }
        try {
            Method getLevelsMethod = enumClass.getMethod("getLevels");
            Rateable enumInstance = enumClass.getEnumConstants()[0];
            @SuppressWarnings("unchecked")
            List<Level> levels = (List<Level>) getLevelsMethod.invoke(enumInstance);
            return new SensoryLevel(SensoryTypeInfo.of(sensoryType), levels);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new BaseException("Failed to get levels for sensory type: " + sensoryType, ErrorCode.INVALID_SENSORY_TYPE);
        }
    }

    public List<SensoryLevel> getAllSensoryLevel(List<SensoryType> sensoryTypes) {
        return sensoryTypes.stream()
                .map(this::getSensoryLevel)
                .toList();
    }

}
