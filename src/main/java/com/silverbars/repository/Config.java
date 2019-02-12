package com.silverbars.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverbars.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Configuration
@EnableTransactionManagement
public class Config {

    @Value( "#{systemProperties['init.orders.file.path']}" )
    private String initialOrdersFilePath;

    @Bean
    public List<Order> initialOrderList() throws IOException {
        log.info("initial orders file path = '{}'",initialOrdersFilePath);
        if(StringUtils.isEmpty(initialOrdersFilePath)){
            return new ArrayList<>();
        }
        String json = FileUtils.readFileToString(new File(initialOrdersFilePath));
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
