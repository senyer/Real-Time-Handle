package com.sssj.dto;

import org.influxdb.dto.Point;

public interface InfluxPoint<T> {

  Point buildPoint(T tppe);
}
