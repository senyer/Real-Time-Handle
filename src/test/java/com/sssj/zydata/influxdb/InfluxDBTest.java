package com.sssj.zydata.influxdb;

import com.sssj.dto.Data_G;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;
import java.util.ArrayList;
import java.util.List;

public class InfluxDBTest {
  private static final String URL="http://127.0.0.1:8086";
  private static final String USER="admin";
  private static final String PASSWORD="admin";
  private static final String INFLUXDB_DBNAME="RealTimeDB";
  public static void main(String[] args) {

    InfluxDB influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD);
    //写数据

    String dbName = "aTimeSeries";
    QueryResult query1 = influxDB.query(new Query("show measurements ",INFLUXDB_DBNAME));
    List<List<Object>> values = query1.getResults().get(0).getSeries().get(0).getValues();
    System.out.println(values.size());
    values.forEach((objectList)->{
      String measurement = String.valueOf(objectList.get(0));
     // System.out.println(measurement);
    });
    System.out.println(values.get(0).size());
    System.out.println(values.get(0).get(0));
    String rpName = "aRetentionPolicy";
    influxDB.query(new Query("CREATE RETENTION POLICY " + rpName + " ON " + dbName + " DURATION 30h REPLICATION 2 SHARD DURATION 30m DEFAULT"));

    System.out.println("=====================================================AAAA================================================================");

    Point.Builder builder = Point.measurement("松浦泵站6#机泵状态")
            .tag("tag1", "www22").tag("tag2", "22")
            .tag("tag3", "man22");
    Point point = builder .addField("value11", 66).addField("value2", 22).build();
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

    System.out.println("=======================================================BBBB==============================================================");

    List<String> measurements = getAllMeasurement(influxDB);
    List<Data_G> data=new ArrayList<>();
    measurements.forEach((measurement)->{
      //查数据
      Query select = select("value","sourceTime")
              .from(INFLUXDB_DBNAME,measurement).orderBy(desc()).limit(1);
      QueryResult queryResult = influxDB.query(select);
      List<QueryResult.Series> series = queryResult.getResults().get(0).getSeries();
      if (series!= null) {
        String time=(String)series.get(0).getValues().get(0).get(0);
        String value=(String)series.get(0).getValues().get(0).get(1);
        String sourcetime=(String)series.get(0).getValues().get(0).get(2);
        Data_G data_g = new Data_G();
        data_g.setName(measurement);
        data_g.setValue(value);
        data_g.setSourceTime(sourcetime);
        data_g.setTime(time);
        data.add(data_g);
      }
    });
    System.out.println(data.toString());
  }

  /*
  获取所有的表名
 */
  private static List<String> getAllMeasurement(InfluxDB influxDB){
    List<String> list=new ArrayList<>();
    //查数据
    String sql = "show measurements";
    QueryResult results = influxDB.query(new Query(sql, INFLUXDB_DBNAME));
    List<List<Object>> values = results.getResults().get(0).getSeries().get(0).getValues();
    values.forEach((objectList)->{
      String measurement = String.valueOf(objectList.get(0));
      list.add(measurement);
    });
    return list;
  }
}
