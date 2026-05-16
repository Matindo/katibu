package com.katibu;

import com.katibu.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class KatibuApplicationTests {

    @MockBean
    MinioService minioService;

    @Test
    void contextLoads() {
    }
}
