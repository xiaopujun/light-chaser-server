package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.service.ConnectService;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class ConnectServiceImpl implements ConnectService {

    @Override
    public String testConnect(String name, String pwd) {
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                return "mysql driver load err...";
            }
            String url = "jdbc:mysql://localhost:3306/light_chaser_server?user=root&password=123456";
            DriverManager.getConnection(url);
        } catch (SQLException ex1) {
            System.out.print("connect err, please check username and password...");
        }
        return "connect success...";
    }

}
