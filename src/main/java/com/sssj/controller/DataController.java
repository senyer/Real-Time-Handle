package com.sssj.controller;

import com.sssj.domain.RealtimeData;
import com.sssj.dto.Data_G;
import com.sssj.service.RealtimeDataService;
import com.sssj.utils.DateUtil;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;


@RestController
public class DataController {
  @Resource
  private RealtimeDataService realtimeDataService;

  //TODO(senyer) improve this
  private static final String URL = "http://127.0.0.1:8086";
  private static final String USER = "admin";
  private static final String PASSWORD = "admin";
  private static final String DATABASE = "RealTimeDB";

  /*
    influxDB获取所哟最新的实时数据
   */
  @GetMapping("/realtime-influxdb")
  public Map<String,Object> realTimeFromInfluxDB(){
    Map<String,Object> result =new HashMap<>();
    List<Data_G> data=new ArrayList<>();
    long a = System.currentTimeMillis();
    try (InfluxDB influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD)) {
      List<String> measurements = getAllMeasurement(influxDB);
      measurements.forEach((measurement)->{
        //查所有measurement的最新数据--即实时数据
        Query select = select("value","sourceTime")
                .from(DATABASE,measurement).orderBy(desc()).limit(1);
        QueryResult queryResult = influxDB.query(select);
        List<QueryResult.Series> series = queryResult.getResults().get(0).getSeries();
        if (series!= null) {
          String time=(String)series.get(0).getValues().get(0).get(0);
          Double value=(Double)series.get(0).getValues().get(0).get(1);
          String sourcetime=(String)series.get(0).getValues().get(0).get(2);

          Data_G data_g = new Data_G();
          data_g.setName(measurement);
          data_g.setValue(value);
          data_g.setSourceTime(sourcetime);
          data_g.setTime(time);
          data.add(data_g);
        }
      });
    }
    long b = System.currentTimeMillis();
    System.out.println("----------------------------Influx Cost:"+(b-a));
    result.put("status",200);
    result.put("msg","OK");
    result.put("data",data);
   return result;
  }

  /*
    数据库获取所有最新的实时数据
   */
  @GetMapping("/realtime-db")
  public Map<String,Object> realTimeFromDB(){
    long a = System.currentTimeMillis();
    Map<String,Object> result =new HashMap<>();
    List<RealtimeData> realtimeData = realtimeDataService.list();
    result.put("status",200);
    result.put("msg","OK");
    result.put("data",realtimeData);
    long b = System.currentTimeMillis();
    System.out.println("----------------------------DB query Cost:"+(b-a));
    return result;
  }

  /*
    数据库获取历史数据-两个时间段的，可以支持更丰富的
   */
  @GetMapping("/history")
  public List<Object> history(String[] measurements ,String beginTime,String endTime){
    String beginTimeToUTC = localToUTC(beginTime);
    String endTimeToUTC = localToUTC(endTime);
   List<Object> target=new ArrayList<>();
    try (InfluxDB influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD)) {
      for (int i = 0; i < measurements.length; i++) {
        Query query = select().column("value").from(DATABASE,measurements[i])
                .where(gte("time",beginTimeToUTC))
                .and(lte("time",endTimeToUTC));
        QueryResult queryResult = influxDB.query(query);
        target.add(queryResult);
      }
    }
    return target;
  }

  /**
   * local时间转换成UTC时间(金转换格式，不转换时间差：因为fluxdb存的时间加了8小时。)
   * @param localTime localTime
   * @return localToUTC
   */
  public static String localToUTC(String localTime) {
    Date date = DateUtil.stringToDate(localTime, DateUtil.DATETIME_NORMAL_FORMAT);
    return DateUtil.dateToString(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
  }


  /*
    获取所有的表名
   */
  private List<String> getAllMeasurement(InfluxDB influxDB){
    List<String> list=new ArrayList<>();
    //查数据
    String sql = "show measurements";
    QueryResult results = influxDB.query(new Query(sql, DATABASE));
    List<List<Object>> values = results.getResults().get(0).getSeries().get(0).getValues();
    values.forEach((objectList)->{
      String measurement = String.valueOf(objectList.get(0));
      list.add(measurement);
    });
    return list;
  }



}
