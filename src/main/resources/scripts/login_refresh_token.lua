-- KEYS[1] = newTokenKey (e.g., "refresh_token:{hashedToken}")
-- KEYS[2] = indexKey (e.g., "refresh_index:{memberId}:{clientId}:{deviceId}")
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

-- Find all keys for the user+client+device
local oldTokenKeys = redis.call("SMEMBERS", indexKey)

-- Revoke all old tokens
for _, key in ipairs(oldTokenKeys) do
    if redis.call("EXISTS", key) == 1 then
        redis.call("HSET", key, "revoked", 1)
    else
        redis.call("SREM", indexKey, key) -- clean up dead keys
    end
end

-- Save the new token hash
redis.call("HSET", newTokenKey, "memberId", memberId, "clientId", clientId, "deviceId", deviceId, "ipAddress",
    ipAddress, "userAgent", userAgent, "revoked", 0)

-- Set TTL
redis.call("EXPIRE", newTokenKey, ttl)

-- Update the index with new token
redis.call("SADD", indexKey, newTokenKey)
redis.call("EXPIRE", indexKey, ttl)

return {
    ok = "LOGIN_SUCCESS"
}
