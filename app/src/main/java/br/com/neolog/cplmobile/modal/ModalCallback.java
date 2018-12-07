package br.com.neolog.cplmobile.modal;

public interface ModalCallback
{
    ModalCallback EMPTY = () -> {
    };

    void call();
}
