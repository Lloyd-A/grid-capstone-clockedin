package com.clockedIn.userservice;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@ToString(callSuper = false)
public class TimeOffRequest extends AbstractRequest{
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    @Override
    public void submit() {
        setRequestStatus(RequestStatus.PENDING);
    }

    @Override
    public void deny() {
        setRequestStatus(RequestStatus.DENIED);
    }

    @Override
    public void approve() {
        setRequestStatus(RequestStatus.APPROVED);
    }

    @Override
    public RequestStatus getStatus() {
        return getRequestStatus();
    }
}
