package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductRespDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NaverShoppingServiceImpl implements NaverShoppingService {
    @Value("${naver.restapiKey}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;


    public List<ProductRespDto> searchFilteredProducts(String query, int budget) {
        try {
            String url = "https://openapi.naver.com/v1/search/shop.json" +
                "?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) +
                "&display=100&exclude=rental";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .GET()
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            Set<String> seen = new HashSet<>();
            List<ProductRespDto> result = new ArrayList<>();

            for (JsonNode item : root.path("items")) {
                // 필터 조건
                if (!"디지털/가전".equals(item.path("category1").asText())) continue;
                if (!"PC".equals(item.path("category2").asText())) continue;

                int price = item.path("lprice").asInt(0);
                if (price > budget || price == 0) continue;

                String productId = item.path("productId").asText();
                if (seen.contains(productId)) continue;
                seen.add(productId);

                String cleanTitle = item.path("title").asText().replaceAll("<[^>]*>", "");
                String lowerTitle = cleanTitle.toLowerCase();

                List<String> excludedKeywords = List.of(
                        "수랭", "워터", "블록", "백플레이트", "방열판", "라디에이터", "쿨러", "냉각", "히트싱크", "cooler", "waterblock",
                        "컨트롤러", "커버", "수냉", "호환", "브라켓", "kit", "radiator", "cooling", "block",
                        "커넥터", "케이블", "전원", "파워", "psu", "독", "도킹", "외장", "케이스", "프레임", "shell",
                        "렌탈", "rental", "대여", "RGB", "LED", "fan", "리모컨", "조명",
                        "Bykski", "Barrow", "산업용", "workstation",
                        "그래픽 카드 방열판", "쿨링 팬", "그래픽카드 냉각"
                );

                if (excludedKeywords.stream().anyMatch(lowerTitle::contains)) continue;

                System.out.println("상품ID: " + productId + " / 제목: " + cleanTitle);

                result.add(ProductRespDto.builder()
                        .productId(productId)
                        .cpu(null)
                        .gpu(null)
                        .line(null)
                        .totalScores(0.0)
                        .totalPrice(price)
                        .title(cleanTitle)
                        .link(item.path("link").asText())
                        .image(item.path("image").asText())
                        .price(price)
                        .mall(item.path("mallName").asText())
                        .build());
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}