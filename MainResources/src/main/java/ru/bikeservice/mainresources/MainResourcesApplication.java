package ru.bikeservice.mainresources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableJpaAuditing
public class MainResourcesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MainResourcesApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainResourcesApplication.class);
    }
/*TODO
- Thread name to logging
- REST API module ???
- admin module
- logOut user work (active true/false) User may stay active.
- resend token via login, not email
- oauth2 from VK
 */
}
