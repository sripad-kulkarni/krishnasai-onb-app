/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Date;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.driver-class-name}")
  private String classname;

  @Autowired
 private DataSource dataSource;

  public static void main(String[] args) throws Exception {
            System.out.println("Hi");
            // delay 5 seconds
            TimeUnit.SECONDS.sleep(60);
            System.out.println("Bye");    
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  
  @RequestMapping("/hi")
  @ResponseBody
  String index1() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    RestTemplate rest=new RestTemplate();
    return "Hi"+rest.exchange("10.255.255.1", HttpMethod.GET, entity, String.class).getBody();
  }

 
  @RequestMapping("/hiii")
  @ResponseBody
  String index2() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    RestTemplate rest=new RestTemplate();
    
     rest.exchange("https://krishnasai-helper-app.herokuapp.com/db", HttpMethod.GET, entity, String.class).getBody();
    System.exit(-1);
     return "Hiiii";
    }
  
      @RequestMapping("/postbody")
  @ResponseBody
    public void postBody(@RequestBody String fullName) {
      
      System.out.println("Bye"+ fullName); 
    }
  
  
  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }


      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  @RequestMapping("/db1")
  String db1(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {

      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM ticks");
      rs.next();
                
      model.put("records", rs.getInt("count"));
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
   @Bean
  public DataSource dataSource() throws SQLException {
   if (dbUrl == null || dbUrl.isEmpty()) {
     return new HikariDataSource();
    } else {
       HikariConfig config = new HikariConfig();

      config.setJdbcUrl(dbUrl);
      config.setDriverClassName(classname);

      return new HikariDataSource(config);
    }
  } 

}
