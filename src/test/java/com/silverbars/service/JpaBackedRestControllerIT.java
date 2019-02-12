package com.silverbars.service;

import com.silverbars.model.Order;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("db_persisted")
@SpringBootTest
@AutoConfigureMockMvc
public class JpaBackedRestControllerIT extends BaseRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected List<Order> initialOrderList;

    @Before
    public void setup() {
        setMvc(mvc);
        setInitialOrderList(initialOrderList);
    }

}
