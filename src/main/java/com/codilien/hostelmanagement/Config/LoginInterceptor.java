package com.codilien.hostelmanagement.Config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.*;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // Define a mapping of roles to allowed paths
    private static final Map<String, Set<String>> roleAccessMap = Map.of(
            "ADMIN", Set.of("/api/**", "/auth/logout"),
            "STUDENT", Set.of("/api/complaints/**", "/api/fees/{id}", "/api/students/{id}","/auth/logout"),
            "WARDEN", Set.of("/api/complaints/**", "/api/students/**", "/api/employees/{id}", "/auth/logout"),
            "ACCOUNTANT", Set.of("/api/fees/**", "/api/payments/**", "/api/employees/{id}", "/auth/logout")
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Retrieve the user's role from the session
        String userRole = (String) request.getSession().getAttribute("role");
        String requestURI = request.getRequestURI();

        // Check if the user is logged in
        if (userRole == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("You must log in first.");
            return false;
        }

        // Check if the user's role has access to the requested URI
        Set<String> allowedPaths = roleAccessMap.getOrDefault(userRole, Set.of());
        if (!isPathAllowed(requestURI, allowedPaths)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied");
            return false;
        }

        return true;
    }

    // Check if the request URI matches any of the allowed paths for the user's role
    private boolean isPathAllowed(String requestURI, Set<String> allowedPaths) {
        for (String allowedPath : allowedPaths) {
            if (requestURI.startsWith(allowedPath.replace("**", ""))) {
                return true;
            }
        }
        return false;
    }
}





//package com.codilien.hostelmanagement.Config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class LoginInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession(false);  // Don't create a new session if doesn't exist
//        if (session != null && session.getAttribute("userId") != null && session.getAttribute("role") != null) {
//            return true;
//        } else {
//            // If no valid session
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Unauthorized - Please log in.");
//            return false;
//        }
//    }
//}
