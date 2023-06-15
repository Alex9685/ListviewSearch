package com.example.listviewsearch;

public class AlumnoData {
    private String imageId;
    private String nombreCompleto;
    private String matricula;

    public AlumnoData(String imageId, String nombreCompleto, String matricula) {
        this.imageId = imageId;
        this.nombreCompleto = nombreCompleto;
        this.matricula = matricula;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
