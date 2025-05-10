package com.wzh.manager.mapper;

import com.wzh.manager.domain.MFileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IndexMapper {

    Map<String,Long> getHeaderTotal(String[] type);

    Map<String,Long> getTotal();
    Map<String,Long> getTotal1();

    List<MFileInfo> resentUpload();
}
