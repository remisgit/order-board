package com.silverbars.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverbars.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Configuration
public class Config {

    @Bean
    public List<Order> initialOrderList() throws IOException {
        log.info("Configuration reading initialList <Long,T> ----------------------------------------------");
        String json = FileUtils.readFileToString(new File("src/main/resources/initial_orders.txt"));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<Order>>() {});
    }

    @Bean
    public ConcurrentMap<Long,Order> entitiesMap(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public AtomicLong cachedItemsIdCounter(){
        return new AtomicLong();
    }
}
