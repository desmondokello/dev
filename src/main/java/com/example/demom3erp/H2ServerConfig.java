//package com.example.demom3erp;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.h2.tools.Server;
//
//@Configuration
//public class H2ServerConfig {
//
//    @Bean
//    public CommandLineRunner startH2Server() {
//        return args -> {
//            // Start the H2 TCP server
//            Server tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090").start();
//            System.out.println("H2 TCP server started at " + tcpServer.getURL());
//        };
//    }
//}
//
//
