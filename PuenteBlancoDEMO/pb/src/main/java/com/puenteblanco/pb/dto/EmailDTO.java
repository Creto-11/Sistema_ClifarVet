package com.puenteblanco.pb.dto;

public class EmailDTO {
    private String correo;

    public EmailDTO() {
    }

    public EmailDTO(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
