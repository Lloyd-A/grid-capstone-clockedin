package com.clockedIn.userservice;

import com.clockedIn.userservice.patterns.observers.Observer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@ToString(callSuper = false)
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



}
