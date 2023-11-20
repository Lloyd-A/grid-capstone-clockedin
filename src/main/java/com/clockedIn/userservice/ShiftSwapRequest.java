package com.clockedIn.userservice;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.clockedIn.shiftservice.model.Shift;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Component
//@ToString(callSuper = false)
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

    public String toString() {
        return requestedShift.getShiftId() +" ////" + proposedShift.getShiftId();
    }

}
