package com.clockedIn.shiftservice.services;

import com.clockedIn.shiftservice.Course;
import com.clockedIn.shiftservice.Shift;
import com.clockedIn.shiftservice.repositories.ShiftRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShiftCollectionService implements ShiftRepository<Shift, UUID> {
    private final Map<UUID, Shift> shiftHashMap;

    public ShiftCollectionService(Map<UUID, Shift> shiftHashMap) {
        this.shiftHashMap = shiftHashMap;
    }

    @Override
    public Shift save(Shift shift) {
        shiftHashMap.put(shift.getShiftId(), shift);
        return shift;
    }

    @Override
    public Optional<Shift> findById(UUID shiftId) {
        return Optional.of(shiftHashMap.get(shiftId));
    }

    @Override
    public Iterable<Shift> findAll() {
        return shiftHashMap.values();
    }

    @Override
    public Iterable<Shift> findShiftsByCourse(Course course) {
        return shiftHashMap.values().stream()
                .filter(shift -> course.equals(shift.getCourseEnum()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID shiftId) {
        shiftHashMap.remove(shiftId);
    }

}
