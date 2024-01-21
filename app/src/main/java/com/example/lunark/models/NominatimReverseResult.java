package com.example.lunark.models;

public class NominatimReverseResult {

    NominatimAddress address;

    public NominatimReverseResult() {}

    public String getStreet() {
        String street = address.road != null ? address.road : "Unnamed street";
        String number = address.house_number != null ? address.house_number : "N/A";
        return street + " " + number;
    }

    public String getCity() {
        if (address.city != null) {
            return address.city;
        } else if (address.town != null) {
            return address.town;
        } else if (address.city_district != null) {
            return address.city_district;
        } else {
            return "";
        }
    }

    public String getCountry() {
        return address.country;
    }

    public static class NominatimAddress {
        private String house_number;
        private String road;
        private String city;
        private String town;
        private String city_district;
        private String country;

        public NominatimAddress() {}

        public String getHouse_number() {
            return house_number;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
        }

        public String getRoad() {
            return road;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getCity_district() {
            return city_district;
        }

        public void setCity_district(String city_district) {
            this.city_district = city_district;
        }
    }
}
