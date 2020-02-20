package com.sssj.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.SQLException;

public class MyBatisPlusGenerator {
        /**
         * 具体参考官方源码：https://gitee.com/baomidou/mybatis-plus/tree/3.0/mybatis-plus-generator/src/main/java/com/baomidou/mybatisplus/generator/config
         */
        public static void main(String[] args) throws SQLException {
                // 代码生成器
                AutoGenerator mpg = new AutoGenerator();

                // 全局配置
                GlobalConfig gc = new GlobalConfig();
                String projectPath = System.getProperty("user.dir");
                gc.setOutputDir(projectPath + "/src/main/java");
                gc.setAuthor("senyer");
                gc.setOpen(false);
                gc.setIdType(IdType.AUTO);
                gc.setServiceName("%sService");
                gc.setServiceImplName("%sServiceImpl");// 设置生成的service接口的名字的首字母是否为I，%s 为占位符“I%sService”
                gc.setFileOverride(true);// 文件覆盖
                //gc.setSwagger2(true); //实体属性 Swagger2 注解 //是否开启 swagger2 模式
                gc.setBaseResultMap(true);//生成基本的resultMap
                gc.setBaseColumnList(true);//生成基本的SQL片段
                gc.setActiveRecord(true);
                mpg.setGlobalConfig(gc);

                // 数据源配置
                DataSourceConfig dsc = new DataSourceConfig();
                dsc.setUrl("jdbc:sqlserver://192.168.100.15;DatabaseName=RealTimeDB");
                dsc.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                dsc.setUsername("sa");
                dsc.setPassword("c4FZy63120760jqT");
                dsc.setDbType(DbType.SQL_SERVER);
                mpg.setDataSource(dsc);

                // 策略配置
                StrategyConfig strategy = new StrategyConfig();
                strategy.setCapitalMode(true);//全局大写命名
                //strategy.setLogicDeleteFieldName("del_flag");//统一设置逻辑删除的字段
                strategy.setNaming(NamingStrategy.underline_to_camel);// 数据库表映射到实体的命名策略
                strategy.setColumnNaming(NamingStrategy.underline_to_camel);
                strategy.setEntityLombokModel(true);//【实体】是否为lombok模型（默认 false）
                //strategy.setRestControllerStyle(true);//restControllerStyle 生成@RestController 控制器
                //strategy.setControllerMappingHyphenStyle(true);
                // 设置公共父类
                //strategy.setSuperControllerClass("com.senyer.common.base.BaseController");//可以自定义或者用mp的com.baomidou.ant.common.BaseController
                //设置公共的继承父类
                //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
                // 写于父类中的公共字段,可以是多个
                //strategy.setSuperEntityColumns("id");
                strategy.setInclude(new String[] {"realtime_data"});// 需要生成的表
                //strategy.setExclude(new String[] {"sys_version_update"});// 排除生成的表
                //strategy.setTablePrefix(new String[]{"sys_"});//此处提供多个表的前缀去除  也可以动态获取pc.getModuleName() + "_"
                mpg.setStrategy(strategy);
                        // 包配置
                        PackageConfig pc = new PackageConfig();
                pc.setParent("");//父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名,建议写空，这样，可以让xml文件写入到templates下面，更为规范。
                //pc.setController("com.senyer.system.controller");
                pc.setService("com.sssj.service");
                pc.setServiceImpl("com.sssj.service");
                pc.setMapper("com.sssj.persistence");
                pc.setEntity("com.sssj.domain");
                pc.setXml("mappers");
                mpg.setPackageInfo(pc);

                mpg.execute();
        }

}
