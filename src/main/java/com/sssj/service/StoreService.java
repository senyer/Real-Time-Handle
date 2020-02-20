package com.sssj.service;

import com.sssj.domain.RealtimeData;

import java.util.List;

public interface StoreService {

  /**
   * 异步存储实时数据到数据库
   * 1.维护最新的实时数据
   * 2.SaveOrUpdate
   * @param list RealtimeData集合。
   * @return 是否存储成功
   */
  void storeToDBAsync(List<RealtimeData> list);


  /**
   * 同步存储实时数据到数据库
   * 1.维护最新的实时数据
   * 2.SaveOrUpdate
   * @param list RealtimeData集合。
   * @return 是否存储成功
   */
  boolean storeToDB(List<RealtimeData> list);

}
