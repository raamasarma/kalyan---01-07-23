package com.CourtReserve.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userType; //User Type  - Admin / Member / Non member / VIP / TopMgmt
    private String mobileNo;
    private String email;
    private String userName;
    private String password;
    private String location;
    private String country; // Countries -
    private String referral = "";
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime lastLogin;

    // Constructors, getters, and setters

    public Map getDict(){
        Map dict = new HashMap<>();
        dict.putIfAbsent("id",id);
        dict.putIfAbsent("userType", userType);
        dict.putIfAbsent("userName", userName);
        dict.putIfAbsent("mobileNo", mobileNo);
        dict.putIfAbsent("email", email);
        dict.putIfAbsent("location", location);
        dict.putIfAbsent("country", country);

        return dict;
    }
//    public class User implements HttpSessionBindingListener {
//        private static Map<User, HttpSession> logins = new HashMap<User, HttpSession>();
//        private Long id;
//        private String mobileNo;
//        @Override
//        public boolean equals(Object other) {
//            return (other instanceof User) && (id != null) ? id.equals(((User) other).id) : (other == this);
//        }
//        @Override
//        public int hashCode() {
//            return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
//        }
//        @Override
//        public void valueBound(HttpSessionBindingEvent event) {
//            HttpSession session = logins.remove(this);
//            if (session != null) {
//                session.invalidate();
//            }
//            logins.put(this, event.getSession());
//        }
//        @Override
//        public void valueUnbound(HttpSessionBindingEvent event) {
//            logins.remove(this);
//        }
//    }
}
