package models;

import java.util.List;

public class Merchants {
    private List<Merchant> merchants;
    private double dataBaseVersion;

    public double getDataBaseVersion() {
        return dataBaseVersion;
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public static class Merchant {
        private Integer id;
        private String name;
        private String city;

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
        public String getCity() {
            return city;
        }

    }
}
