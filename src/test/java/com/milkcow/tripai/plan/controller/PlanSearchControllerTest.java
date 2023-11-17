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
class PlanSearchControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void mockMvcSetUP() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void 항공권조회_컨트롤러() throws Exception {
        //given
        final String api = "/plan/budget/flight";
        final String departureAirport = "LAX";
        final String arrivalAirport = "ICN";
        final String departure = "2023-12-25";
        final int maxFare = 2000000;

        final String apiUrl = api +
                "?departureAirport=" + departureAirport +
                "&arrivalAirport=" + arrivalAirport +
                "&departure=" + departure +
                "&maxFare=" + maxFare;
        //when
        final ResultActions result = mockMvc.perform(get(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
        System.out.println(
                "result.andReturn().getResponse() = " + result.andReturn().getResponse().getContentAsString());
    }
    @Test
    public void 항공권조회_컨트롤러_날짜형식예외() throws Exception {
        //given
        final String api = "/plan/budget/flight";
        final String departureAirport = "LAX";
        final String arrivalAirport = "ICN";
        final String departure = "20231225";
        final int maxFare = 2000000;

        final String apiUrl = api +
                "?departureAirport=" + departureAirport +
                "&arrivalAirport=" + arrivalAirport +
                "&departure=" + departure +
                "&maxFare=" + maxFare;
        //when
        final ResultActions result = mockMvc.perform(get(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    public void 숙박_조회_컨트롤러() throws Exception {
        //given
        final String api = "/plan/budget/accommodation";
        String destination = "도쿄";
        String startDate = "2023-12-25";
        String endDate = "2023-12-31";
        final int maxPrice = 1500000;

        final String apiUrl = api +
                "?destination=" + destination +
                "&startDate=" + startDate +
                "&endDate=" + endDate +
                "&maxPrice=" + maxPrice;
        //when
        ResultActions result = mockMvc.perform(
                get(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().isOk());
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        System.out.println("result.andReturn().getResponse() = " + contentAsString);
    }
    @Test
    public void 맛집_조회_컨트롤러() throws Exception {
        //given
        final String api = "/plan/budget/restaurant";
        String destination = "tokyo";
        String startDate = "2023-12-25";
        String endDate = "2023-12-31";
        final int maxPrice = 1500000;

        final String apiUrl = api +
                "?destination=" + destination +
                "&startDate=" + startDate +
                "&endDate=" + endDate +
                "&maxPrice=" + maxPrice;
        //when
        ResultActions result = mockMvc.perform(
                get(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().isOk());
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        System.out.println("result.andReturn().getResponse() = " + contentAsString);
    }
    @Test
    public void 맛집_조회_컨트롤러_목적지_한글예외() throws Exception {
        //given
        final String api = "/plan/budget/restaurant";
        String destination = "도쿄";
        String startDate = "2023-12-25";
        String endDate = "2023-12-31";
        final int maxPrice = 1500000;

        final String apiUrl = api +
                "?destination=" + destination +
                "&startDate=" + startDate +
                "&endDate=" + endDate +
                "&maxPrice=" + maxPrice;
        //when
        ResultActions result = mockMvc.perform(
                get(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().is4xxClientError());
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        System.out.println("result.andReturn().getResponse() = " + contentAsString);
    }
    @Test
    public void 맛집_조회_컨트롤러_목적지_글자예외() throws Exception {
        //given
        final String api = "/plan/budget/restaurant";
        String destination = "a";
        String startDate = "2023-12-25";
        String endDate = "2023-12-31";
        final int maxPrice = 1500000;

        final String apiUrl = api +
                "?destination=" + destination +
                "&startDate=" + startDate +
                "&endDate=" + endDate +
                "&maxPrice=" + maxPrice;
        //when
        ResultActions result = mockMvc.perform(
                get(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().is4xxClientError());
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        System.out.println("result.andReturn().getResponse() = " + contentAsString);
    }

    @Test
    public void 해외명소_조회_컨트롤러() throws Exception {
        //given
        final String api = "/plan/budget/attraction/international";
        String destination = "tokyo";
        final int maxPrice = 1500000;

        final String apiUrl = api +
                "?destination=" + destination +
                "&maxPrice=" + maxPrice;
        //when
        ResultActions result = mockMvc.perform(
                get(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().isOk());
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        System.out.println("result.andReturn().getResponse() = " + contentAsString);
    }
}