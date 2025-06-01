package com.example.klue_sever.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🎹 KLUE Keyboard API")
                        .description("""
                                # KLUE 키보드 추천 시스템 API
                                
                                **키보드 소믈리에 Tony**와 함께하는 완벽한 키보드 구성을 위한 API입니다.
                                
                                ## 주요 기능
                                - 🔍 **부품 검색**: 스위치, 키캡, PCB 등 모든 키보드 부품 조회
                                - 🤖 **AI 추천**: 사용자 선호도 기반 맞춤형 키보드 추천
                                - 📊 **통계 분석**: 인기 부품 및 트렌드 분석
                                - 💰 **가격 비교**: 쇼핑몰별 실시간 가격 정보
                                
                                ## 사용 방법
                                1. `/api/health` - 서버 상태 확인
                                2. `/api/cases` - 키보드 케이스 조회
                                3. `/api/recommendations` - AI 추천 서비스
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("KLUE Team")
                                .email("admin@klue.com")
                                .url("https://github.com/CARRO11/klue-keyboard"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("https://klue-keyboard.up.railway.app")
                                .description("프로덕션 서버")
                ));
    }
} 