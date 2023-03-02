package com.mylar.sample.redis.controller;

import com.mylar.lib.base.SpringResolver;
import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import com.mylar.lib.redis.script.SampleScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Lua Test
 *
 * @author wangz
 * @date 2023/3/2 0002 23:22
 */
@RestController
@RequestMapping(value = "/test/lua")
public class TestLuaController {

    @Autowired
    private IRedisAggregateOperations redisOperations;

    /**
     * Lua List Ret Get
     *
     * @return 结果
     */
    @RequestMapping(value = "/luaListRetGet")
    public String luaListRetGet() {

        List<String> keys = new ArrayList<>();
        keys.add("Test");

        Object luaResult = this.redisOperations.opsScript().executeScript(SampleScript.singleton().luaListRetGet(), keys);

        List<String> lst = (List<String>) luaResult;

        for (String s : lst) {
            System.out.println(s == null);
            System.out.println(s);
        }

        return JsonUtils.toJson(lst);
    }

    /**
     * Lua Local Func Param
     *
     * @return 结果
     */
    @RequestMapping(value = "/luaLocalFuncParam")
    public String luaLocalFuncParam() {

        List<String> keys = new ArrayList<>();
        keys.add("MyKey");

        List<String> args = new ArrayList<>();
        args.add("MyArg");

        Object luaResult = this.redisOperations.opsScript().executeScript(SampleScript.singleton().luaLocalFuncParam(), keys, args);

        return (String) luaResult;
    }

    /**
     * Lua Cross Func Call
     *
     * @return 结果
     */
    @RequestMapping(value = "/luaCrossFuncCall")
    public String luaCrossFuncCall() {

        List<String> keys = new ArrayList<>();
        keys.add("MyKey");

        List<String> args = new ArrayList<>();
        args.add("MyArg");

        Object luaResult = this.redisOperations.opsScript().executeScript(SampleScript.singleton().luaCrossFuncCall(), keys, args);

        return (String) luaResult;
    }
}
