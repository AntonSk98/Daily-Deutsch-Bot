package com.ansk.development.learngermanwithansk98;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DailyDeutschBotTestConfig.class)
class DailyDeutschBotTests {

    @Test
    void contextLoads() {
    }

}
