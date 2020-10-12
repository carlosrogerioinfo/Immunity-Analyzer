package com.life.medicineandnutrition.model;

public class Activations {
    private String uuid;
    private String documentid;
    private Boolean activated;

    public Activations() {

    }

    public Activations(String uuid, String documentid, Boolean activated) {
        this.uuid = uuid;
        this.documentid = documentid;
        this.activated = activated;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDocumentid() {
        return documentid;
    }

    public Boolean getActivated() {
        return activated;
    }

}
