package br.com.monetiza.pockafka.controller;

import br.com.monetiza.pockafka.payload.Usuario;
import br.com.monetiza.pockafka.producer.UsuarioProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioProducer usuarioProducer;

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario user = Usuario.builder()
                .usuario(usuario.getUsuario())
                .senha(usuario.getSenha())
                .id(usuario.getId())
                .build();
        usuarioProducer.sendMessage(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
