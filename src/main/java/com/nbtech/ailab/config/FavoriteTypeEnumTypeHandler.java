package com.nbtech.ailab.config;

import com.nbtech.ailab.common.FavoriteTypeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 收藏类型枚举 TypeHandler
 * 用于 MyBatis 中枚举类型与数据库字符串类型的转换
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@MappedTypes(FavoriteTypeEnum.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class FavoriteTypeEnumTypeHandler extends BaseTypeHandler<FavoriteTypeEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, FavoriteTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        // 将枚举的 value 值存储到数据库
        ps.setString(i, parameter.getValue());
    }

    @Override
    public FavoriteTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : FavoriteTypeEnum.getByValue(value);
    }

    @Override
    public FavoriteTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : FavoriteTypeEnum.getByValue(value);
    }

    @Override
    public FavoriteTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : FavoriteTypeEnum.getByValue(value);
    }
}

