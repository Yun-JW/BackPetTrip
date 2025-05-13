package com.trip.ssafytrip.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.ssafytrip.attraction.dto.AttractionDto;
import com.trip.ssafytrip.api.model.AreaDto;
import com.trip.ssafytrip.api.model.Cat2Dto;
import com.trip.ssafytrip.api.model.CatDto;
import com.trip.ssafytrip.api.model.GunguDto;
import com.trip.ssafytrip.api.repository.ApiRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class cat {
	Integer preCatGroup;
	String preCatName;
	cat (Integer preCatGroup, String preCatName){
		this.preCatGroup = preCatGroup;
		this.preCatName = preCatName;
	}
}

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService{

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();
    private final ApiRepo apiRepo;

    // ✅ 1. 서비스 키 디코딩된 상태로 준비
    String decodedKey = "LTpV7VA+hBpa/omXnilLDyrim8PcM4AlmjniGd4G6vkJRWbQkTjsR+hU+jUnx9lH8UsjhD9SOFgzGnPSaBGrjA==";
    String encodedKey = URLEncoder.encode(decodedKey, StandardCharsets.UTF_8);

    private HttpHeaders createDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        return headers;
    }

//     @Value("${tourapi.service-key}")
//        private String serviceKey;

    @Override
    public void attractionInsert() {
        // TODO Auto-generated method stub


    }

@Override
public void sigunguInsert() {
    // TODO Auto-generated method stub
    //  numOfRows = 20
    Queue<Integer> que = new ArrayDeque<>();
    String temp;
    
    try {
        HttpHeaders headers = createDefaultHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        // area 삽입 -----------------------------------------------------------------
        String url = "https://apis.data.go.kr/B551011/KorPetTourService/areaCode"
                 + "?serviceKey=" + encodedKey
                 + "&numOfRows=1000&MobileOS=WIN&MobileApp=g&_type=json"; 
        URI uri = new URI(url);
        
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String json = response.getBody();
        //System.out.println("응답 내용:\n" + json);
        
        JsonNode items = mapper.readTree(json)
                .path("response").path("body").path("items").path("item");

        List<AreaDto> list = new ArrayList<>();
        for (JsonNode item : items) {
            AreaDto dto = mapper.convertValue(item, AreaDto.class);
            //System.out.println(dto.getName()); // 확인용 출력
            list.add(dto);
            que.add(dto.getCode());
        }
        apiRepo.areaInsert(list);
        
        // 군구 삽입 ---------------------------------------------------------------------
        while(!que.isEmpty()) {
            temp = Integer.toBinaryString(que.poll());
            url = "https://apis.data.go.kr/B551011/KorPetTourService/areaCode"
                    + "?serviceKey=" + encodedKey
                    + "&numOfRows=1000&MobileOS=WIN&MobileApp=g&_type=json&areaCode="
                    + temp;
            uri = new URI(url);
            
            response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            json = response.getBody();
            
            items = mapper.readTree(json)
                    .path("response").path("body").path("items").path("item");
            
            List<GunguDto> gungu_list = new ArrayList<>(); 
            for (JsonNode item : items) {
                GunguDto dto = mapper.convertValue(item, GunguDto.class);
                //System.out.println(dto.getName()); // 확인용 출력
                dto.setSiCode(Integer.parseInt(temp));
                gungu_list.add(dto);
            }
            apiRepo.gunguInsert(gungu_list);
        } 
    } catch (Exception e) {
        e.printStackTrace();
    }
}


