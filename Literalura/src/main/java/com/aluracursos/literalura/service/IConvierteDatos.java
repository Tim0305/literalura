package com.aluracursos.literalura.service;

public interface IConvierteDatos {
    <T> T convierteDatos(String json, Class<T> clase);
}
