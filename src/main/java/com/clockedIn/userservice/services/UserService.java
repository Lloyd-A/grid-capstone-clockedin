package com.clockedIn.userservice.services;

import com.clockedIn.userservice.AbstractRequest;
import com.clockedIn.userservice.User;
import com.clockedIn.userservice.patterns.commands.RequestCommand;

public interface UserService {
    public void processRequest(RequestCommand command, AbstractRequest request, User user);

    public void approveRequest(AbstractRequest request, User user);

    public void denyRequest(AbstractRequest request, User user);

//    public void updateRequestList(AbstractRequest request, User user);


}
