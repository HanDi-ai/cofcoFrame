package com.cofco.utils;

import com.cofco.base.common.Constant;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类的功能描述.
 * 用户工具类
 * @Auther cofco
 * @Date 2017/5/10
 */

public class UserUtils {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登陆用户
     * @return
     */
    public static UserEntity getCurrentUser(){
        UserEntity user = ShiroUtils.getUserEntity();
        return user;
    }

    /**
     * 获取当前登陆用户 待完善缓存
     * @return
     */
    public static String getCurrentUserId(){
        UserEntity user = ShiroUtils.getUserEntity();
        return user.getId();
    }


    /**
     * 获取机构部门数据权限
     * @param type 1=部门权限，2=机构权限，3=部门机构权限
     * @return
     */
    public static List<String> getDateAuth(String type){
        UserEntity user = UserUtils.getCurrentUser();
        if (user ==null){
            return null;
        }
        if(Constant.DataAuth.BA_DATA.getValue().equals(type)){
            return user.getBaidList();
        }
        if(Constant.DataAuth.BAP_DATA.getValue().equals(type)){
            return user.getBapidList();
        }
        if(Constant.DataAuth.ALL_DATA.getValue().equals(type)){
            user.getBaidList().addAll(user.getBapidList());
            return user.getBaidList();
        }
        return null;
    }
}
