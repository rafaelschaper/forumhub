package com.alura.forumhub.topico;

import jakarta.validation.constraints.NotBlank;

public record DadosCadstroTopico(@NotBlank String titulo,
                                 @NotBlank String mensagem,
                                 @NotBlank String curso) {
}
