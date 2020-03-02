package com.sssj.webservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sssj.domain.RealtimeData;
import com.sssj.dto.*;
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

import static com.sssj.dto.DBNAME.*;
import static com.sssj.dto.DataStyle.*;

@Component
@Slf4j
public class WebServiceHandler {
  //TODO(senyer) improve this
  private static final String URL = "http://127.0.0.1:8086";
  private static final String USER = "admin";
  private static final String PASSWORD = "admin";
  private static String DATABASE = "RealTimeDB";
  private static AtomicBoolean HASDB = new AtomicBoolean(false);
  private InfluxDB influxDB;

  public WebServiceHandler() {
    influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD);
  }

  @Resource
  private StoreService storeService;
  @Resource
  private RealtimeDataService realtimeDataService;

  protected <T> void handle(String data, String type) {
    switch (type) {
      case STYLE_A:
        //List<Data_A> listA = FastJsonUtil.toList(data, Data_A.class);
        //writeToInfluxDB(listA);//to influxDB -> history
        //storeService.storeToDBAsync(list);//to DB -> realtime
        break;
      case STYLE_B:
        //List<Data_B> listB = FastJsonUtil.toList(data, Data_B.class);
        //writeToInfluxDB(listB);//to influxDB -> history
        //storeService.storeToDBAsync(list);//to DB -> realtime
        break;
      case STYLE_C:
        List<Data_C> listC = FastJsonUtil.toList(data, Data_C.class);
        writeToInfluxDB(listC);//to influxDB -> history
        //storeService.storeToDBAsync(transDataToEntity(listC));//to DB -> realtime
        break;
      case STYLE_D:
        List<Data_D> listD = FastJsonUtil.toList(data, Data_D.class);
        writeToInfluxDB(listD);//to influxDB -> history
        //storeService.storeToDBAsync(list);//to DB -> realtime
        break;
      case STYLE_E:
        List<Data_E> listE = FastJsonUtil.toList(data, Data_E.class);
        writeToInfluxDB(listE);//to influxDB -> history
        //storeService.storeToDBAsync(list);//to DB -> realtime
        break;
      case STYLE_F:
        List<Data_F> listF = FastJsonUtil.toList(data, Data_F.class);
        writeToInfluxDB(listF);//to influxDB -> history
        //storeService.storeToDBAsync(list);//to DB -> realtime
        break;
    }


  }

  /**
   * 解析来自Senyer的OpcClient的JSON数据
   *
   * @param data 源数据
   * @return 转换为map映射对象
   */
  private <T> List<T> parseData(String data, Class<T> clazz) {

    return FastJsonUtil.toList(data, clazz);
  }

  private <T> void writeToInfluxDB(List<T> list) {
    try {
      // Flush every 2000 Points, at least every 100ms
      influxDB.enableBatch(BatchOptions.DEFAULTS.actions(2000).flushDuration(100));
      hasDBOrCreate(list.get(0));

      list.forEach((v) -> {
        Point point = null;
        if (v instanceof Data_A) {
          Data_A dataA = (Data_A) v;
          point = dataA.buildPoint(dataA);
        } else if (v instanceof Data_B) {
          Data_B dataB = (Data_B) v;
          point = dataB.buildPoint(dataB);
        } else if (v instanceof Data_C) {
          Data_C dataC = (Data_C) v;
          point = dataC.buildPoint(dataC);
        } else {
          //TODO 继续扩展
          throw new RuntimeException("influx DB 设置point失败");
        }
        influxDB.write(DATABASE, "autogen", point);
      });
      log.info(">>>>>>>>>>>>>>Write To InfluxDB SUCCESS!");
    } catch (Exception e) {
      log.error(">>>>>>>>>>>>>>Write To InfluxDB Failed! {1}", e);
    } finally {
      influxDB.close();
    }
  }


  /**
   * 特殊格式的转换数据为实体映射集合
   *
   * @param listDC 解析后的JSON数据
   * @return 实时数据实体映射集合
   */
  private List<RealtimeData> transDataToEntity(List<Data_C> listDC) {
    List<RealtimeData> list = new ArrayList<>();
    long a = System.currentTimeMillis();
    listDC.forEach((v) -> {
      RealtimeData realtimeData = new RealtimeData();
      realtimeData.setName(v.getName());
      realtimeData.setTime(DateUtil.stringToDate(v.getTime(), DateUtil.DATETIME_NORMAL_FORMAT));
      //realtimeData.setValue(String.valueOf(v.getValue()));//TODO Fix BUG
      list.add(realtimeData);
    });
    long b = System.currentTimeMillis();
    log.info(">>>>>>>>second parse Cost: " + (b - a) + " ms");
    return list;
  }

  private <T> void hasDBOrCreate(T type) {
    QueryResult showDatabases = influxDB.query(new Query("show databases"));
    List<List<Object>> values = showDatabases.getResults().get(0).getSeries().get(0).getValues();

    if (type instanceof Data_A) {
      values.forEach((objectList) -> {
        String dbName = String.valueOf(values.get(0).get(0));
        if (DATABASE_A.equals(dbName)) {
          HASDB.set(true);
        }
      });
      DATABASE = DATABASE_A;
    } else if (type instanceof Data_B) {
      values.forEach((objectList) -> {
        String dbName = String.valueOf(values.get(0).get(0));
        if (DATABASE_B.equals(dbName))
          HASDB.set(true);
      });
      DATABASE = DATABASE_B;
    } else if (type instanceof Data_C) {
      values.forEach((objectList) -> {
        String dbName = String.valueOf(values.get(0).get(0));
        if (DATABASE_C.equals(dbName))
          HASDB.set(true);
      });
      DATABASE = DATABASE_C;
    } else {
      //TODO 继续扩展
      throw new RuntimeException("influx DB hasDBOrCreate 出错");
    }

    if (!HASDB.get()) {
      influxDB.query(new Query("CREATE DATABASE " + DATABASE));
      HASDB.set(false);
    }
  }

}
