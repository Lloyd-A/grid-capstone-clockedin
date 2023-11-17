package com.clockedIn.userservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractRequest implements Request{
    private UUID requestID;
    private User requester;
    private String reason;
    private List<User> requestApprover;
    private RequestStatus requestStatus;
    private LocalDateTime timeCreated;
    private LocalDateTime timeResolved;

}
