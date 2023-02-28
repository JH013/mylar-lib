-- 测试集合返回

local unpack = unpack or table.unpack

-- 解决原生 pack 的 nil 截断问题
local function safePack(...)
    local params = {...}
    params.n = select('#', ...)
    return params
end

-- 解决原生 unpack 的 nil 截断问题
local function safeUnpack(safe_pack_tb)
    return unpack(safe_pack_tb, 1, safe_pack_tb.n)
end

-- local temp = safePack('123', '456', nil, '789')
-- return safeUnpack(temp)

-- return {'123', nil, '456', nil, '789' }

-- local ret = {'123', nil, '456', nil, '789' }
-- ret.n = select('#', ret)
-- return ret

-- local ret = {}
-- table.insert(ret, '123')
-- table.insert(ret, false)
-- table.insert(ret, '456')
-- table.insert(ret, false)
-- table.insert(ret, '789')
-- return ret

return {'123', false, '456', false, '789'}

