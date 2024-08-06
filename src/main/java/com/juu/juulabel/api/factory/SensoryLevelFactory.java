package com.juu.juulabel.api.factory;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.Level;
import com.juu.juulabel.domain.dto.alcohol.SensoryLevel;
import com.juu.juulabel.domain.dto.alcohol.SensoryTypeInfo;
import com.juu.juulabel.domain.embedded.Sensory;
import com.juu.juulabel.domain.enums.Rateable;
import com.juu.juulabel.domain.enums.alcohol.sensory.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class SensoryLevelFactory {
    private final Map<SensoryType, Class<? extends Rateable>> sensoryMap;
    private final Map<SensoryType, Method> sensoryMethodMap;

    public SensoryLevelFactory() {
        sensoryMap = new EnumMap<>(SensoryType.class);
        sensoryMethodMap = new EnumMap<>(SensoryType.class);
        initialize();
    }

    private void initialize() {
        sensoryMap.put(SensoryType.CARBONATION, CarbonationLevel.class);
        sensoryMap.put(SensoryType.CLARITY, ClarityLevel.class);
        sensoryMap.put(SensoryType.DENSITY, DensityLevel.class);
        sensoryMap.put(SensoryType.SEDIMENT, SedimentLevel.class);
        sensoryMap.put(SensoryType.VISCOSITY, ViscosityLevel.class);
        sensoryMap.put(SensoryType.TURBIDITY, TurbidityLevel.class);

        try {
            sensoryMethodMap.put(SensoryType.CARBONATION, Sensory.SensoryBuilder.class.getMethod("carbonation", CarbonationLevel.class));
            sensoryMethodMap.put(SensoryType.CLARITY, Sensory.SensoryBuilder.class.getMethod("clarity", ClarityLevel.class));
            sensoryMethodMap.put(SensoryType.DENSITY, Sensory.SensoryBuilder.class.getMethod("density", DensityLevel.class));
            sensoryMethodMap.put(SensoryType.SEDIMENT, Sensory.SensoryBuilder.class.getMethod("sediment", SedimentLevel.class));
            sensoryMethodMap.put(SensoryType.VISCOSITY, Sensory.SensoryBuilder.class.getMethod("viscosity", ViscosityLevel.class));
            sensoryMethodMap.put(SensoryType.TURBIDITY, Sensory.SensoryBuilder.class.getMethod("turbidity", TurbidityLevel.class));
        } catch (NoSuchMethodException e) {
            throw new BaseException(e.getMessage(), ErrorCode.NO_SUCH_METHOD);
        }

    }

    public Rateable getRateableBySensoryTypeAndLevel(SensoryType sensoryType, String levelName) {
        Class<? extends Rateable> enumClass = getEnumClass(sensoryType);
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getName().equals(levelName))
                .findFirst()
                .orElseThrow(() -> new InvalidParamException("Invalid sensory level : " + levelName, ErrorCode.INVALID_SENSORY_LEVEL));
    }

    public void updateSensoryLevel(Sensory.SensoryBuilder sensoryBuilder, SensoryType sensoryType, Rateable level) {
        Method method = sensoryMethodMap.get(sensoryType);
        if (Objects.isNull(method)) {
            throw new InvalidParamException("Unknown sensory type: " + sensoryType);
        }
        try {
            method.invoke(sensoryBuilder, level);
        } catch (Exception e) {
            throw new InvalidParamException("Failed to build level for sensory type: " + sensoryType);
        }
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
//    public SensoryLevel getSensoryLevel(SensoryType sensoryType) {
//        Class<? extends Rateable> enumClass = sensoryMap.get(sensoryType);
//        if (Objects.isNull(enumClass)) {
//            throw new InvalidParamException(ErrorCode.INVALID_SENSORY_TYPE);
//        }
//
//        try {
//            Method getLevelsMethod = enumClass.getMethod("getLevels");
//            Rateable enumInstance = enumClass.getEnumConstants()[0];
//            @SuppressWarnings("unchecked")
//            List<Level> levels = (List<Level>) getLevelsMethod.invoke(enumInstance);
//            return new SensoryLevel(SensoryTypeInfo.of(sensoryType), levels);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            throw new BaseException("Failed to get levels for sensory type: " + sensoryType, ErrorCode.INVALID_SENSORY_TYPE);
//        }
//    }

    /**
     * 트러블 슈팅 (24.08.05) 정리 후 주석 제거
     * Rateable enumInstance 를 가져와 getLevels()를 호출하도록 변경
     */
    public SensoryLevel getSensoryLevel(SensoryType sensoryType) {
        Class<? extends Rateable> enumClass = getEnumClass(sensoryType);

        try {
            Rateable enumInstance = enumClass.getEnumConstants()[0];
            List<Level> levels = enumInstance.getLevels();
            return new SensoryLevel(SensoryTypeInfo.of(sensoryType), levels);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BaseException("Failed to get levels for sensory type: " + sensoryType, ErrorCode.INVALID_SENSORY_TYPE);
        }
    }

    public List<SensoryLevel> getAllSensoryLevel(List<SensoryType> sensoryTypes) {
        return sensoryTypes.stream()
                .map(this::getSensoryLevel)
                .toList();
    }

    private Class<? extends Rateable> getEnumClass(SensoryType sensoryType) {
        Class<? extends Rateable> enumClass = sensoryMap.get(sensoryType);
        if (Objects.isNull(enumClass)) {
            throw new InvalidParamException(ErrorCode.INVALID_SENSORY_TYPE);
        }
        return enumClass;
    }

}
