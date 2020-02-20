package com.sssj.webservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sssj.domain.RealtimeData;
import com.sssj.service.StoreService;
import com.sssj.utils.DateUtil;
import com.sssj.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WebServiceHandler {
  //TODO(senyer) improve this
  private static final String URL = "http://127.0.0.1:8086";
  private static final String USER = "admin";
  private static final String PASSWORD = "admin";
  private InfluxDB influxDB;

  public WebServiceHandler() {
    influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD);
  }

  @Resource
  private StoreService storeService;

  protected void handle(String data) throws Exception {
    Map<String, Object> storeMap = parseData(data);
    List<RealtimeData> dataList = transDataToEntity(storeMap);
    writeToInfluxDB(dataList);//to influxDB -> history
    storeService.storeToDBAsync(dataList);//to DB -> realtime
  }

  /**
   * 解析JSON数据
   *
   * @param data 源数据
   * @return 转换为map映射对象
   */
  private Map<String, Object> parseData(String data) {
    long a = System.currentTimeMillis();
    Map<String, Object> storeMap = FastJsonUtil.stringToMap(
            FastJsonUtil.toJSONString(
                    FastJsonUtil.toArray(data)[0]));
    long b = System.currentTimeMillis();
    log.info(">>>>>>>>First parse Cost: " + (b - a) + " ms");
    return storeMap;
  }

  /**
   * 转换数据为实体映射集合
   *
   * @param storeMap 解析后的JSON数据
   * @return 实时数据实体映射集合
   */
  private List<RealtimeData> transDataToEntity(Map<String, Object> storeMap) {
    List<RealtimeData> list = new ArrayList<>();
    long a = System.currentTimeMillis();
    for (Map.Entry<String, Object> entry : storeMap.entrySet()) {
      RealtimeData realtimeData = new RealtimeData();
      //realtimeData.setTid(Integer.valueOf(entry.getKey()));
      realtimeData.setName(String.valueOf(entry.getKey()));
      JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(entry.getValue()));
      realtimeData.setValue(String.valueOf(jsonObject.get("Value")));
      realtimeData.setTime(DateUtil.stringToDate(String.valueOf(jsonObject.get("Time"))
              .replace("/", "-"), DateUtil.DATETIME_NORMAL_FORMAT));
      list.add(realtimeData);
    }
    long b = System.currentTimeMillis();
    log.info(">>>>>>>>second parse Cost: " + (b - a) + " ms");
    return list;
  }

  private void writeToInfluxDB(List<RealtimeData> list) {
    //TODO(senyer) improve this
    list.forEach((v) -> {
      /*Point.Builder builder = Point.measurement(v.getName())
              .tag("tag1", "www").tag("tag2", "22")
              .tag("tag3", "man");*/
      Point point = Point
                    .measurement(v.getName())
                    .addField("seq_id", v.getSeqId())
                    .addField("value", v.getValue())
                    .addField("sourceTime", DateUtil.dateToString(v.getTime(), DateUtil.DATETIME_NORMAL_FORMAT))
                    .build();
      influxDB.write(v.getName(), "autogen", point);
    });
  }

}
