-- 测试局部变量在函数中使用

local key_1 = KEYS[1]

local argv_1 = ARGV[1]

local function connect(_connector)

    return key_1 .. _connector .. argv_1

end

return connect('@^@')