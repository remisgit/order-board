package com.silverbars.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import com.silverbars.repository.EntityInMemoryCache;
import com.silverbars.repository.HashMapOrderService;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("cache_persisted")
@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerIntegrationTest {

    /*@Configuration
    //@ComponentScan("comcom.silverbars")
    public static class SpringConfig {
        @Bean
        public ConcurrentMap<Long,Order> entitiesMap(){
            ConcurrentHashMap<Long, Order> cache = new ConcurrentHashMap<>();
            return new ConcurrentHashMap<>();
        }
    }

    ConcurrentHashMap<Long, Order> cache;*/

    @Autowired
    private MockMvc mvc;

    @Autowired
    List<Order> initialOrderList;

    @Autowired
    private HashMapOrderService hashMapOrderService;

    @Autowired
    private EntityInMemoryCache entityInMemoryCache;

    @Autowired
    AtomicLong cachedItemsIdCounter;

    @Autowired
    public ConcurrentMap<Long,Order> entitiesMap;

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        entitiesMap.clear();
        cachedItemsIdCounter.set(0);
        initialOrderList.forEach(o -> {
            entityInMemoryCache.save(entityInMemoryCache.getNextId(),o);
        });
    }

    @Test
    public void testGetAllOrders() throws Exception {
        MvcResult rezult = mvc.perform(get("/getallorders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String content = rezult.getResponse().getContentAsString();
        List<Order> orders = jsonObjectMapper.readValue(content, new TypeReference<List<Order>>() {});

        Assert.assertThat(orders, IsIterableContainingInAnyOrder.containsInAnyOrder(initialOrderList.toArray()));
    }

    @Test
    public void testRegisterNewOrder() throws Exception {

        Order newOrderToBePersisted = Order.builder().userId("testUser_99").build();
        MvcResult rezult = mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonObjectMapper.writeValueAsString(newOrderToBePersisted)))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        Order persistedOrder = getOrderFromResultJson(rezult);
        Assert.assertEquals(Long.valueOf(6L),persistedOrder.getId());
        Assert.assertEquals("testUser_99",persistedOrder.getUserId());

        //check if it was actually persisted
        MvcResult getOrderRezult = mvc.perform(get("/getorder").param("orderId", persistedOrder.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        Order actualOrder = getOrderFromResultJson(rezult);
        Assert.assertEquals(newOrderToBePersisted.getUserId(),actualOrder.getUserId());
    }



    @Test
    public void testCancelExistingOrder() throws Exception {
        Order newOrder = Order.builder().userId("testUser_101").build();
        MvcResult registerRezult = mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonObjectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk())
                .andReturn();
        Order persistedOrder = getOrderFromResultJson(registerRezult);

        MvcResult cancelOrderRezult = mvc.perform(get("/cancel").param("orderId", persistedOrder.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        Order canceledOrder = getOrderFromResultJson(cancelOrderRezult);
        Assert.assertEquals("persisted order is expected to be equal to caneled order",newOrder.getUserId(),canceledOrder.getUserId());

        mvc.perform(get("/getorder").param("orderId", canceledOrder.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(equalTo("Order with id = [6] not found")))
                .andReturn();

    }

    @Test
    public void testCancelNonExistingOrderFail() throws Exception {
        mvc.perform(get("/cancel").param("orderId", "103"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(equalTo("could not cancel order as it does not exist, orderId = [103]")))
                .andReturn();
    }


    @Test
    public void testGetSummary() throws Exception {
        MvcResult result = mvc.perform(get("/aggregate"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String persistedContent = result.getResponse().getContentAsString();
        List<OrderSummary> summary = jsonObjectMapper.readValue(persistedContent, new TypeReference<List<OrderSummary>>() {});
        assertThat(summary.size()).isEqualTo(4);

        //more on
        //http://joel-costigliola.github.io/assertj/
        System.out.println();
    }


    private Order getOrderFromResultJson(MvcResult rezult) throws IOException {
        String persistedContent = rezult.getResponse().getContentAsString();
        return jsonObjectMapper.readValue(persistedContent, new TypeReference<Order>() {});
    }

}