package com.juu.juulabel.auth.service;

public interface FraudDetectionService<T> {
    RiskAssessment assessRisk(T data,
            String currentIpAddress, String currentUserAgent, String currentDeviceId);
            
}
