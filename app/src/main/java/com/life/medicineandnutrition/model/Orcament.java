package com.life.medicineandnutrition.model;

public class Orcament {
    private String uuid;
    private String dosaged3;
    private String dosagek2;
    private String dosagemg;
    private String name;
    private String email;
    private String contact;
    private String pharmacyname;
    private String dateorcament;

    private String period;
    private String dosaged3manteining;
    private String dosagek2manteining;

    private String timeinit;
    private String timefinal;
    private String weight;

    public Orcament() {

    }

    public Orcament(String uuid, String dosaged3, String dosagek2, String dosagemg,
                    String name, String email, String contact, String pharmacyname,
                    String dateorcament, String period, String dosaged3manteining,
                    String dosagek2manteining, String timeinit, String timefinal, String weight) {
        this.uuid = uuid;
        this.dosaged3 = dosaged3;
        this.dosagek2 = dosagek2;
        this.dosagemg = dosagemg;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.pharmacyname = pharmacyname;
        this.dateorcament = dateorcament;
        this.period = period;
        this.dosaged3manteining = dosaged3manteining;
        this.dosagek2manteining = dosagek2manteining;

        this.timeinit = timeinit;
        this.timefinal = timefinal;
        this.weight = weight;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDosaged3() {
        return dosaged3;
    }

    public String getDosagek2() {
        return dosagek2;
    }

    public String getDosagemg() {
        return dosagemg;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getPharmacyname() {
        return pharmacyname;
    }

    public String getDateorcament() {
        return dateorcament;
    }

    public String getPeriod() {
        return period;
    }

    public String getDosaged3manteining() {
        return dosaged3manteining;
    }

    public String getDosagek2manteining() {
        return dosagek2manteining;
    }

    public String getTimeinit() {
        return timeinit;
    }

    public String getTimefinal() {
        return timefinal;
    }

    public String getWeight() {
        return weight;
    }
}
