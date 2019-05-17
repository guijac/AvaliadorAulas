package br.edu.pucsp.avaliador.controller;

public class Response<E extends Object> {
    private E element;
    private String message;

    private Response(E element, String message) {
        this.element = element;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public E getElement() {
        return element;
    }

    public static <E extends Object> Response<E> of(E element, String message) {
        return new Response<>(element, message);
    }
}
