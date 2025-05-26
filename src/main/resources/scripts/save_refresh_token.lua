-- KEYS[1] = newTokenKey (e.g., "RefreshToken:{hashedToken}")
-- KEYS[2] = indexKey (e.g., "RefreshIndex:{memberId}:{clientId}:{deviceId}")
-- ARGV[1] = memberId
-- ARGV[2] = clientId
-- ARGV[3] = deviceId
-- ARGV[4] = ipAddress
-- ARGV[5] = userAgent
-- ARGV[6] = ttl in seconds
local newTokenKey = KEYS[1]
local indexKey = KEYS[2]

local memberId = ARGV[1]
local clientId = ARGV[2]
local deviceId = ARGV[3]
local ipAddress = ARGV[4]
local userAgent = ARGV[5]
local ttl = tonumber(ARGV[6])

-- Store token
-- Save the new token hash
redis.call("HSET", newTokenKey, "memberId", memberId, "clientId", clientId, "deviceId", deviceId, "ipAddress",
    ipAddress, "userAgent", userAgent, "revoked", 0)
redis.call("EXPIRE", newTokenKey, ttl)

-- Update index with limited size
redis.call("SADD", indexKey, newTokenKey)
redis.call("EXPIRE", indexKey, ttl)

return {
    ok = "SAVE_REFRESH_TOKEN_SUCCESS"
}
