package com.sssj.dto;

import lombok.Data;
import org.influxdb.dto.Point;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Data
public class Data_C implements InfluxPoint<Data_C>{
  private String name;
  private Object value;
  private String unit;
  private String time;

  @Override
  public Point buildPoint(Data_C data) {
    Object value = data.getValue();
    if (value instanceof Boolean) {
      return Point
              .measurement(data.getName())
              .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
              .addField("value", (boolean)value)
              .addField("unit", data.getUnit())
              .addField("sourceTime", data.getTime())
              .build();
    } else if (value instanceof Double) {
      return Point
              .measurement(data.getName())
              .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
              .addField("value", (double)value)
              .addField("unit", data.getUnit())
              .addField("sourceTime", data.getTime())
              .build();
    } else if (value instanceof Integer) {
      return Point
              .measurement(data.getName())
              .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
              .addField("value", (int)value)
              .addField("unit", data.getUnit())
              .addField("sourceTime", data.getTime())
              .build();
    } else if (value instanceof BigDecimal) {
      return Point
              .measurement(data.getName())
              .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
              .addField("value", String.valueOf(value))
              .addField("unit", data.getUnit())
              .addField("sourceTime", data.getTime())
              .build();
    }
    else {
        return Point
                .measurement(data.getName())
                .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
                .addField("value", String.valueOf( value))
                .addField("unit", data.getUnit())
                .addField("sourceTime", data.getTime())
                .build();
    }
  }
}
//A：变量+数据、B：变量+数据+单位、C：变量+数据+时间+单位、D：变量+数据+时间。。。。。。