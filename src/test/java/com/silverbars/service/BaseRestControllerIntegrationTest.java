package com.silverbars.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverbars.model.Order;
import com.silverbars.model.OrderSummary;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Setter
@Transactional
public abstract class BaseRestControllerIntegrationTest {

    static {
        System.setProperty("init.orders.file.path", "src/test/resources/initial_orders.txt");
    }

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    protected MockMvc mvc;

    protected List<Order> initialOrderList;

    @Test
    public void testGetAllOrders() throws Exception {
        MvcResult rezult = mvc.perform(get("/getallorders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String content = rezult.getResponse().getContentAsString();
        List<Order> orders = jsonObjectMapper.readValue(content, new TypeReference<List<Order>>() {
        });

        Assert.assertThat(orders, IsIterableContainingInAnyOrder.containsInAnyOrder(initialOrderList.toArray()));
    }

    @Test
    public void testRegisterNewOrder() throws Exception {

        Order newOrderToBePersisted = Order.builder().userId("testUser_99").orderQuantity(1.0).orderPrice(1L).build();
        MvcResult rezult = mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonObjectMapper.writeValueAsString(newOrderToBePersisted)))
                .andExpect(status().isOk())
                .andReturn();

        Order persistedOrder = getOrderFromResultJson(rezult);
        assertThat(persistedOrder.getId()).isEqualTo(9L);
        assertThat(persistedOrder.getUserId()).isEqualTo("testUser_99");

        //check if it was actually persisted
        MvcResult getOrderRezult = mvc.perform(get("/getorder").param("orderId", persistedOrder.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        Order actualOrder = getOrderFromResultJson(rezult);
        assertThat(newOrderToBePersisted.getUserId()).isEqualTo(actualOrder.getUserId());
    }

    @Test
    public void testCancelExistingOrder() throws Exception {
        Order newOrder = Order.builder().userId("testUser_101").orderQuantity(1.0).orderPrice(1L).build();
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
        Assert.assertEquals("persisted order is expected to be equal to caneled order", newOrder.getUserId(), canceledOrder.getUserId());

        mvc.perform(get("/getorder").param("orderId", canceledOrder.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(equalTo(format("Order with id = [%s] not found",canceledOrder.getId().toString()))))
                .andReturn();
    }

    @Test
    public void testCancelNonExistingOrderFail() throws Exception {
        mvc.perform(get("/cancel").param("orderId", "103"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(equalTo("could not cancel order as it does not exist, orderId = [103]" )))
                .andReturn();
    }

    @Test
    public void testGetSummary() throws Exception {
        MvcResult result = mvc.perform(get("/aggregate"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String persistedContent = result.getResponse().getContentAsString();
        List<OrderSummary> actualSummaryList = jsonObjectMapper.readValue(persistedContent, new TypeReference<List<OrderSummary>>() {
        });

        String json = FileUtils.readFileToString(new File("src/test/resources/expected_orders_summary_list.txt"));
        ObjectMapper mapper = new ObjectMapper();
        List<OrderSummary> expectedOrderSummaryList = mapper.readValue(json, new TypeReference<List<OrderSummary>>() {
        });

        Assert.assertThat(actualSummaryList, IsIterableContainingInOrder.contains(expectedOrderSummaryList.toArray()));
    }

    private Order getOrderFromResultJson(MvcResult rezult) throws IOException {
        String persistedContent = rezult.getResponse().getContentAsString();
        return jsonObjectMapper.readValue(persistedContent, new TypeReference<Order>() {});
    }

}
