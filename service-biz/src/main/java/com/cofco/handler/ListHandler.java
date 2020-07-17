package com.cofco.handler;

import com.cofco.base.common.Constant;
import com.cofco.sys.entity.user.UserLocationEntity;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListHandler extends BaseTypeHandler {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Object parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = null;
        try {
            if (null != parameter) {
                List<UserLocationEntity> list = (ArrayList<UserLocationEntity>) parameter;
                conn = ps.getConnection().getMetaData().getConnection();
                // 这里必须得用大写，而且必须要引入一个包，如果不引入这个包的话字符串无法正常转换，包是：orai18n.jar
                ARRAY array = getArray(conn, Constant.PUBLIC_JDBC_NAME+"USER_LOCATION_OBJECT", Constant.PUBLIC_JDBC_NAME+"USER_LOCATION_LIST", list);
                ps.setArray(i, array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static ARRAY getArray(Connection con, String OraObjType, String OraArrType, List tempList) throws Exception {
        ARRAY list = null;
        if (tempList != null && tempList.size() > 0) {
            // Oracle识别的集合对象,匹配java对象集合
            STRUCT[] structs = new STRUCT[tempList.size()];

            // Oracle识别的对象模板,匹配单个java对象
            StructDescriptor structdesc = new StructDescriptor(OraObjType, con);

            // 遍历stuList,将每个Student对象转换为Oracle可识别的模板对象

            for (int i = 0; i < tempList.size(); i++) {
                Object[] oneRow = new Object[1];
                oneRow[0] = tempList.get(i).toString();
                structs[i] = new STRUCT(structdesc, con, oneRow);
            }

            // 匹配list
            ArrayDescriptor desc = ArrayDescriptor.createDescriptor(OraArrType, con);
            list = new ARRAY(desc, con, structs);
        } else {
            ArrayDescriptor desc = ArrayDescriptor.createDescriptor(OraArrType, con);
            list = new ARRAY(desc, con, null);
        }
        return list;
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}

