package com.silverbars.service;

import com.silverbars.model.Order;
import com.silverbars.repository.EntityInMemoryCache;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@ActiveProfiles("cache_persisted")
@SpringBootTest
@AutoConfigureMockMvc
public class MapBackedRestControllerIT extends BaseRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    protected List<Order> initialOrderList;

    @Autowired
    private EntityInMemoryCache entityInMemoryCache;

    @Autowired
    AtomicLong cachedItemsIdCounter;

    @Autowired
    public ConcurrentMap<Long, Order> entitiesMap;

    @Before
    public void setup() throws Exception {
        setMvc(mvc);
        setInitialOrderList(initialOrderList);


        entitiesMap.clear();
        cachedItemsIdCounter.set(0);

        //refresh to the initial state before each test
        initialOrderList.forEach(o -> {
            entityInMemoryCache.save(entityInMemoryCache.getNextId(), o);
        });
    }

}
