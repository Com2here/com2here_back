package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductResponseDto;
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


    public List<ProductResponseDto> searchFilteredProducts(String query, int budget) {
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
            List<ProductResponseDto> result = new ArrayList<>();

            for (JsonNode item : root.path("items")) {
                // 필터 조건
                if (!"디지털/가전".equals(item.path("category1").asText())) continue;
                if (!"PC".equals(item.path("category2").asText())) continue;

                int price = item.path("lprice").asInt(0);
                if (price > budget || price == 0) continue;

                String title = item.path("title").asText().replaceAll("<[^>]*>", "").toLowerCase();
                String mallName = item.path("mallName").asText().toLowerCase();

                // 해외 배송/직구 키워드 필터링
                if (title.contains("해외") || title.contains("직구") || mallName.contains("해외") || mallName.contains("직구")) continue;

                String productId = item.path("productId").asText();
                if (seen.contains(productId)) continue;
                seen.add(productId);

                // HTML 태그 제거 (ex: <b>조립PC</b>)
                String cleanTitle = item.path("title").asText().replaceAll("<[^>]*>", "");

                result.add(ProductResponseDto.builder()
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