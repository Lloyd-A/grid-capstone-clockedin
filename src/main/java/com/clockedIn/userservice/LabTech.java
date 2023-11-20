package com.clockedIn.userservice;

import com.clockedIn.shiftservice.model.Shift;
import com.clockedIn.userservice.patterns.observers.Observer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.*;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@ToString(callSuper = true)
public class LabTech extends User implements Observer {
    private Map<RequestStatus, HashMap<UUID, AbstractRequest>> myRequests;



    @Override
    public void updateRequestList(AbstractRequest request) {
        if (request.getStatus() == RequestStatus.PENDING) {
            otherRequests.get(RequestStatus.PENDING).put(request.getRequestID(), request);
        } else {
            myRequests.get(RequestStatus.PENDING).remove(request.getRequestID());
            myRequests.get(request.getStatus()).put(request.getRequestID(), request);
        }
    }

    public AbstractRequest makeShiftChangeRequest(Shift shiftOffered, Shift shiftWanted, String reason) {
        AbstractRequest request = ShiftSwapRequest.builder()
                .requestID(UUID.randomUUID())
                .requester(this)
                .reason(reason)
                .timeCreated(LocalDateTime.now())
                .proposedShift(shiftOffered)
                .requestedShift(shiftWanted)
                .requestApprover(shiftWanted.getLabTechs().values().stream().toList())
                .build();
        return sendAbstractRequest(request);
    }

    public AbstractRequest makeTimeOffRequest(List<Observer> labManagers, String reason) {
        AbstractRequest request = TimeOffRequest.builder()
                .requestID(UUID.randomUUID())
                .requester(this)
                .reason(reason)
                .timeCreated(LocalDateTime.now())
                .requestApprover(labManagers)
                .build();
        return sendAbstractRequest(request);
    }

    @NotNull
    private AbstractRequest sendAbstractRequest(AbstractRequest request) {
        request.submit();
        for(Observer observer : request.getRequestApprover()) {
            notificationService.addObserver(observer);
        }
        notificationService.send(request);
        notificationService.clearObservers();
        myRequests.get(request.getStatus()).put(request.getRequestID(), request);
        return request;
    }
}
