package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.MidiaCreateDTO;
import br.com._hire.projeto_web.dto.MidiaResponseDTO;
import br.com._hire.projeto_web.entity.Midia;
import br.com._hire.projeto_web.enums.TipoMidia;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.MensagemRepository;
import br.com._hire.projeto_web.repository.MidiaRepository;
import br.com._hire.projeto_web.repository.PerfilPrestadorRepository;
import br.com._hire.projeto_web.repository.SolicitacaoOrcamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MidiaService {

    private final MidiaRepository midiaRepository;
    private final PerfilPrestadorRepository perfilPrestadorRepository;
    private final SolicitacaoOrcamentoRepository orcamentoRepository;
    private final MensagemRepository mensagemRepository;

    @Transactional
    public MidiaResponseDTO criar(MidiaCreateDTO dto) {
        Midia midia = new Midia();
        midia.setTipoMidia(dto.tipoMidia());
        midia.setUrlArquivo(dto.urlArquivo().trim());

        // o DTO já garante que só o dono compatível com o tipo foi informado
        switch (dto.tipoMidia()) {
            case PORTFOLIO, ANTECEDENTES -> midia.setPerfilPrestador(
                    perfilPrestadorRepository.findById(dto.perfilPrestadorId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException(
                                    "Prestador não encontrado: " + dto.perfilPrestadorId())));
            case ANEXO_ORCAMENTO -> midia.setOrcamento(
                    orcamentoRepository.findById(dto.orcamentoId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException(
                                    "Orçamento não encontrado: " + dto.orcamentoId())));
            case ANEXO_CHAT -> midia.setMensagem(
                    mensagemRepository.findById(dto.mensagemId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException(
                                    "Mensagem não encontrada: " + dto.mensagemId())));
        }

        return MidiaResponseDTO.de(midiaRepository.save(midia));
    }

    @Transactional(readOnly = true)
    public MidiaResponseDTO buscarPorId(UUID id) {
        return MidiaResponseDTO.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<MidiaResponseDTO> listarPorPrestador(UUID perfilPrestadorId, TipoMidia tipo) {
        if (!perfilPrestadorRepository.existsById(perfilPrestadorId)) {
            throw new RecursoNaoEncontradoException("Prestador não encontrado: " + perfilPrestadorId);
        }
        List<Midia> midias = tipo == null
                ? midiaRepository.findByPerfilPrestadorIdOrderByCriadoEmAsc(perfilPrestadorId)
                : midiaRepository.findByPerfilPrestadorIdAndTipoMidiaOrderByCriadoEmAsc(perfilPrestadorId, tipo);
        return midias.stream().map(MidiaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<MidiaResponseDTO> listarPorOrcamento(UUID orcamentoId) {
        if (!orcamentoRepository.existsById(orcamentoId)) {
            throw new RecursoNaoEncontradoException("Orçamento não encontrado: " + orcamentoId);
        }
        return midiaRepository.findByOrcamentoIdOrderByCriadoEmAsc(orcamentoId).stream()
                .map(MidiaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<MidiaResponseDTO> listarPorMensagem(UUID mensagemId) {
        if (!mensagemRepository.existsById(mensagemId)) {
            throw new RecursoNaoEncontradoException("Mensagem não encontrada: " + mensagemId);
        }
        return midiaRepository.findByMensagemIdOrderByCriadoEmAsc(mensagemId).stream()
                .map(MidiaResponseDTO::de).toList();
    }

    @Transactional
    public void deletar(UUID id) {
        midiaRepository.delete(buscarEntidade(id));
    }

    Midia buscarEntidade(UUID id) {
        return midiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mídia não encontrada: " + id));
    }
}
