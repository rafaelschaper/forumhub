package com.alura.forumhub.controller;

import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.domain.usuario.UsuarioRepository;
import com.alura.forumhub.topico.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("topicos")
public class TopicoController {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody DadosCadstroTopico dadosCadstroTopico) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario autor = (Usuario) usuarioRepository.findByLogin(userDetails.getUsername());
        topicoRepository.save(new Topico(dadosCadstroTopico, autor));
    }

    @GetMapping
    public Page<DadosListagemTopicos> listar(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return topicoRepository.findAll(pageable).map(DadosListagemTopicos::new);
    }

    @GetMapping("/{id}")
    public Topico detalhar(@PathVariable Long id) {
        return topicoRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizarTopico dadosAtualizarTopico) {
        var topico = topicoRepository.getReferenceById(dadosAtualizarTopico.id());
        topico.atualizar(dadosAtualizarTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Topico topico = topicoRepository.findById(id).orElseThrow();

        if (!topico.getAutor().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sem permissão para excluir este tópico");
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
