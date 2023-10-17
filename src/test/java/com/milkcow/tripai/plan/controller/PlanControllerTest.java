package com.milkcow.tripai.plan.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlanControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void mockMvcSetUP(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void 항공권조회_컨트롤러() throws Exception{
        //given
        final String api = "/plan/budget/flight";
        final String departureAirport = "LAX";
        final String arrivalAirport = "ICN";
        final String departure = "20231225";
        final int maxFare = 2000000;

        final String url = api +
                "?departureAirport="+departureAirport+
                "&arrivalAirport="+arrivalAirport+
                "&departure="+departure+
                "&maxFare="+maxFare;
        System.out.println("url = " + url);

        //when
        final ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
        System.out.println("result.andReturn().getResponse() = " + result.andReturn().getResponse().getContentAsString());
    }
}