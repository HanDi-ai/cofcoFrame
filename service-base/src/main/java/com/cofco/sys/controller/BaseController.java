package com.cofco.sys.controller;



import com.cofco.sys.entity.user.UserEntity;
import com.cofco.utils.UserUtils;
import org.apache.log4j.Logger;

/**
 * 类的功能描述.
 * 公共的控件器，可在类中实现一些共同的方法和属性 持续更新中
 * @Auther cofco
 * @Date 2017/4/29
 */
public class BaseController {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 获取当前登陆用户
     * @return
     */
    public UserEntity getUser(){
        return UserUtils.getCurrentUser();
    }

    /**
     * 获取当前登陆用户Id
     * @return
     */
    public String getUserId(){
        UserEntity user = getUser();
        if(null != user && null !=user.getId()){
            return user.getId();
        }
        return "";
    }


}
