package com.cofco.sys.service.members.impl;

import com.cofco.constant.Const;
import com.cofco.sys.dao.members.ClientDao;
import com.cofco.sys.entity.members.AllianceClientEntity;
import com.cofco.sys.entity.members.AllianceClientLogEntity;
import com.cofco.sys.service.members.ClientService;
import com.cofco.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 联盟客户注册
 *
 * @author cofco
 * @date 2019-10-29 13:42:42
 */
@Service("clientService")
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Override
    public String clientSeq() {
        return clientDao.clientSeq();
    }

    @Override
    public String clientSeqLog() {
        return clientDao.clientSeqLog();
    }

    /**
     * 查询当前联盟成员下是否包含联盟客户
     * @param memberId
     * @return
     */
    @Override
    public int containNums(String memberId) {
        return clientDao.containNums(memberId);
    }

    /**
     * 联盟客户注册
     */
    @Override
    public void save(AllianceClientEntity client) {
        client.setVersion("v1");
        client.setTimeCreate(new Date());
        client.setCreator(UserUtils.getCurrentUserId());
        clientDao.save(client);
    }

    /**
     * 联盟客户日志表
     */
    @Override
    public void saveLog(AllianceClientLogEntity clientLogEntity) {
        clientDao.saveLog(clientLogEntity);
    }

    @Override
    public void updateLog(AllianceClientLogEntity clientLogEntity) {
        clientDao.updateLog(clientLogEntity);
    }

    /**
     * 联盟客户信息修改
     */
    @Override
    public void update(AllianceClientEntity client) {
        // 修改时间
        client.setTimeModify(new Date());
        // 修改人
        //client.setModifier(UserUtils.getCurrentUserId());
        clientDao.update(client);
    }

    @Override
    public void updateClientId(String clientId, String clientTmpId) {
        clientDao.updateClientId(clientId, clientTmpId);
    }

    /**
     * 联盟客户信息删除
     */
    @Override
    public void delete(AllianceClientEntity client) {
        // 注销标识
        client.setIsDelete(Const.DELETE_FLG_YES);
        clientDao.delete(client);
    }

    /**
     * 联盟客户信息查询
     */
    @Override
    public AllianceClientEntity queryObject(String id) {
        return clientDao.queryObject(id);
    }

    @Override
    public List<AllianceClientEntity> queryList(Map<String, Object> var) {
        return clientDao.queryList(var);
    }

    @Override
    public int queryTotal(Map<String, Object> var) {
        return clientDao.queryTotal(var);
    }


}
