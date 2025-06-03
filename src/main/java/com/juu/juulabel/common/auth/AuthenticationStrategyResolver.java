package com.juu.juulabel.common.auth;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resolves and applies appropriate authentication strategy for incoming
 * requests.
 * Coordinates multiple authentication strategies in priority order.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationStrategyResolver {

    private final List<AuthenticationStrategy> strategies;

    /**
     * Resolves authentication for the given request using available strategies
     * 
     * @param request HTTP request to authenticate
     * @return Authentication object or null if no authentication applies
     */
    public Optional<Authentication> resolveAuthentication(HttpServletRequest request) {
        // Skip OPTIONS requests
        if ("OPTIONS".equals(request.getMethod())) {
            log.trace("Skipping authentication for OPTIONS request: {}", request.getRequestURI());
            return Optional.empty();
        }

        // Find the first strategy that can handle this request (sorted by priority)
        Optional<AuthenticationStrategy> applicableStrategy = strategies.stream()
                .sorted(Comparator.comparingInt(AuthenticationStrategy::getOrder))
                .filter(strategy -> strategy.canHandle(request))
                .findFirst();

        if (applicableStrategy.isEmpty()) {
            log.trace("No authentication strategy found for request: {}", request.getRequestURI());
            return Optional.empty();
        }

        AuthenticationStrategy strategy = applicableStrategy.get();

        try {
            log.debug("Using {} strategy for request: {}",
                    strategy.getStrategyName(), request.getRequestURI());

            Authentication authentication = strategy.authenticate(request);

            if (authentication != null) {
                log.debug("Authentication successful using {} strategy for: {}",
                        strategy.getStrategyName(), authentication.getName());
            } else {
                log.debug("No authentication provided by {} strategy", strategy.getStrategyName());
            }

            return Optional.ofNullable(authentication);

        } catch (Exception e) {
            log.warn("Authentication failed using {} strategy for {}: {}",
                    strategy.getStrategyName(), request.getRequestURI(), e.getMessage());
            throw e; // Re-throw to let filter handle the exception
        }
    }

    /**
     * Returns the list of available authentication strategies for debugging
     */
    public List<String> getAvailableStrategies() {
        return strategies.stream()
                .sorted(Comparator.comparingInt(AuthenticationStrategy::getOrder))
                .map(AuthenticationStrategy::getStrategyName)
                .toList();
    }
}