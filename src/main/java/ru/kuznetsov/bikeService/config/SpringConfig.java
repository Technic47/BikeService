package ru.kuznetsov.bikeService.config;

import io.nats.client.Connection;
import io.nats.client.ErrorListener;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan("ru.kuznetsov.bikeService")
@EnableJpaRepositories("ru.kuznetsov.bikeService.repositories")
@EntityScan("ru.kuznetsov.bikeService.models")
@EnableMethodSecurity(securedEnabled = true)
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    public static String UPLOAD_PATH;
    public static String ADMIN_NAME;
    public static String ADMIN_PASS;
    public static String BACK_LINK;
    @Value("${upload.path}")
    private String path;
    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.pass}")
    private String adminPass;
    @Value("${datasource.driver}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${http.port}")
    private int httpPort;
    @Value("${server.port}")
    private int httpsPort;
    @Value("${return.link}")
    private String backLink;
    @Value("${spring.mail.host}")
    private String smtpHost;
    @Value("${spring.mail.port}")
    private String smtpPort;
    @Value("${spring.mail.username}")
    private String smtpUserName;
    @Value("${spring.mail.password}")
    private String smtpUserPass;
    public static String NATS_SERVER;
    @Value("${nats.server}")
    private String natsServer;
    public static final String SUBSCRIBER_PDF = "server.pdf";
    public static final String SUBSCRIBER_Picture = "server.getPicture";

    @Value("${upload.path}")
    private void setUploadPath() {
        UPLOAD_PATH = this.path;
        ADMIN_NAME = adminName;
        ADMIN_PASS = adminPass;
        BACK_LINK = backLink;
        NATS_SERVER = natsServer;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(jdbcURL);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/IMG/**")
                .addResourceLocations("file:" + UPLOAD_PATH + "/");
        registry.addResourceHandler("/preview/**")
                .addResourceLocations("file:" + UPLOAD_PATH + "/preview/");
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/**");
    }


    @Bean(name = "MainExecutor")
    public ExecutorService getMainService() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(coreCount);
    }

    @Bean(name = "AdditionExecutor")
    public ExecutorService getAdditionService() {
        return Executors.newFixedThreadPool(2);
    }

    //redirect from http to https
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        return connector;
    }

    //email sender setUp
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(Integer.parseInt(smtpPort));
        mailSender.setUsername(smtpUserName);
        mailSender.setPassword(smtpUserPass);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

    //NATS
    @Bean
    public Connection getConnection(@Value("${nats.server}") String server) throws IOException, InterruptedException {
        Options options = new Options.Builder()
                .server(server)
                .connectionListener((connection, eventType) -> {
                })
                .errorListener(new ErrorListener() {
                })
                .build();

        return Nats.connect(options);
    }
}