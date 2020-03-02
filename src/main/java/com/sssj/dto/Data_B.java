package com.sssj.dto;

import lombok.Data;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

@Data
public class Data_B implements InfluxPoint<Data_B>{
  private String name;
  private Double value;
  private String unit;

  @Override
  public Point buildPoint(Data_B data) {
    return Point
            .measurement(data.getName())
            .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
            .addField("value", data.getValue())
            .addField("unit", data.getUnit())
            .build();
  }
}
//A：变量+数据、B：变量+数据+单位、C：变量+数据+时间+单位、D：变量+数据+时间。。。。。。