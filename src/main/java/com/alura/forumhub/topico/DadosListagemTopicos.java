package com.alura.forumhub.topico;

public record DadosListagemTopicos(Long id,
                                   String titulo,
                                   String mensagem,
                                   String autor,
                                   String curso,
                                   Boolean estadoDoTopico,
                                   String dataCriacao) {
    public DadosListagemTopicos(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getAutor(),
                topico.getCurso(),
                topico.getEstadoDoTopico(),
                topico.getDataCriacao().toString()
        );
    }
}
