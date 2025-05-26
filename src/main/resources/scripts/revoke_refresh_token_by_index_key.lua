local pattern = KEYS[1]

local indexKeys = redis.call("KEYS", pattern)

for _, idxKey in ipairs(indexKeys) do
    local tokenKeys = redis.call("SMEMBERS", idxKey)
    for _, tokenKey in ipairs(tokenKeys) do
        redis.call("HSET", tokenKey, "revoked", 1)
    end
    redis.call("SREM", idxKey, tokenKey)
end

return {
    ok = "REVOKED_ALL_TOKENS_BY_INDEX_KEY"
}
