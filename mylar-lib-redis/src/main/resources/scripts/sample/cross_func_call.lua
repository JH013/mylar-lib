-- 测试跨脚本函数调用 - 调用脚本

local key_1 = KEYS[1]

local argv_1 = ARGV[1]

local mylarLib = require('mylar_lib')

return require:contactKV(key_1, argv_1)