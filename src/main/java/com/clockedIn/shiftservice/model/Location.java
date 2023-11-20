package com.clockedIn.shiftservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    private UUID locationId;
    private String roomName;
    private String virtualLink;
}
