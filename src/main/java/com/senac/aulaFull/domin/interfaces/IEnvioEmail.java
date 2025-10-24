package com.senac.aulaFull.domin.interfaces;

public interface IEnvioEmail {

    void  enviarEmailSimples(String para, String assunto,String texto);
    void  enviarEmailComTemplate(String para, String assunto,String texto);
}
