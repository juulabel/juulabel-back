-- KEYS[1] = new token key (e.g., "refresh_token:{hashedToken}")
-- KEYS[2] = indexKey (e.g., "refresh_index:{memberId}:{clientId}:{deviceId}")
-- KEYS[3] = old token key (e.g., "refresh_token:{hashedToken}")
-- ARGV[1] = memberId
-- ARGV[2] = clientId
-- ARGV[3] = deviceId
-- ARGV[4] = ipAddress
-- ARGV[5] = userAgent
-- ARGV[6] = ttl in seconds
local newTokenKey = KEYS[1]
local indexKey = KEYS[2]
local oldTokenKey = KEYS[3]
local memberId = ARGV[1]
local clientId = ARGV[2]
local deviceId = ARGV[3]
local ipAddress = ARGV[4]
local userAgent = ARGV[5]
local ttl = tonumber(ARGV[6])

-- Helper function to revoke all member tokens
local function revokeAllMemberTokens(memberId)    
    local cursor = "0"
    local pattern = "refresh_index:" .. memberId .. ":*"

    repeat
        local result = redis.call("SCAN", cursor, "MATCH", pattern, "COUNT", 100)
        cursor = result[1]
        local indexKeys = result[2]

        for _, idxKey in ipairs(indexKeys) do
            local tokenKeys = redis.call("SMEMBERS", idxKey)

            -- Batch revoke tokens (max 100 at a time to avoid large commands)
            for i = 1, #tokenKeys, 100 do
                local batch = {}
                local endIdx = math.min(i + 99, #tokenKeys)

                for j = i, endIdx do
                    table.insert(batch, "HSET")
                    table.insert(batch, tokenKeys[j])
                    table.insert(batch, "revoked")
                    table.insert(batch, 1)
                end

                if #batch > 0 then
                    redis.call(unpack(batch))
                end
            end

            -- Clean up index
            redis.call("DEL", idxKey)
        end
    until cursor == "0"
end

-- Check if old token exists and get all fields at once
local oldToken = redis.call("HMGET", oldTokenKey, "revoked", "deviceId")
if not oldToken[1] and not oldToken[2] then
    return {
        err = "OLD_TOKEN_NOT_FOUND"
    }
end

-- Check if token is already revoked (using direct array access)
if oldToken[1] == "1" then
    revokeAllMemberTokens(memberId)
    return {
        err = "OLD_TOKEN_ALREADY_REVOKED_ALL_TOKENS_INVALIDATED"
    }
end

-- Check device ID mismatch (using direct array access)
if oldToken[2] ~= deviceId then
    revokeAllMemberTokens(memberId)
    return {
        err = "DEVICE_ID_MISMATCH"
    }
end

-- Revoke old token
redis.call("HSET", oldTokenKey, "revoked", 1)
redis.call("SREM", indexKey, oldTokenKey)

-- Store new token with all fields at once
redis.call("HSET", newTokenKey, "memberId", memberId, "clientId", clientId, "deviceId", deviceId, "ipAddress",
    ipAddress, "userAgent", userAgent, "revoked", 0)

-- Set expiration and update index
redis.call("EXPIRE", newTokenKey, ttl)
redis.call("SADD", indexKey, newTokenKey)
redis.call("EXPIRE", indexKey, ttl)

return {
    ok = "ROTATION_SUCCESS"
}
