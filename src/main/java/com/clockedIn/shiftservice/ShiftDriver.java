package com.clockedIn.shiftservice;

import com.clockedIn.shiftservice.model.Course;
import com.clockedIn.shiftservice.model.Shift;
import com.clockedIn.shiftservice.services.collection.ShiftCollectionService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShiftDriver {
    public static void main(String[] args) {
        Map<UUID, Shift> shiftHashMap = new HashMap<>();
        ShiftCollectionService shiftCollectionService = new ShiftCollectionService(shiftHashMap);

        //Creating shifts to be used
        Shift shift1 = Shift.builder().shiftId(UUID.randomUUID()).courseEnum(Course.COMP1126).build();
        Shift shift2 = Shift.builder().shiftId(UUID.randomUUID()).courseEnum(Course.COMP1126).build();
        Shift shiftUpdate = Shift.builder().shiftId(UUID.randomUUID()).courseEnum(Course.COMP3161).build();

        //Save two shifts
        Shift firstSavedShift = shiftCollectionService.save(shift1);
        Shift secondSavedShift = shiftCollectionService.save(shift2);

        //Find Shift By ID
        Shift shiftFoundById = shiftCollectionService.findById(shift1.getShiftId()).get();

        //Find Shift By Course
        Iterable<Shift> shiftsFoundByCourse = shiftCollectionService.findShiftsByCourse(Course.COMP1126);

        //Find All Courses
        Iterable<Shift> allShifts = shiftCollectionService.findAll();

        //Output the Results
        System.out.println("First shift that was saved: " + firstSavedShift.toString());
        System.out.println("Second shift that was saved: " + secondSavedShift.toString());
        System.out.println("Shift with ID number 1: " + shiftFoundById);
        System.out.println("Shifts for the Course COMP1126: " + shiftsFoundByCourse.toString());
        System.out.println("All shifts: " + allShifts);

        //Update a Shift
        Shift updatedShift = shiftCollectionService.save(shiftUpdate);

        //Delete a Shift
        shiftCollectionService.delete(shift1.getShiftId());

        //Output Results
        System.out.println("All shifts after deleting Shift with ID 1: " + allShifts);
        System.out.println("Updated Shift: " + updatedShift.toString());

        //Find All Shifts With New Update to Shift
        Iterable<Shift> allShiftsWithNewUpdate = shiftCollectionService.findAll();
        System.out.println("All shifts with newly updated shift: " + allShiftsWithNewUpdate);

    }

}
