package com.clockedIn.userservice.services;

import com.clockedIn.notificationservice.NotificationService;
import com.clockedIn.shiftservice.Shift;
import com.clockedIn.userservice.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Service
public class LabTechServiceImpl extends UserServiceImpl implements LabTechService {
    //LabTech labTech;
    //UserService userService;


    public LabTechServiceImpl(NotificationService notificationService) {
        super(notificationService);
    }

//    @Override
//    public void updateRequestList(AbstractRequest request, LabTech labTech) {
//        System.out.println(labTech.getUniversityID());
//        if (request.getStatus() == RequestStatus.PENDING) {
//            labTech.getOtherRequests().get(RequestStatus.PENDING).put(request.getRequestID(), request);
//        } else {
//            labTech.getMyRequests().get(RequestStatus.PENDING).remove(request.getRequestID());
//            labTech.getMyRequests().get(request.getStatus()).put(request.getRequestID(), request);
//        }
//    }

    public AbstractRequest makeShiftChangeRequest(Shift shiftOffered, Shift shiftWanted, String reason, LabTech labTech) {
        AbstractRequest request = ShiftSwapRequest.builder()
                .requestID(UUID.randomUUID())
                .requester(labTech)
                .reason(reason)
                .timeCreated(LocalDateTime.now())
                .proposedShift(shiftOffered)
                .requestedShift(shiftWanted)
                .requestApprover(shiftWanted.getLabTechs().values().stream().toList())
                .build();
        return sendAbstractRequest(request,labTech);
    }

    public AbstractRequest makeTimeOffRequest(List<User> labManagers, String reason, LabTech labTech) {
        AbstractRequest request = TimeOffRequest.builder()
                .requestID(UUID.randomUUID())
                .requester(labTech)
                .reason(reason)
                .timeCreated(LocalDateTime.now())
                .requestApprover(labManagers)
                .build();
        return sendAbstractRequest(request, labTech);
    }

    @NotNull
    public AbstractRequest sendAbstractRequest(AbstractRequest request, LabTech labTech) {
        request.submit();
        for(User observer : request.getRequestApprover()) {
            getNotificationService().addObserver(observer);
        }

        getNotificationService().send(request);
        getNotificationService().clearObservers();
        labTech.getMyRequests().get(request.getStatus()).put(request.getRequestID(), request);
        return request;
    }







}
