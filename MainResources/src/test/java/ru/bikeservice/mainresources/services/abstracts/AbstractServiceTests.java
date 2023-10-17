package ru.bikeservice.mainresources.services.abstracts;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public abstract class AbstractServiceTests {
}
