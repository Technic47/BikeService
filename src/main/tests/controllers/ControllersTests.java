package controllers;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kuznetsov.bikeService.Starter;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Starter.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllersTests {

}
