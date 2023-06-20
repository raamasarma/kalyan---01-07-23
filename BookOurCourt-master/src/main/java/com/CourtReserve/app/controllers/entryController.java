package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.Referral;
import com.CourtReserve.app.models.User;
import com.CourtReserve.app.models.UserLog;
import com.CourtReserve.app.repositories.ReferralRepository;
import com.CourtReserve.app.repositories.UserLogRepository;
import com.CourtReserve.app.repositories.UserRepository;
import io.netty.handler.codec.http2.Http2SecurityUtil;
import org.hibernate.internal.SessionFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class entryController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReferralRepository referralRepository;
    @Autowired
    UserLogRepository userLogRepository;

    @GetMapping("/public/register")
    public String showRegistrationForm(@ModelAttribute User user, HttpSession session) {
        return "entryTemplates/registrationForm";

//        if (session.getAttribute("verified") != null ){
//            if (session.getAttribute("verified").toString().equals( "true")){
//
//            return "entryTemplates/registrationForm";}
//        } else {
//            return "entryTemplates/verification";
//        }
//        return "entryTemplates/verification";
    }

    @RequestMapping("/public/login")
    public String showLoginForm(HttpSession session){

        return "entryTemplates/login";
    }
    @PostMapping("/public/login")
    public String loginUser(HttpSession session, @RequestParam Map body,HttpSession request) throws UnknownHostException {
        System.out.println(body);
        User user = new User();
        UserLog userLog = new UserLog();
        String result="";
        UserLog usercheck= userLogRepository.findByMobileNoAndStatus(body.get("mobileNo").toString(),"active");
        if(usercheck==null) {
            System.out.println("if exe");
            user = userRepository.findByMobileNoAndPassword(body.get("mobileNo").toString(), body.get("password").toString());
            if (user != null) {
                session.setAttribute("loggedIn", "true");
                session.setAttribute("loggedMobile", user.getMobileNo());
                session.setAttribute("userType", user.getUserType());
                session.setAttribute("user", user.getDict());
                userLog.setUserId(user.getId());
                userLog.setMobileNo((String) body.get("mobileNo"));
                userLog.setIpAddress(String.valueOf(InetAddress.getByName("192.168.29.119")));
                userLog.setSessionId(session.getId());
                userLog.setStatus("active");
                userLogRepository.save(userLog);
                result = "redirect:/";
            } else {
                result = "entryTemplates/login";
                System.out.println("user invalid credentials");
            }
        }else{
            session.setAttribute("error","user already loggedin");
            System.out.println("user already loggedin");
            result= "entryTemplates/login";
        }
        return result;
    }
    @RequestMapping("/public/logout")
    public String loginUser(HttpSession session){
        UserLog userLog=new UserLog();
        System.out.println(userLog.getUserId());
       // User user=new User();
        UserLog usercheck2= userLogRepository.findByUserIdAndStatus(userLog.getUserId(),"active");
        System.out.println("user Checking:"+usercheck2);
//        userLogRepository.delete(usercheck2);
        session.setAttribute("loggedIn", "false");
        session.setAttribute("loggedMobile", null);
        session.setAttribute("userType", null);
        session.setAttribute("user", null);
        return "redirect:/";

    }

    @PostMapping("/public/register")
    public String registerUser(@ModelAttribute User user, HttpSession session) {
        // Process the user registration


        if (user.getReferral().equals("")){
            user.setUserType("NonMember");
        }else{
            System.out.println(referralRepository.findAll());

            Referral referral = referralRepository.findByCode(user.getReferral().toString());

            user.setUserType(referral.getType());

        }
        userRepository.save(user);
        session.setAttribute("verified" , null);
        session.setAttribute("mobileNo", null);

        // Redirect to a success page or perform other actions
        return "redirect:/"; // Name of the success page or URL
    }

}
