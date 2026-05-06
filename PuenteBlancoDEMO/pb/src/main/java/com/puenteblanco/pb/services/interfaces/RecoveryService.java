package com.puenteblanco.pb.services.interfaces;

public interface RecoveryService {
    void enviarCodigoRecuperacion(String correo);

    void cambiarContrasena(String correo, String codigo, String nuevaContrasena);
}
