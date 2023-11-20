package com.clockedIn.userservice;

import com.clockedIn.notificationservice.EmailNotificationService;
import com.clockedIn.notificationservice.NotificationService;
import com.clockedIn.shiftservice.model.Shift;
import com.clockedIn.userservice.patterns.commands.ApproveRequestCommand;
import com.clockedIn.userservice.patterns.commands.DenyRequestCommand;
import com.clockedIn.userservice.services.LabTechServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainTwo {
    public static void main(String[] args) {

        LabTech labTech1 = LabTech.builder()
                .firstName("Sam")
                .lastName("Karry")
                .universityID(620134045L)
                .userRole(UserRole.LABTECH)
                .phoneNum("876-358-1838")
                .email("sammu55@gmai.com")
                .otherRequests(new HashMap<>(){{
                    put(RequestStatus.PENDING, new HashMap<>());
                    put(RequestStatus.APPROVED, new HashMap<>());
                    put(RequestStatus.DENIED, new HashMap<>());

                }})
                .approveCommand(new ApproveRequestCommand())
                .denyCommand(new DenyRequestCommand())
                .notificationService(new EmailNotificationService())
                .myRequests(new HashMap<>(){{
                    put(RequestStatus.PENDING, new HashMap<>());
                    put(RequestStatus.APPROVED, new HashMap<>());
                    put(RequestStatus.DENIED, new HashMap<>());

                }})
                .build();

        LabTech labTech2 = LabTech.builder()
                .firstName("Farrah")
                .lastName("Aman")
                .universityID(620134049L)
                .userRole(UserRole.LABTECH)
                .phoneNum("876-359-1838")
                .email("farrah55@gmai.com")
                .otherRequests(new HashMap<>(){{
                    put(RequestStatus.PENDING, new HashMap<>());
                    put(RequestStatus.APPROVED, new HashMap<>());
                    put(RequestStatus.DENIED, new HashMap<>());

                }})
                .approveCommand(new ApproveRequestCommand())
                .denyCommand(new DenyRequestCommand())
                .myRequests(new HashMap<>(){{
                    put(RequestStatus.PENDING, new HashMap<>());
                    put(RequestStatus.APPROVED, new HashMap<>());
                    put(RequestStatus.DENIED, new HashMap<>());

                }})
                .build();

        User labManager1 = LabManager.builder()
                .firstName("Bruce")
                .lastName("WhoFung")
                .universityID(620134066L)
                .userRole(UserRole.LABMANAGER)
                .phoneNum("876-666-9999")
                .email("bruceFung@gmai.com")
                .otherRequests(new HashMap<>(){{
                    put(RequestStatus.PENDING, new HashMap<>());
                    put(RequestStatus.APPROVED, new HashMap<>());
                    put(RequestStatus.DENIED, new HashMap<>());

                }})
                .approveCommand(new ApproveRequestCommand())
                .denyCommand(new DenyRequestCommand())
                .build();

        NotificationService notificationService = new EmailNotificationService();

        LabTechServiceImpl labTechService = new LabTechServiceImpl(notificationService);



        AbstractRequest shiftSwap = labTechService.makeShiftChangeRequest(Shift.builder()
                .shiftId(UUID.randomUUID())
                .labTechs(new HashMap<>(){{
                    put(labTech1.getUserID(), labTech1);
                }
                }).build(), Shift.builder()
                .shiftId(UUID.randomUUID())
                .labTechs(new HashMap<>(){{
                    put(labTech2.getUserID(), labTech2);
                }
                }).build(),"help me please", labTech1);

        AbstractRequest timeOff = labTechService.makeTimeOffRequest(new ArrayList<User>(){{
            add(labManager1);
        }}, "I work hard, everyday of life. I work till I ache my bones.", labTech1);

        labTechService.processRequest(new DenyRequestCommand(),shiftSwap, labTech2);
        labTechService.processRequest(new ApproveRequestCommand(), timeOff, labManager1);

        System.out.println("First Lab Tech");

        System.out.println(labTech1.getOtherRequests());
        System.out.println(labTech1.getMyRequests());

        System.out.println("Second Lab Tech");
        System.out.println(labTech2.getOtherRequests());
        System.out.println(labTech2.getMyRequests());

        System.out.println("First Lab Manager");
        System.out.println(labManager1.getOtherRequests());

    }
}
