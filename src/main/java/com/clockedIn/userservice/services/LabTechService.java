package com.clockedIn.userservice.services;

import com.clockedIn.shiftservice.Shift;
import com.clockedIn.userservice.AbstractRequest;
import com.clockedIn.userservice.LabTech;
import com.clockedIn.userservice.User;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public interface LabTechService {

//    public void updateRequestList(AbstractRequest request, LabTech labTech);

    public AbstractRequest makeShiftChangeRequest(Shift shiftOffered, Shift shiftWanted, String reason, LabTech labTech);

    public AbstractRequest makeTimeOffRequest(List<User> labManagers, String reason, LabTech labTech);

    @NotNull
    public AbstractRequest sendAbstractRequest(AbstractRequest request, LabTech labTech);


}
