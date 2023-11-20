package com.clockedIn.userservice;

import com.clockedIn.notificationservice.NotificationService;
import com.clockedIn.userservice.patterns.commands.RequestCommand;
import com.clockedIn.userservice.patterns.observers.Observer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Component
public class User implements Observer {
    protected UUID userID = UUID.randomUUID();
    protected long universityID;
    protected String firstName;
    protected String lastName;
    protected UserRole userRole;
    protected String phoneNum;
    protected String email;
    protected Map<RequestStatus, HashMap<UUID, AbstractRequest>> otherRequests;
    protected RequestCommand approveCommand;
    protected RequestCommand denyCommand;
    protected NotificationService notificationService;

    @Override
    public void updateRequestList(AbstractRequest request) {
        if (request.getStatus() == RequestStatus.PENDING) {
            otherRequests.get(RequestStatus.PENDING).put(request.getRequestID(), request);
        }

    }
}
