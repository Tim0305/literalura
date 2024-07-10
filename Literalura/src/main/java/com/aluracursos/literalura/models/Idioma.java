package com.aluracursos.literalura.models;

public enum Idioma {
    ENGLISH ("en"),
    ESPANOL ("es"),
    FRANCES ("fr"),
    NULL ("NULL");

    private String idiomaLibro;

    Idioma (String idioma) {
        this.idiomaLibro = idioma;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaLibro.equalsIgnoreCase(text))
                return idioma;
        }
        return Idioma.NULL;
    }

    public static void mostrarIdiomas() {
        for (Idioma idioma : Idioma.values()) {
            System.out.println("[" + idioma.idiomaLibro + "] " + idioma.name());
        }
    }
}
