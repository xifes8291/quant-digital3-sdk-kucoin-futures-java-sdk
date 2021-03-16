/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core;

import com.kucoin.futures.core.rest.request.DuringHasMoreRequest;
import com.kucoin.futures.core.rest.request.DuringPageRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by chenshiwei on 2019/1/21.
 */
public class BaseTest {
    protected static KucoinFuturesRestClient futuresRestClient;
    protected static KucoinFuturesPrivateWSClient kucoinFuturesPrivateWSClient;

    protected static Long startAt;
    protected static Long endAt;


    protected static DuringPageRequest pageRequest;
    protected static DuringHasMoreRequest hasMoreRequest;

    protected final static String SYMBOL = "XBTUSDTM";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws IOException {
        // sandbox
//        futuresRestClient = new KucoinFuturesClientBuilder().withBaseUrl("https://api-sandbox-futures.kucoin.cc")
//                .withApiKey("604dd0fe365ac600068976d6", "09f3e686-f1d5-4cc3-9a3e-5d60c29d3703", "1828380")
//                .buildRestClient();

        KucoinFuturesClientBuilder builder = new KucoinFuturesClientBuilder()
                .withBaseUrl("https://api-futures.kucoin.cc")
                .withApiKey("60010212d304000006c2e490", "b9626b9a-c869-413f-b1d3-cc5858171d53", "1828380");
        futuresRestClient = builder.buildRestClient();
        kucoinFuturesPrivateWSClient = builder.buildPrivateWSClient();
        kucoinFuturesPrivateWSClient.connect();

        startAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0).atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
        endAt = LocalDateTime.of(2021, 4, 30, 0, 0, 0).atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();

        pageRequest = DuringPageRequest.builder().starAt(startAt).endAt(endAt).currentPage(1).pageSize(10).build();
        hasMoreRequest = DuringHasMoreRequest.builder().starAt(startAt).endAt(endAt).offset(0).maxCount(10).build();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        kucoinFuturesPrivateWSClient.close();
    }

    @Test
    public void timeAPI() throws Exception {
        Long serverTimeStamp = futuresRestClient.timeAPI().getServerTimeStamp();
        assertThat(System.currentTimeMillis() - serverTimeStamp, lessThanOrEqualTo(5000L));
    }

}