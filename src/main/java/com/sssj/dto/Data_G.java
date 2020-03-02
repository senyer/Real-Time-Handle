package com.sssj.dto;

import lombok.Data;

@Data
public class Data_G {
  private String name;
  private Double value;
  private String time;//influxDB更新时间戳
  private String sourceTime;//目标源时间
}
//A：变量+数据、B：变量+数据+单位、C：变量+数据+时间+单位、D：变量+数据+时间。。。。。。