package com.cofco.sys.dao.notice;

import com.cofco.sys.entity.notice.NoticeEntity;
import com.cofco.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-08-31 15:59:09
 */
@Repository
public interface NoticeDao extends BaseDao<NoticeEntity> {

    /**
     * 我的通知列表
     * @param noticeEntity
     * @return
     */
    List<NoticeEntity> findMyNotice(NoticeEntity noticeEntity);
}
