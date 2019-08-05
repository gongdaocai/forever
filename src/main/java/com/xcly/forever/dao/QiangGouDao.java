package com.xcly.forever.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QiangGouDao {

    Map<String, Object> getSkuById(@Param("id") String id);


    Integer updateStockBySkuId(@Param("id")String id,@Param("count")Integer count);

    List<Map<String, Object>> listByUserId(@Param("userId")String userId);

    void insertOrder(@Param("count")Long count);
}
