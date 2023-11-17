package com.clockedIn.userservice;


import com.clockedIn.notificationservice.EmailNotificationService;
import com.clockedIn.shiftservice.Shift;
import com.clockedIn.userservice.patterns.commands.ApproveRequestCommand;
import com.clockedIn.userservice.patterns.commands.DenyRequestCommand;
import com.clockedIn.userservice.services.LabTechService;
import com.clockedIn.userservice.services.LabTechServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class LabTechTest {

    LabTech testLabTech;
    EmailNotificationService emailNotificationService = Mockito.mock(EmailNotificationService.class);

    LabTechService labTechService;
    AbstractRequest shiftSwapApproved;
    AbstractRequest shiftSwapDenied;
    AbstractRequest shiftSwapPending;
    @BeforeEach
    void setUp() {
        shiftSwapApproved = ShiftSwapRequest.builder()
                .requestID(UUID.randomUUID())
                .build();

        shiftSwapDenied = ShiftSwapRequest.builder()
                .requestID(UUID.randomUUID())
                .build();;
        shiftSwapPending = ShiftSwapRequest.builder()
                .requestID(UUID.randomUUID())
                .build();;
        HashMap<UUID, AbstractRequest> myPendingMap = new HashMap<>();
        HashMap<UUID, AbstractRequest> myApprovedMap = new HashMap<>();
        HashMap<UUID, AbstractRequest> myDeniedMap = new HashMap<>();

        HashMap<UUID, AbstractRequest> otherPendingMap = new HashMap<>();
        HashMap<UUID, AbstractRequest> otherApprovedMap = new HashMap<>();
        HashMap<UUID, AbstractRequest> otherDeniedMap = new HashMap<>();



        shiftSwapApproved.approve();
        shiftSwapDenied.deny();
        shiftSwapPending.submit();

        HashMap<RequestStatus, HashMap<UUID, AbstractRequest>> otherRequests = new HashMap<>();
        HashMap<RequestStatus, HashMap<UUID, AbstractRequest>> myRequests = new HashMap<>();

        otherRequests.put(RequestStatus.PENDING, otherPendingMap);
        otherRequests.put(RequestStatus.APPROVED, otherApprovedMap);
        otherRequests.put(RequestStatus.DENIED, otherDeniedMap);

        myRequests.put(RequestStatus.PENDING, myPendingMap);
        myRequests.put(RequestStatus.APPROVED,myApprovedMap);
        myRequests.put(RequestStatus.DENIED, myDeniedMap);


        testLabTech = LabTech.builder()
                .userID(UUID.randomUUID())
                .otherRequests(otherRequests)
                .myRequests(myRequests)
                .notificationService(emailNotificationService)
                .denyCommand(new DenyRequestCommand())
                .approveCommand(new ApproveRequestCommand())
                .build();

        labTechService = new LabTechServiceImpl(emailNotificationService);

    }
    @Test
    void updateRequestList() {
        //given

        //when
        testLabTech
                .getMyRequests()
                .get(RequestStatus.PENDING)
                .put(shiftSwapApproved.getRequestID(), shiftSwapApproved);
        testLabTech
                .getMyRequests()
                .get(RequestStatus.PENDING)
                .put(shiftSwapDenied.getRequestID(), shiftSwapDenied);

        testLabTech.updateRequestList(shiftSwapApproved);
        testLabTech.updateRequestList(shiftSwapDenied);
        testLabTech.updateRequestList(shiftSwapPending);


        //then
        assertEquals(testLabTech
                .getMyRequests()
                .get(RequestStatus.DENIED)
                .get(shiftSwapDenied.getRequestID()), shiftSwapDenied);

        assertEquals(testLabTech
                .getMyRequests()
                .get(RequestStatus.APPROVED)
                .get(shiftSwapApproved.getRequestID()), shiftSwapApproved);

        assertEquals(testLabTech
                .getOtherRequests()
                .get(RequestStatus.PENDING)
                .get(shiftSwapPending.getRequestID()), shiftSwapPending);


        assertEquals(testLabTech
                .getMyRequests()
                .get(RequestStatus.PENDING), new HashMap<UUID, AbstractRequest>());

    }

    @Test
    void makeShiftChangeRequest() {

        //given
        AbstractRequest labTechRequest;

        //when
        labTechRequest = labTechService.makeShiftChangeRequest(Shift.builder()
                .shiftId(UUID.randomUUID())
                .build(), Shift.builder()
                .shiftId(UUID.randomUUID())
                .labTechs(new HashMap<>())
                .build(), "I am feeling sick", testLabTech);
        doNothing().when(emailNotificationService).addObserver(labTechRequest.getRequester());
        doNothing().when(emailNotificationService).send(labTechRequest);
        doNothing().when(emailNotificationService).clearObservers();


        //then
        assertEquals(testLabTech
                .getMyRequests()
                .get(RequestStatus.PENDING)
                .get(labTechRequest.getRequestID()), labTechRequest);
    }

    @Test
    void makeTimeOffRequest() {
        //given
        AbstractRequest labTechRequest;

        //when
        labTechRequest = labTechService.makeTimeOffRequest(new ArrayList<User>(), "Need a break from work.", testLabTech);
        doNothing().when(emailNotificationService).addObserver(labTechRequest.getRequester());
        doNothing().when(emailNotificationService).send(labTechRequest);
        doNothing().when(emailNotificationService).clearObservers();


        //then
        assertEquals(testLabTech
                .getMyRequests()
                .get(RequestStatus.PENDING)
                .get(labTechRequest.getRequestID()), labTechRequest);
    }

}