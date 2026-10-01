package co.edu.uptc.servicio_web_calculadora.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RespuestaWrapper<T> {

    private String contenedorId;
    private T data;

    public RespuestaWrapper(String contenedorId, T data) {
        this.contenedorId = contenedorId;
        this.data = data;
    }

}