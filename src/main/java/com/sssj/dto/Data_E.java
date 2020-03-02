package com.sssj.dto;

import lombok.Data;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

@Data
public class Data_E implements InfluxPoint<Data_E>{
  private Integer id;
  private Double value;
  private String time;

  @Override
  public Point buildPoint(Data_E data) {
    return Point
            .measurement("T_"+data.getId())
            .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
            .addField("value", data.getValue())
            .addField("sourceTime", data.getTime())
            .build();
  }
}
//A：变量+数据、B：变量+数据+单位、C：变量+数据+时间+单位、D：变量+数据+时间。。。。。。