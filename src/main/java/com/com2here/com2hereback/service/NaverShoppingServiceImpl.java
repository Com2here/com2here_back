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

                // HTML 태그 제거 (ex: <b>조립PC</b>)
                String cleanTitle = item.path("title").asText().replaceAll("<[^>]*>", "");

                result.add(new ProductRespDto(
                    cleanTitle,
                    item.path("link").asText(),
                    item.path("image").asText(),
                    price,
                    item.path("mallName").asText()
                ));
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}