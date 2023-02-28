-- 测试集合返回

local unpack = unpack or table.unpack

local function getNil()
    return nil;
end

-- 解决原生pack的nil截断问题，SafePack与SafeUnpack要成对使用
local function safePack(...)
    local params = {...}
    params.n = select('#', ...) --返回可变参数的数量,赋值给n
    return params
end

-- 解决原生unpack的nil截断问题，SafePack与SafeUnpack要成对使用
local function safeUnpack(safe_pack_tb)
    return unpack(safe_pack_tb, 1, safe_pack_tb.n)
end

-- local ret = {'123', '456', nil, '789' }

local ret = {}
table.insert(ret, '123')
table.insert(ret, false)
table.insert(ret, '456')
table.insert(ret, false)
table.insert(ret, '789')

-- ret.n = select('#', ret)

return ret

-- local temp = safePack('123', '456', nil, '789');

-- return safeUnpack(temp)

-- ret[1] = '123'
-- ret[2] = getNil()
-- ret[3] = '456'
-- ret[4] = '789'

-- return ret

