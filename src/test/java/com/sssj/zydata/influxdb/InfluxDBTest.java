package com.sssj.zydata.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.List;

public class InfluxDBTest {
  private static final String URL="http://127.0.0.1:8086";
  private static final String USER="admin";
  private static final String PASSWORD="admin";
  private static final String INFLUXDB_DBNAME="Test";
  public static void main(String[] args) {

    InfluxDB influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD);
    //写数据
    Point.Builder builder = Point.measurement("松浦泵站6#机泵状态")
            .tag("tag1", "www").tag("tag2", "22")
            .tag("tag3", "man");
    Point point = builder .addField("value1", 66).addField("value2", 22).build();
    influxDB.write(INFLUXDB_DBNAME, "autogen", point );

    //查数据
    String sql = "select * from table_test";
    Query query = new Query(sql, INFLUXDB_DBNAME);
    QueryResult result = influxDB.query(query);
    if (result.getResults().get(0).getSeries() != null) {
      List<String> columns =     result.getResults().get(0).getSeries().get(0).getColumns();
      List<Object> resval = result.getResults().get(0).getSeries().get(0).getValues().get(0);
      double qpsdata = (Double) resval.get(columns.indexOf("value1"));
      System.out.println(qpsdata);
    }
  }
}
