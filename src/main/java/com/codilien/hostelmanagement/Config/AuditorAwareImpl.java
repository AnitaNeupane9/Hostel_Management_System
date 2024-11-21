package com.codilien.hostelmanagement.Config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditorAwareImpl implements AuditorAware<String> {

//    private final HttpSession httpSession;
//
//    public AuditorAwareImpl(HttpSession httpSession) {
//        this.httpSession = httpSession;
//    }


    @Override
    public Optional<String> getCurrentAuditor()
    {
//        if (httpSession != null) {
//            String currentAuditor = (String) httpSession.getAttribute("userId");
//
//            if (currentAuditor != null) {
//                return Optional.of(currentAuditor);
//            } else {
//                System.out.println("UserId is not set in session.");
//            }
//        } else {
//            System.out.println("HttpSession is null.");
//        }
//        return Optional.of("Unknown User");
//    }

        return Optional.of("Alien");

    }

//    private final HttpServletRequest httpServletRequest;
//
//    public AuditorAwareImpl(HttpServletRequest httpServletRequest) {
//        this.httpServletRequest = httpServletRequest;
//    }
//
//    @Override
//    public Optional<String> getCurrentAuditor()
//    {
//        System.out.println("getCurrentAuditor called");
//        return Optional.ofNullable(getuserId(httpServletRequest));
//    }
//
//    private String getuserId(HttpServletRequest httpServletRequest)
//    {
//        HttpSession session = httpServletRequest.getSession(false);
//        if (session != null) {
//            String userId = (String) session.getAttribute("userId");
//            System.out.println(userId);
//            return userId;
//        } else {
//            System.out.println("HttpSession is null.");
//            return null;
//        }
//    }
}
