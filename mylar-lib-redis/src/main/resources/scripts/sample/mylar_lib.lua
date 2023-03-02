-- 测试跨脚本函数调用 - 基础 lib

local lib = {}

function lib.contactKV(a, b)
    return a .. '-^-' ..b
end

return lib