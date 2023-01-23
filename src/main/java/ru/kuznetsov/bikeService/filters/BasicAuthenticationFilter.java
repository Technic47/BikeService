//package ru.kuznetsov.bikeService.filters;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class BasicAuthenticationFilter implements Filter {
//    private final static String LOGIN = "user";
//    private final static String PASSWORD = "user";
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        //
//        String authHeader = req.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Basic ")) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getOutputStream().println("Please, LogIn!");
//            resp.getOutputStream().close();
//            return;
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
