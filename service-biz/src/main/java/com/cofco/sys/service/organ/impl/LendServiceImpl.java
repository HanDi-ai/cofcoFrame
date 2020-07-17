package com.cofco.sys.service.organ.impl;


import com.cofco.base.utils.Utils;
import com.cofco.sys.dao.organ.LendDao;
import com.cofco.sys.entity.organ.LendEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.LendService;
import com.cofco.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 用户借调
 * @author liming
 * @date 2018-04-30 10:07:59
 */

@Service("LendService")
@Transactional
public class LendServiceImpl implements LendService {
    @Autowired
    protected LendDao LendDao;
    /**
     * 原机构人员
     * @param map
     */
    @Override
    public List<UserEntity> queryList(Map<String, Object> map) {
        return LendDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return LendDao.queryTotal(map);
    }

    /**
     * 借调机构人员
     * @param map
     */
    @Override
    public List<UserEntity> newList(Map<String, Object> map) { return LendDao.newList(map);}

    @Override
    public int newTotal(Map<String, Object> map) {
        return LendDao.newTotal(map);
    }

    /**
     * 保存借调人员
     * @param lend
     */
    @Override
    @Transactional
    public void save(LendEntity lend){
        UserEntity currentUser = UserUtils.getCurrentUser();
        List<String> UserIdList = lend.getUserIdList();
          for (int i = 0;i<UserIdList.size();i++){
              Map<String, Object> map = new HashMap<>();
                map.put("id", Utils.uuid());
                map.put("user_id",UserIdList.get(i));
                map.put("old_organ_Id", lend.getOld_organ_Id());
                map.put("new_organ_Id", lend.getNew_organ_Id());
                map.put("lend_time",new Date());
                map.put("status", 0);
                map.put("create_time", new Date());
                map.put("create_id",currentUser.getId());
                LendDao.save(map);
            }
    }
    /**
     * 取消借调人员
     * @param lend
     */
    @Override
    public void relieve(LendEntity lend) {
        Map<String, Object> map = new HashMap<>();

        map.put("status",'1');
        map.put("new_organ_Id", lend.getNew_organ_Id());
        map.put("userIdList", lend.getUserIdList());
        LendDao.relieve(map);

    }
}
