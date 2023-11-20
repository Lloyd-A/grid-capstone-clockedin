package com.clockedIn.userservice.services;

import com.clockedIn.notificationservice.NotificationService;
import com.clockedIn.userservice.AbstractRequest;
import com.clockedIn.userservice.RequestStatus;
import com.clockedIn.userservice.User;
import com.clockedIn.userservice.patterns.commands.RequestCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {
    private NotificationService notificationService;


    public UserServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }



    @Override
    public void processRequest(RequestCommand command, AbstractRequest request, User user) {
        RequestStatus prevRequestStatus = request.getRequestStatus();
        if (prevRequestStatus != RequestStatus.PENDING) {
            return;
        }
        command.execute(request);
        user.getOtherRequests().get(prevRequestStatus).remove(request.getRequestID());
        user.getOtherRequests().get(request.getStatus()).put(request.getRequestID(), request);
        getNotificationService().addObserver(request.getRequester());
        getNotificationService().send(request);
        getNotificationService().clearObservers();

    }

    @Override
    public void approveRequest(AbstractRequest request, User user) {
        processRequest(user.getApproveCommand(), request, user);

    }

    @Override
    public void denyRequest(AbstractRequest request, User user) {
        processRequest(user.getDenyCommand(), request, user);
    }



}
