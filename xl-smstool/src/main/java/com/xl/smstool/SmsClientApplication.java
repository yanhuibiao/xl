package com.xl.smstool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SmsClientApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // 初始化 Spring Boot 上下文
        springContext = SpringApplication.run(SmsClientApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/xl/smstool/sms-client.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("短信测试工具");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // 在 JavaFX 应用关闭时关闭 Spring 上下文
        if (springContext != null) {
            springContext.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
