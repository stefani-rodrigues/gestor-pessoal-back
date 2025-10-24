package com.senac.aulaFull.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categoria")
@Tag(name = "Controlar as categorias",description = "Metodo responsavel por registrar as categorias")
public class CategoriaController {
}
