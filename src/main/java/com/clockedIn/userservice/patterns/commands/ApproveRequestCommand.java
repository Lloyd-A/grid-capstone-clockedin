package com.clockedIn.userservice.patterns.commands;

import com.clockedIn.userservice.AbstractRequest;


public class ApproveRequestCommand implements RequestCommand {
    @Override
    public AbstractRequest execute(AbstractRequest request) {
        request.approve();
        return request;
    }
}
