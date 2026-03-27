package com.example.gymmanagement;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class GymManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymManagementApplication.class, args);
    }

    // PHP는 웹 서버(Apache/Nginx)가 진입점 역할을 하지만, Spring Boot는 내장 서버를 직접 실행하는
    // main() 메서드가 필요하다.
    // @SpringBootApplication 은 이 클래스가 앱의 시작점임을 Spring에게 알려준다.
}
