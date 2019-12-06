package com.iamyanbing.dao.manager.handler;

import com.iamyanbing.dao.manager.utils.ApplicationContextUtil;
import com.iamyanbing.dao.manager.utils.DataBaseEncryptUtils;
import lombok.Setter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Setter
public class DataTypeHandler extends BaseTypeHandler<String> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement,int i,String parameter,JdbcType jdbcType) throws SQLException{
        DataBaseEncryptUtils dataBaseEncrypt = ApplicationContextUtil.getBean(DataBaseEncryptUtils.class);
        preparedStatement.setString(i, dataBaseEncrypt.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet resultSet,String s) throws SQLException{
        DataBaseEncryptUtils dataBaseEncrypt = ApplicationContextUtil.getBean(DataBaseEncryptUtils.class);
        return dataBaseEncrypt.decrypt(resultSet.getString(s));
    }

    @Override
    public String getNullableResult(ResultSet resultSet,int i) throws SQLException{
        DataBaseEncryptUtils dataBaseEncrypt = ApplicationContextUtil.getBean(DataBaseEncryptUtils.class);
        return dataBaseEncrypt.decrypt(resultSet.getString(i));
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement,int i) throws SQLException{
        DataBaseEncryptUtils dataBaseEncrypt = ApplicationContextUtil.getBean(DataBaseEncryptUtils.class);
        return dataBaseEncrypt.decrypt(callableStatement.getString(i));
    }
}
