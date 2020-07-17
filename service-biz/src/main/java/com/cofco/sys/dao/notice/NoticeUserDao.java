package com.cofco.sys.dao.notice;

import com.cofco.sys.entity.notice.NoticeUserEntity;
import com.cofco.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * 通知和用户关系表
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-08-31 15:58:58
 */
@Repository
public interface NoticeUserDao extends BaseDao<NoticeUserEntity> {
    /**
     * 根据通知id和用户id 更新
     * @param noticeUserEntity
     * @return
     */
    int updateByNoticeIdUserId(NoticeUserEntity noticeUserEntity);
}
