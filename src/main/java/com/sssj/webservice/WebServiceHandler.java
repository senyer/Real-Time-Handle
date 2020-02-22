package com.sssj.webservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sssj.domain.RealtimeData;
import com.sssj.service.RealtimeDataService;
import com.sssj.service.StoreService;
import com.sssj.utils.DateUtil;
import com.sssj.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class WebServiceHandler {
  //TODO(senyer) improve this
  private static final String URL = "http://127.0.0.1:8086";
  private static final String USER = "admin";
  private static final String PASSWORD = "admin";
  private static final String DATABASE = "RealTimeDB";
  private static AtomicBoolean HASDB =new AtomicBoolean(false);
  private InfluxDB influxDB;

  public WebServiceHandler() {
    influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD);
  }

  @Resource
  private StoreService storeService;
  @Resource
  private RealtimeDataService realtimeDataService;

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
      realtimeData.setTime(DateUtil.stringToDate(String.valueOf(jsonObject.get("Time")).replace("/", "-"), DateUtil.DATETIME_NORMAL_FORMAT));
      list.add(realtimeData);
    }
    long b = System.currentTimeMillis();
    log.info(">>>>>>>>second parse Cost: " + (b - a) + " ms");
    return list;
  }
  private void writeToInfluxDB(List<RealtimeData> list) {
    try {
      // Flush every 2000 Points, at least every 100ms
      influxDB.enableBatch(BatchOptions.DEFAULTS.actions(2000).flushDuration(100));
      hasDBOrCreate();
      list.forEach((v) -> {
        String name = v.getName();
        System.out.println(name);
        Point point = Point
                .measurement(name)
                .addField("seq_id", v.getSeqId())
                .addField("value", v.getValue())
                .addField("sourceTime", DateUtil.dateToString(v.getTime(), DateUtil.DATETIME_NORMAL_FORMAT))
                .build();

        influxDB.write(DATABASE, "autogen", point);
      });
      log.error(">>>>>>>>>>>>>>Write To InfluxDB SUCCESS!");
    } catch (Exception e){
      log.error(">>>>>>>>>>>>>>Write To InfluxDB Failed! {1}",e);
    }finally {
      influxDB.close();
    }
  }

  private void hasDBOrCreate(){
    QueryResult showDatabases = influxDB.query(new Query("show databases"));
    List<List<Object>> values = showDatabases.getResults().get(0).getSeries().get(0).getValues();
    values.forEach((objectList)->{
      String dbName = String.valueOf(values.get(0).get(0));
      if (DATABASE.equals(dbName))
        HASDB.set(true);
    });

    if (!HASDB.get()){
      influxDB.query(new Query("CREATE DATABASE " + DATABASE));
      HASDB.set(false);
    }

    QueryResult query1 = influxDB.query(new Query("show databases "));
    List<List<Object>> t = query1.getResults().get(0).getSeries().get(0).getValues();
  }
}
