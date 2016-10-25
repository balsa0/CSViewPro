package com.csviewpro.config;

import org.springframework.context.annotation.Configuration;


/**
 * Created by Balsa on 2016. 10. 19..
 */
@Configuration
public class DatabaseConfig {

//	@Bean(name = "settings-db")
//	public DataSource dataSource(){
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setUrl("jdbc:hsqldb:file:internaldatabase");
//		dataSource.setUsername("sa");
//		dataSource.setPassword("");
//
//		try{
//			dataSource.getConnection().createStatement().execute("SELECT * FROM FILE_FAVOURITES;");
//		}catch (SQLException e){
//			//create database
//			System.out.println("CREATING SCHEMA");
//			try{
//				dataSource.getConnection().createStatement()
//						.execute("CREATE TABLE FILE_FAVOURITES (path  VARCHAR(1000))");
//
//			}catch (SQLException f){}
//		}
//
//		System.out.println("DB INITIALIZED: "+dataSource.getUrl());
//
//		return dataSource;
//	}

}
