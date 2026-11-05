package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.AvaliacaoCreateDTO;
import br.com._hire.projeto_web.dto.AvaliacaoResponseDTO;
import br.com._hire.projeto_web.dto.MediaAvaliacaoDTO;
import br.com._hire.projeto_web.entity.Avaliacao;
import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import br.com._hire.projeto_web.enums.StatusOrcamento;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.AvaliacaoRepository;
import br.com._hire.projeto_web.repository.PerfilPrestadorRepository;
import br.com._hire.projeto_web.repository.SolicitacaoOrcamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final SolicitacaoOrcamentoRepository orcamentoRepository;
    private final PerfilPrestadorRepository perfilPrestadorRepository;

    @Transactional
    public AvaliacaoResponseDTO criar(AvaliacaoCreateDTO dto) {
        SolicitacaoOrcamento orcamento = orcamentoRepository.findById(dto.orcamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado: " + dto.orcamentoId()));

        if (orcamento.getStatusAtual() != StatusOrcamento.CONCLUIDO) {
            throw new ConflitoException("Só é possível avaliar orçamentos concluídos");
        }
        if (!orcamento.getCliente().getId().equals(dto.avaliadorId())) {
            throw new ConflitoException("Somente o cliente do orçamento pode avaliá-lo");
        }
        if (avaliacaoRepository.existsByOrcamentoId(orcamento.getId())) {
            throw new ConflitoException("Este orçamento já foi avaliado");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setOrcamento(orcamento);
        avaliacao.setAvaliador(orcamento.getCliente());
        avaliacao.setPerfilPrestador(orcamento.getPerfilPrestador());
        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());
        return AvaliacaoResponseDTO.de(avaliacaoRepository.save(avaliacao));
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorId(UUID id) {
        return avaliacaoRepository.findById(id)
                .map(AvaliacaoResponseDTO::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliação não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorPrestador(UUID perfilPrestadorId) {
        exigirPrestador(perfilPrestadorId);
        return avaliacaoRepository.findByPerfilPrestadorIdOrderByDataAvaliacaoDesc(perfilPrestadorId).stream()
                .map(AvaliacaoResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public MediaAvaliacaoDTO media(UUID perfilPrestadorId) {
        exigirPrestador(perfilPrestadorId);
        Double media = avaliacaoRepository.mediaPorPrestador(perfilPrestadorId);
        double arredondada = media == null ? 0.0 : Math.round(media * 10) / 10.0;
        return new MediaAvaliacaoDTO(perfilPrestadorId, arredondada,
                avaliacaoRepository.countByPerfilPrestadorId(perfilPrestadorId));
    }

    private void exigirPrestador(UUID perfilPrestadorId) {
        if (!perfilPrestadorRepository.existsById(perfilPrestadorId)) {
            throw new RecursoNaoEncontradoException("Prestador não encontrado: " + perfilPrestadorId);
        }
    }
}