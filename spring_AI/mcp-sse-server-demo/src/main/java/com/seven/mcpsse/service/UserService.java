package com.seven.mcpsse.service;

import com.seven.mcpsse.entity.UserInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    static Map<String, UserInfo> userInfoMap = new HashMap<>();

    static {
        userInfoMap.put("zhangsan", new UserInfo("zhangsan", 3, "male", "xian"));
    }

    @Tool(description = "根据用户姓名,返回用户信息")
    public String getUserInfo(@ToolParam(description = "用户姓名") String name) {
        if(userInfoMap.containsKey(name)) {
            return userInfoMap.get(name).toString();
        }
        return "None";
    }
}
