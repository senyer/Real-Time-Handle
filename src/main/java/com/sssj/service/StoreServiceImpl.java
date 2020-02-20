package com.sssj.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sssj.domain.RealtimeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class StoreServiceImpl implements StoreService {

  @Resource
  private RealtimeDataService realtimeDataService;

  @Override
  @Async
  public void storeToDBAsync(List<RealtimeData> list) {
    try {
      long a = System.currentTimeMillis();
      //TODO(senyer) improve this.
      list.forEach((v) -> realtimeDataService.saveOrUpdate(v, new UpdateWrapper<RealtimeData>().eq("name", v.getName())));
      long b = System.currentTimeMillis();
      log.info("异步存入数据库花费时间：" + (b - a));
    } catch (Exception e) {
      log.error("storeToDBAsync Error: {}", e.toString());
    }
  }

  @Override
  public boolean storeToDB(List<RealtimeData> list) {
    try {
      long a = System.currentTimeMillis();
      //TODO(senyer) improve this.
      list.forEach((v) -> realtimeDataService.saveOrUpdate(v, new UpdateWrapper<RealtimeData>().eq("name", v.getName())));
      long b = System.currentTimeMillis();
      log.info("同步存入数据库花费时间：" + (b - a));
    } catch (Exception e) {
      log.error("storeToDB Error: {}", e.toString());
      return false;
    }
    return true;
  }
}
