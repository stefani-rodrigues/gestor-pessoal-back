package com.senac.aulaFull.domin.interfaces;

import com.senac.aulaFull.domin.enums.TipoTransacao;

public interface SomaPorTipoResponse {

    TipoTransacao getTipo();
    Double getTotal();
}
