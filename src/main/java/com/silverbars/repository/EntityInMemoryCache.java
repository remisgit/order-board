package com.silverbars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class EntityInMemoryCache <T> {

    private final ConcurrentMap<Long, T> entitiesMap;

    private final AtomicLong cachedItemsIdCounter;

    public Long getNextId(){
        return cachedItemsIdCounter.incrementAndGet();
    }

    public void save(Long id, T t){
        entitiesMap.put(id,t);
    }

    public T get(Long id){
        return entitiesMap.get(id);
    }

    public T remove(Long id){
        return entitiesMap.remove(id);
    }

    public List<T> getAllValues(){
        return entitiesMap.values().stream().collect(collectingAndThen(toList(),
                Collections::unmodifiableList));
    }

}
