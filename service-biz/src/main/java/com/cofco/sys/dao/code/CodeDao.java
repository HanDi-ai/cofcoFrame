package com.cofco.sys.dao.code;

import com.cofco.sys.entity.code.CodeEntity;
import com.cofco.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统数据字典
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-07-14 13:42:42
 */
@Repository
public interface CodeDao extends BaseDao<CodeEntity> {
    /**
     * 查询所有的字典及子字典(用做字典缓存)
     * @return
     */
    List<CodeEntity> queryAllCode();

    /**
     * 查询所有的字典及子字典(用做字典缓存)
     * @return
     */
    List<CodeEntity> queryChildsByMark(String mark);

    /**
     * 根据字典标识查询
     * @param mark
     * @return
     */
    CodeEntity queryByMark(String mark);


    /**
     * 字典取值通用方法
     * @param mark
     * @return
     */
    List<CodeEntity> getCodeByMark(String mark);
}
