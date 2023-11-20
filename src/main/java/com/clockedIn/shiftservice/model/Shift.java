package com.clockedIn.shiftservice.model;

import com.clockedIn.userservice.patterns.observers.Observer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {
    private UUID shiftId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Location location;
    private Enum<Course> courseEnum;
    private Map<UUID, Observer> labTechs;



}
