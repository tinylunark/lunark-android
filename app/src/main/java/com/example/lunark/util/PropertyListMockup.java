package com.example.lunark.util;

import com.example.lunark.R;
import com.example.lunark.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyListMockup {
    public static List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();
        Property p1 = new Property("Test", "Lorem ipsum dolor sit amet.", "Belgrade", 100.0, R.drawable.beach_apartment, 4.5);
        Property p2 = new Property("Test2", "Lorem ipsum dolor sit amet.", "Novi Sad", 150.0, R.drawable.interior_one, 5.0);
        Property p3 = new Property("blabla", "Bkjflasjkflkdsajflkds", "Subotica", 200.0, R.drawable.italy_apartment, 4.0);
        Property p4 = new Property("Test4", "Bkjflasjkflkdsajflkds", "Subotica", 100.0, R.drawable.minimalist_apartment, 5.0);

        properties.add(p1);
        properties.add(p2);
        properties.add(p3);
        properties.add(p4);

        return properties;
    }
}
