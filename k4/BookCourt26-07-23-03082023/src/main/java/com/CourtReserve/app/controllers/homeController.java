package com.CourtReserve.app.controllers;


import com.CourtReserve.app.models.Notifies;
import com.CourtReserve.app.repositories.NotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class homeController {

    @Autowired
    NotifyRepository notifyRepository;

    @RequestMapping("/")
    public String home(HttpSession session, Model model){
        List<String> messages = new ArrayList<>();
        String messages1="Successfully login";
        //String messages2="Successfully Register";
       // model.addAttribute("messages",messages2);
        if (session.getAttribute("loggedIn").equals("true") ){
        Map user = (Map) session.getAttribute("user");
        if (!user.get("userType").equals("Admin")){
            List<Notifies> notifies = notifyRepository.findByUserOrderByIdDesc(user.get("mobileNo").toString());
            model.addAttribute("notifies", notifies);
            model.addAttribute("messages",messages1);

        }}

        return "home" ;
    }



}
