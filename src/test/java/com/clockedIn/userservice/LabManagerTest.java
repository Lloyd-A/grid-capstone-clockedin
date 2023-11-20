package com.clockedIn.userservice;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LabManagerTest {

    @Test
    void placeHolder() {
        System.out.println("help");
    }
//    LabManager testLabManager;
//    EmailNotificationService emailNotificationService = mock(EmailNotificationService.class);
//    List<AbstractRequest> requestList;
//
//    @BeforeEach
//    void setUp() {
//        AbstractRequest timeOffPending2;
//        AbstractRequest timeOffPending1;
//
//        timeOffPending1 = TimeOffRequest.builder()
//                .requestID(UUID.randomUUID())
//                .build();
//        timeOffPending2 = TimeOffRequest.builder()
//                .requestID(UUID.randomUUID())
//                .build();
//
//        requestList = new ArrayList<>();
//
//        timeOffPending1.submit();
//        timeOffPending2.submit();
//
//        requestList.add(timeOffPending1);
//        requestList.add(timeOffPending2);
//
//        HashMap<UUID, AbstractRequest> otherPendingMap = new HashMap<>();
//        HashMap<UUID, AbstractRequest> otherApprovedMap = new HashMap<>();
//        HashMap<UUID, AbstractRequest> otherDeniedMap = new HashMap<>();
//
//        HashMap<RequestStatus, HashMap<UUID, AbstractRequest>> otherRequests = new HashMap<>();
//
//
//        otherRequests.put(RequestStatus.PENDING, otherPendingMap);
//        otherRequests.put(RequestStatus.APPROVED, otherApprovedMap);
//        otherRequests.put(RequestStatus.DENIED, otherDeniedMap);
//
//        testLabManager = LabManager.builder()
//                .userID(UUID.randomUUID())
//                .otherRequests(otherRequests)
//                .notificationService(emailNotificationService)
//                .denyCommand(new DenyRequestCommand())
//                .approveCommand(new ApproveRequestCommand())
//                .build();
//    }
//    @Test
//    void updateRequestList() {
//        //given
//
//        //when
//        for (AbstractRequest request: requestList) {
//            testLabManager.updateRequestList(request);
//        }
//
//
//        //then
//        for (AbstractRequest request: requestList) {
//            assertEquals(testLabManager
//                    .getOtherRequests()
//                    .get(RequestStatus.PENDING)
//                    .get(request.getRequestID()), request);
//        }
//    }
//
//    @Test
//    void approvePendingRequestsTest() {
//        //given
//
//        //when
//        for (AbstractRequest request : requestList) {
//            doNothing().when(emailNotificationService).addObserver(request.getRequester());
//            doNothing().when(emailNotificationService).send(request);
//            doNothing().when(emailNotificationService).clearObservers();
//
//            testLabManager.approveRequest(request);
//        }
//
//        //then
//        for (AbstractRequest request : requestList) {
//            assertEquals(RequestStatus.APPROVED, request.getRequestStatus());
//        }
//
//    }
//
//    @Test
//    void denyPendingRequestsTest() {
//        //given
//
//        //when
//        for (AbstractRequest request : requestList) {
//            doNothing().when(emailNotificationService).addObserver(request.getRequester());
//            doNothing().when(emailNotificationService).send(request);
//            doNothing().when(emailNotificationService).clearObservers();
//
//            testLabManager.denyRequest(request);
//        }
//
//        //then
//        for (AbstractRequest request : requestList) {
//            assertEquals(RequestStatus.DENIED, request.getRequestStatus());
//        }
//    }
//
//

}