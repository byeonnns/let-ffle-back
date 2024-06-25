package com.mycompany.let_ffle.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration // 설정 파일이라는 의미로 Configuration 어노테이션을 붙여줌 -> 서버가 시작될 때 해당 어노테이션이 붙은 애들을 찾고 설정 내용을 처리
@Slf4j
public class DataSourceConfig {

   @Bean
   public DataSource dataSource() {
      HikariConfig config = new HikariConfig();
      config.setDriverClassName("oracle.jdbc.OracleDriver");
      config.setJdbcUrl("jdbc:oracle:thin:@kosa160.iptime.org:1521:orcl");
      //config.setDriverClassName("net.sf.log4jdbc.DriverSpy");
      //config.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@kosa164.iptime.org:orcl");      
      config.setUsername("user_final_team4");
      config.setPassword("oracle");
      config.setMaximumPoolSize(3);
      HikariDataSource hikariDataSource = new HikariDataSource(config);
      return hikariDataSource;
    // 의존성 주입을 하기 위해 bean 객체로 관리함( 코드의 재사용성과 유지보수에 용이 ) 
    // HikariConfig 객체를 생성
    // 데이터 베이스를 연결할 시 사용할 드라이버를 명시
    // 호스트 이름, 포트번호, SID 를 설정
    // 사용자의 이름을 명시 / 비밀번호 지정 / 데이터베이스 연결 수??
    
   }
	// application.properties 파일에서 작성된 Hikari DataSource 설정을 Java의 클래스로도 가능 : spring boot의 기능
	// 코드 내용은 application.properties 안에 있는 주석을 참고
	

}