package com.sssj;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RunTesst {
  public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    Method method = ZydataApplication.class.getMethod("main", String[].class);
    method.invoke(null, (Object) new String[]{String.valueOf(8082)});
    killForWin("8082");
  }

  public static void killForWin(String port) {
    String pid = null;
    Process process = null;
    try {
      process = Runtime.getRuntime().exec("cmd /c netstat -ano |findstr  \"" + port+"\"");
      try (BufferedReader reader = new BufferedReader(
              new InputStreamReader(process.getInputStream()))) {
        String line = null;
        while ((line = reader.readLine()) != null) {
          System.out.println("+++++++++++++++++++++++++++ -----> " + line);
          String[] strs = line.split("\\s+");
            pid = strs[5];
        }
      }
      System.out.println("pid"+pid);
      Process exec = Runtime.getRuntime().exec("taskkill /F /pid "+pid);
      System.out.println(exec.isAlive());
    } catch (IOException e) {
      log.error("Failed to kill process of StoreServer. ");
    }
  }

}
