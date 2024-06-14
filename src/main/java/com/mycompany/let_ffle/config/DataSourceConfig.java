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
	// application.properties 파일에서 작성된 Hikari DataSource 설정을 Java의 클래스로도 가능 : spring boot의 기능
	// 코드 내용은 application.properties 안에 있는 주석을 참고
	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("oracle.jdbc.OracleDriver");
		config.setJdbcUrl("jdbc:oracle:thin:@kosa164.iptime.org:1521:orcl");
		// config.setDriverClassName("net.sf.log4jdbc.DriverSpy");
		// config.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@kosa164.iptime.org:orcl");
		config.setUsername("user_spring");
		config.setPassword("oracle");
		config.setMaximumPoolSize(3);
		HikariDataSource hikariDataSource = new HikariDataSource(config);
		return hikariDataSource;
	}
}