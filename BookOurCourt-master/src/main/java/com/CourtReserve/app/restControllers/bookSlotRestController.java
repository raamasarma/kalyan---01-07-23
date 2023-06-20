package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.models.BookSlot;
import com.CourtReserve.app.models.Slot;
import com.CourtReserve.app.models.SpecialDates;
import com.CourtReserve.app.models.User;
import com.CourtReserve.app.repositories.BookSlotRepository;
import com.CourtReserve.app.repositories.SlotRepository;
import com.CourtReserve.app.repositories.SpecialDatesRepository;
import com.CourtReserve.app.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class bookSlotRestController {
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final BookSlotRepository bookSlotRepository;
    private final SpecialDatesRepository specialDatesRepository;

    public bookSlotRestController(SlotRepository slotRepository,
                                  UserRepository userRepository,
                                  BookSlotRepository bookSlotRepository,
                                  SpecialDatesRepository specialDatesRepository) {
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.bookSlotRepository = bookSlotRepository;
        this.specialDatesRepository = specialDatesRepository;
    }

    @PostMapping("/requestSlot")
    public ResponseEntity requestSlotPost(@RequestParam Map map){
        System.out.println(map);
        Map response = new HashMap();
        String date = map.get("date").toString();
        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );

        Slot slot = slotRepository.findBySlotCode(map.get("slotCode").toString());
        System.out.println(slot);
        User user = userRepository.findByMobileNo(map.get("userMobile").toString());
        BookSlot bookSlot = new BookSlot();
        int series = 1;
        Iterable<BookSlot> bookSlotsIterator = bookSlotRepository.findAll();
        List<BookSlot> bookSlotsList = new ArrayList<>();
        for (BookSlot getSeries: bookSlotsIterator ){
            bookSlotsList.add(getSeries);
        }
        if (!bookSlotsList.isEmpty()){
            String bookingNo = bookSlotsList.get(bookSlotsList.size()-1).getBookingNo();
            series = Integer.parseInt(bookingNo.split("-")[bookingNo.split("-").length - 1]) + 1;
        }
        bookSlot.setSlotCode(slot.getSlotCode());
        bookSlot.setCourtCode(slot.getCourtCode());
        bookSlot.setBookingDate(LocalDate.now());
        bookSlot.setGameDate(dateModified);
        bookSlot.setBookingDate(dateModified);
        bookSlot.setBookTime(LocalDateTime.now());
        bookSlot.setBookedBy(user.getMobileNo());
        bookSlot.setConfirmStatus("pending");
        bookSlot.setRefNo(map.get("referredBy").toString());

        bookSlot.setRemarksByUser(map.get("remarks").toString());
        String bkNo= dateModified.format(DateTimeFormatter.ofPattern("ddMMyyyy"))+ "-"+slot.getStartHour() +"-"+series;
        bookSlot.setBookingNo(bkNo);
        bookSlotRepository.save(bookSlot);

        response.putIfAbsent("msg", "success");
        response.putIfAbsent("status", 202);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/approveSlot")
    public ResponseEntity approveSlot(@RequestParam Map map, HttpSession session){
        System.out.println(map);
        Map response = new HashMap();
        Optional<BookSlot> bookSlotResp = bookSlotRepository.findById(Integer.parseInt(map.get("id").toString()));
        BookSlot bookSlot = bookSlotResp.get();
        System.out.println(bookSlot);


        Map user = (Map) session.getAttribute("user");
        bookSlot.setApprovedBy(user.get("userName").toString());
        bookSlot.setApprovedTime(LocalDateTime.now());
        bookSlot.setConfirmStatus("accepted");
        bookSlot.setRemarksByAdmin(map.get("remarks").toString());


        List<BookSlot> bookSlots = bookSlotRepository.findByGameDateAndSlotCode(bookSlot.getGameDate(), bookSlot.getSlotCode());
        for(BookSlot bookSlot1 : bookSlots){
            if (bookSlot1 != bookSlot){
                bookSlot1.setConfirmStatus("rejected");
                bookSlot1.setApprovedBy(user.get("userName").toString());
            }

        }

        User user1 = userRepository.findByMobileNo(bookSlot.getBookedBy());

        response.putIfAbsent("msg", "successfully accepted request of " + user1.getUserName() + " a " + user1.getUserType() + " to use court " + bookSlot.getCourtCode() + " of slotCode:  "+ bookSlot.getSlotCode() + " while rejecting all others;" );
        response.putIfAbsent("status", 202);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/getMonthDays")
    public ResponseEntity getMonthDays(@RequestParam Map map, HttpSession session){
        String month = map.get("month").toString();
        String year = map.get("year").toString();
        System.out.println(map);
        Map response = new HashMap();
        Iterable<BookSlot> bookSlots = bookSlotRepository.findAll();
        if (month.length() == 1){
            month = "0"+month;
        }
        Map datesMap = new HashMap<>();
        for (BookSlot bookSlot: bookSlots){
            String date= bookSlot.getGameDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println(date);
            if (!datesMap.containsKey(date ) && date.split("-")[1].equals(month) && date.split("-")[2].equals(year) ){
                int Size = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "pending").size();
                int Sizeaccpt = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "accepted").size();
                int SizeReject = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "reject").size();

                String statusColor= "white";
                if(Size != 0){
                    statusColor="yellow";
                }else if (Sizeaccpt != 0){
                    statusColor="green";
                }else if (SizeReject != 0){
                    statusColor="red";
                }
                datesMap.putIfAbsent(Integer.parseInt(date.split("-")[0]),new String[]{String.valueOf(Size), statusColor} );
            }
        }

        response.put("datesMap", datesMap);
        response.put("defaultColor", "white");

        System.out.println("1234 "+datesMap);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getSlots")
    public ResponseEntity getSlots2(@RequestParam(name = "date", defaultValue = "") String date, HttpSession session){
        System.out.println(date);
        Map response = new HashMap();
        if (date.equals("")){
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );
        SpecialDates specialDate = specialDatesRepository.findByDate(date);
        String dayType = "REGL";
        System.out.println(dateModified.getDayOfWeek());
        if (!(specialDate == null)){
            dayType = specialDate.getDayType();
        } else if (dateModified.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||dateModified.getDayOfWeek().equals(DayOfWeek.SUNDAY) ) {
            dayType = "WEND";
        }
        List<Slot> slots = slotRepository.findByDayTypeOrderByStartHourAsc(dayType);
        List<Map> slotsList = new ArrayList<>();
        for(Slot slot: slots){
            Map slotMap = slot.getDict();
            int sizeList = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "pending", slot.getSlotCode()).size();
            int sizeListAcc = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "accepted", slot.getSlotCode()).size();
            int sizeListRej = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "rejected", slot.getSlotCode()).size();
            String statusColor= "white";
            if(sizeList != 0){
                statusColor="yellow";
            }else if(sizeListAcc != 0){
                statusColor="green";
            }
            else if(sizeListRej != 0){
                statusColor="red";
            }
            slotMap.put("statusColor", statusColor);
            slotMap.put("numberOfRequest",sizeList);

            slotsList.add(slotMap);
        }
        System.out.println(slotsList);
        List<BookSlot> bookSlotsList = bookSlotRepository.findByGameDate(dateModified);
        List<Map> bookSlotMapList = new ArrayList<>();

        for (BookSlot bookSlot: bookSlotsList){

            Map bookSlotMap = bookSlot.toMap();
            bookSlotMap.put("user", userRepository.findByMobileNo(bookSlot.getBookedBy()).getDict());
            bookSlotMap.put("slotId", slotRepository.findBySlotCode(bookSlot.getSlotCode()).getId());
            bookSlotMapList.add(bookSlotMap);
        }

        response.putIfAbsent("bookSlotMap", bookSlotMapList);
        response.putIfAbsent("slots", slotsList);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getRequests")
    public ResponseEntity getRequests(@RequestParam(name = "date", defaultValue = "") String date,@RequestParam(name = "slotCode") String slotCode, HttpSession session){
        System.out.println(date);
        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );

        Map response = new HashMap();
        List<BookSlot> bookSlotsList = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "pending", slotCode);
        List<Map> bookSlotMapList = new ArrayList<>();

        for (BookSlot bookSlot: bookSlotsList){

            Map bookSlotMap = bookSlot.toMap();
            bookSlotMap.put("user", userRepository.findByMobileNo(bookSlot.getBookedBy()));
            bookSlotMapList.add(bookSlotMap);
        }
        response.putIfAbsent("bookSlotMap", bookSlotMapList);
        return ResponseEntity.ok(response);
    }






}
//    private String branch;
//    private String courtCode;
//    private LocalDate bookingDate;
//    private LocalDate gameDate;
//    private String slotCode;
//    private String bookedBy;
//    private String remarksByUser;
//    private LocalDateTime bookTime;
//    private String confirmStatus;
//    private String remarksByAdmin;
//    private String approvedBy;
//    private LocalDateTime approvedTime;
//    private String bookingNo;
//    private String refNo;