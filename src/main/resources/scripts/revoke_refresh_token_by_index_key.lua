local pattern = KEYS[1]

-- Use SCAN instead of KEYS for better performance with large datasets
local cursor = "0"
local batchSize = 100

repeat
    local result = redis.call("SCAN", cursor, "MATCH", pattern, "COUNT", batchSize)
    cursor = result[1]
    local indexKeys = result[2]

    for _, idxKey in ipairs(indexKeys) do
        local tokenKeys = redis.call("SMEMBERS", idxKey)
        
        -- Batch revoke tokens to reduce Redis calls
        if #tokenKeys > 0 then
            for i = 1, #tokenKeys, batchSize do
                local batch = {}
                local endIdx = math.min(i + batchSize - 1, #tokenKeys)
                
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
            
            -- Clean up the index key
            redis.call("DEL", idxKey)
        end
    end
until cursor == "0"

return {
    ok = "REVOKED_ALL_TOKENS_BY_INDEX_KEY"
}
