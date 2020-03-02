package com.sssj.dto;

import lombok.Data;
import org.influxdb.dto.Point;
import java.util.concurrent.TimeUnit;

@Data
public class Data_A implements InfluxPoint<Data_A>{
  private String name;
  private Double value;

  @Override
  public Point buildPoint(Data_A data) {
    return Point
            .measurement(data.getName())
            .time(System.currentTimeMillis()+28800000, TimeUnit.MILLISECONDS)//UTC+8个小时等于北京时间
            .addField("value", data.getValue())
            .build();
  }
}
