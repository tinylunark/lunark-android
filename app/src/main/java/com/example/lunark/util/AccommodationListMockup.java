package com.example.lunark.util;

import com.example.lunark.R;
import com.example.lunark.models.Accommodation;

import java.util.ArrayList;
import java.util.List;

public class AccommodationListMockup {
    public static List<Accommodation> getAccommodations() {
        List<Accommodation> accommodations = new ArrayList<>();
        Accommodation a1 = new Accommodation("Test", "Lorem ipsum dolor sit amet.", "Belgrade", 100.0, R.drawable.beach_apartment);
        Accommodation a2 = new Accommodation("Test2", "Lorem ipsum dolor sit amet.", "Novi Sad", 150.0, R.drawable.interior_one);
        Accommodation a3 = new Accommodation("blabla", "Bkjflasjkflkdsajflkds", "Subotica", 200.0, R.drawable.italy_apartment);
        Accommodation a4 = new Accommodation("Test4", "Bkjflasjkflkdsajflkds", "Subotica", 100.0, R.drawable.minimalist_apartment);

        accommodations.add(a1);
        accommodations.add(a2);
        accommodations.add(a3);
        accommodations.add(a4);

        return accommodations;
    }
}
