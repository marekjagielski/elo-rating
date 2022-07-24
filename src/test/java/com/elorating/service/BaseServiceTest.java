package com.elorating.service;

import com.elorating.CoreApplication;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by pokor on 10.06.2017.
 */
@SpringBootTest(classes = CoreApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public abstract class BaseServiceTest {
}
