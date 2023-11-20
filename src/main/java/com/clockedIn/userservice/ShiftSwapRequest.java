package com.clockedIn.userservice;


import com.clockedIn.shiftservice.model.Shift;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@ToString(callSuper = true)
public class ShiftSwapRequest extends AbstractRequest{
    Shift requestedShift;
    Shift proposedShift;


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
