# 实时数据处理程序

# 介绍

* InfluxDB实现历史数据、实时数据的读取存储
* 统一管理tag变量，快速脚手架

# InfluxDB重点介绍

## InfluxDB连接方式

```xml
    <dependency>
      <groupId>org.influxdb</groupId>
      <artifactId>influxdb-java</artifactId>
      <version>2.9</version>
    </dependency>
    
```

> InfluxDB influxDB = InfluxDBFactory.connect(URL, USER, PASSWORD);

## InfluxDB写数据

```java
        Builder builder = Point.measurement("table_test")
            .tag("tag1", "www").tag("tag2", "22")
            .tag("tag3", "man");
        Point point = builder .addField("value1", 66).addField("value2", 22).build();
        influxDB.write(INFLUXDB_DBNAME, "autogen", point );
```

## InfluxDB读数据

```java
        String sql = "select * from result";
        Query query = new Query(sql, INFLUXDB_DBNAME);
        QueryResult result = influxDB.query(query);
        if (result.getResults().get(0).getSeries() != null) {
            List<String> columns =     result.getResults().get(0).getSeries().get(0).getColumns();
            List<Object> resval = result.getResults().get(0).getSeries().get(0).getValues().get(0);
            double qpsdata = (Double) resval.get(columns.indexOf("value1"));
            System.out.println(qpsdata);
        }
```

## InfluxDB常用查询语句
> 查询最近10条记录：
```sql
select * from "/ros" order by time desc limit 10
```
> 查询top 10的记录：
```sql
select top(cpu, 100) from "/ros" 
```
> 查询最小的10条记录
```sql
select bottom(cpu, 10) from "/ros"
```