@Override
public void categoryInsert() {
    Queue<Cat> que1 = new ArrayDeque<>();
    Queue<Cat> que2 = new ArrayDeque<>();
    Cat tempCat;
    
    
    try {
        HttpHeaders headers = createDefaultHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        // cat1 삽입 -----------------------------------------------------------------
        String url = "https://apis.data.go.kr/B551011/KorPetTourService/categoryCode?"
                 + "serviceKey=" + encodedKey
                 + "&numOfRows=1000&MobileOS=WIN&MobileApp=g&_type=json"; 
        URI uri = new URI(url);
        
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String json = response.getBody();
        //System.out.println("응답 내용:\n" + json);
        
        JsonNode items = mapper.readTree(json)
                .path("response").path("body").path("items").path("item");

        List<CatDto> cat1_list = new ArrayList<>();
        for (JsonNode item : items) {
            CatDto dto = mapper.convertValue(item, CatDto.class);
            //System.out.println(dto.getName()); // 확인용 출력
            cat1_list.add(dto);
            que1.add(new Cat(dto.getCode(), dto.getName()));
        }
        apiRepo.category1Insert(cat1_list);
        
        // cat2 삽입 ---------------------------------------------------------------------
        while(!que1.isEmpty()) {
            tempCat =que1.poll();
            
            url = "https://apis.data.go.kr/B551011/KorPetTourService/categoryCode?"
                     + "serviceKey=" + encodedKey
                    + "&numOfRows=1000&MobileOS=WIN&MobileApp=g&_type=json&cat1="
                    + tempCat.preCatName;
            uri = new URI(url);
            
            response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            json = response.getBody();
            
            items = mapper.readTree(json)
                    .path("response").path("body").path("items").path("item");
            
            List<Cat2Dto> cat2_list = new ArrayList<>(); 
            for (JsonNode item : items) {
                Cat2Dto dto = mapper.convertValue(item, Cat2Dto.class);
                //System.out.println(dto.getName()); // 확인용 출력
                dto.setCat1Group(tempCat.preCatGroup);
                cat2_list.add(dto);
                que2.add(new Cat(dto.getCode(), dto.getName()));
            }
            apiRepo.category2Insert(cat2_list);
            
         // cat2 삽입 ---------------------------------------------------------------------
            while(!que2.isEmpty()) {
                tempCat =que2.poll();
                
                url = "https://apis.data.go.kr/B551011/KorPetTourService/categoryCode?"
                         + "serviceKey=" + encodedKey
                        + "&numOfRows=1000&MobileOS=WIN&MobileApp=g&_type=json&cat1="
                        + tempCat.preCatName.substring(0,3)
                        +"&cat2="
                        + tempCat.preCatName;
                uri = new URI(url);
                
                response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
                json = response.getBody();
                
                items = mapper.readTree(json)
                        .path("response").path("body").path("items").path("item");
                
                List<Cat3Dto> cat3_list = new ArrayList<>(); 
                for (JsonNode item : items) {
                    Cat3Dto dto = mapper.convertValue(item, Cat3Dto.class);
                    //System.out.println(dto.getName()); // 확인용 출력
                    dto.setCat2Group(tempCat.preCatGroup);
                    cat3_list.add(dto);
                }
                apiRepo.category3Insert(cat3_list);
            
        } 
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    
}

@Override
public void sync() {
    // TODO Auto-generated method stub
    
}

@Override
public void init() {
    // TODO Auto-generated method stub
    
}
//    private final ApiRepo apiRepo;
//    @Value("${tourapi.service-key}")
//    private String serviceKey;
//
//    public void fetchAndSaveTours() {
//        String url = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?"
//                   + "MobileOS=ETC&MobileApp=PetPlaceApp&_type=json"
//                   + "&areaCode=1&numOfRows=100&pageNo=1&serviceKey=" + serviceKey;
//
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            String response = restTemplate.getForObject(url, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode items = mapper.readTree(response)
//                    .path("response").path("body").path("items").path("item");
//
//            List<Tour> tours = new ArrayList<>();
//            for (JsonNode item : items) {
//                Tour tour = new Tour();
//                tour.setTitle(item.path("title").asText());
//                tour.setAddr(item.path("addr1").asText());
//                tour.setMapx(item.path("mapx").asDouble());
//                tour.setMapy(item.path("mapy").asDouble());
//                tour.setContentId(item.path("contentid").asText());
//                tour.setContentTypeId(item.path("contenttypeid").asText());
//                tour.setFirstImage(item.path("firstimage").asText());
//                tours.add(tour);
//            }
//
//            tourApiRepository.saveTours(tours);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
