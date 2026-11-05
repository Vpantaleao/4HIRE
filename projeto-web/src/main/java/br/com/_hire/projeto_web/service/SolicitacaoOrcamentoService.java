package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.HistoricoStatusResponseDTO;
import br.com._hire.projeto_web.dto.OrcamentoAgendamentoDTO;
import br.com._hire.projeto_web.dto.OrcamentoCreateDTO;
import br.com._hire.projeto_web.dto.OrcamentoResponseDTO;
import br.com._hire.projeto_web.dto.OrcamentoTransicaoDTO;
import br.com._hire.projeto_web.dto.OrcamentoValoresDTO;
import br.com._hire.projeto_web.entity.Categoria;
import br.com._hire.projeto_web.entity.ChatConversa;
import br.com._hire.projeto_web.entity.HistoricoStatus;
import br.com._hire.projeto_web.entity.PerfilPrestador;
import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import br.com._hire.projeto_web.entity.Usuario;
import br.com._hire.projeto_web.enums.StatusOrcamento;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.CategoriaRepository;
import br.com._hire.projeto_web.repository.ChatConversaRepository;
import br.com._hire.projeto_web.repository.HistoricoStatusRepository;
import br.com._hire.projeto_web.repository.PerfilPrestadorRepository;
import br.com._hire.projeto_web.repository.SolicitacaoOrcamentoRepository;
import br.com._hire.projeto_web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static br.com._hire.projeto_web.enums.StatusOrcamento.*;

@Service
@RequiredArgsConstructor
public class SolicitacaoOrcamentoService {

    private static final Map<StatusOrcamento, Set<StatusOrcamento>> TRANSICOES = Map.of(
            SOLICITADO, EnumSet.of(ORCADO, CANCELADO),
            ORCADO, EnumSet.of(ACEITO, CANCELADO),
            ACEITO, EnumSet.of(AGENDADO, CANCELADO),
            AGENDADO, EnumSet.of(EM_ANDAMENTO, CANCELADO),
            EM_ANDAMENTO, EnumSet.of(CONCLUIDO, CANCELADO),
            CONCLUIDO, EnumSet.noneOf(StatusOrcamento.class),
            CANCELADO, EnumSet.noneOf(StatusOrcamento.class)
    );

    private final SolicitacaoOrcamentoRepository orcamentoRepository;
    private final HistoricoStatusRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilPrestadorRepository perfilPrestadorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ChatConversaRepository conversaRepository;

    @Transactional
    public OrcamentoResponseDTO criar(OrcamentoCreateDTO dto) {
        Usuario cliente = usuarioRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + dto.clienteId()));
        PerfilPrestador perfil = perfilPrestadorRepository.findById(dto.perfilPrestadorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestador não encontrado: " + dto.perfilPrestadorId()));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + dto.categoriaId()));
        ChatConversa conversa = conversaRepository.findById(dto.conversaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conversa não encontrada: " + dto.conversaId()));

        if (!Boolean.TRUE.equals(perfil.getAtivo())) {
            throw new ConflitoException("O prestador está inativo");
        }
        if (perfil.getUsuario().getId().equals(cliente.getId())) {
            throw new ConflitoException("O prestador não pode solicitar orçamento para si mesmo");
        }
        if (!conversa.getCliente().getId().equals(cliente.getId())
                || !conversa.getPerfilPrestador().getId().equals(perfil.getId())) {
            throw new ConflitoException("A conversa não pertence a este cliente e prestador");
        }
        if (orcamentoRepository.existsByConversaId(conversa.getId())) {
            throw new ConflitoException("Já existe um orçamento para esta conversa");
        }

        SolicitacaoOrcamento orcamento = new SolicitacaoOrcamento();
        orcamento.setCliente(cliente);
        orcamento.setPerfilPrestador(perfil);
        orcamento.setCategoria(categoria);
        orcamento.setConversa(conversa);
        orcamento.setDescricaoServico(dto.descricaoServico());
        orcamento.setEnderecoTexto(dto.enderecoTexto());
        orcamento.setLatitudeServico(dto.latitudeServico());
        orcamento.setLongitudeServico(dto.longitudeServico());
        orcamento.setDataAgendamento(dto.dataAgendamento());
        orcamento.setTurnoPreferencia(dto.turnoPreferencia());
        orcamento = orcamentoRepository.save(orcamento);

        registrarHistorico(orcamento, null, SOLICITADO, cliente, "Orçamento solicitado");
        return OrcamentoResponseDTO.de(orcamento);
    }

    @Transactional(readOnly = true)
    public OrcamentoResponseDTO buscarPorId(UUID id) {
        return OrcamentoResponseDTO.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<OrcamentoResponseDTO> listarPorCliente(UUID clienteId) {
        return orcamentoRepository.findByClienteIdOrderByDataSolicitacaoDesc(clienteId).stream()
                .map(OrcamentoResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<OrcamentoResponseDTO> listarPorPrestador(UUID perfilPrestadorId) {
        return orcamentoRepository.findByPerfilPrestadorIdOrderByDataSolicitacaoDesc(perfilPrestadorId).stream()
                .map(OrcamentoResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatusResponseDTO> historico(UUID id) {
        buscarEntidade(id);
        return historicoRepository.findByOrcamentoIdOrderByDataAlteracaoAsc(id).stream()
                .map(HistoricoStatusResponseDTO::de).toList();
    }

    /** Prestador informa os valores: SOLICITADO -> ORCADO. */
    @Transactional
    public OrcamentoResponseDTO orcar(UUID id, OrcamentoValoresDTO dto) {
        SolicitacaoOrcamento orcamento = buscarEntidade(id);
        Usuario ator = exigirPrestador(orcamento, dto.usuarioId());
        validarTransicao(orcamento, ORCADO);

        orcamento.setValorVisitaTecnica(dto.valorVisitaTecnica());
        orcamento.setValorMaoDeObra(dto.valorMaoDeObra());
        orcamento.setTaxaPlataforma(dto.taxaPlataforma());
        return transicionar(orcamento, ORCADO, ator, "Valores informados pelo prestador");
    }

    /** Cliente aceita os valores: ORCADO -> ACEITO. */
    @Transactional
    public OrcamentoResponseDTO aceitar(UUID id, OrcamentoTransicaoDTO dto) {
        SolicitacaoOrcamento orcamento = buscarEntidade(id);
        Usuario ator = exigirCliente(orcamento, dto.usuarioId());
        return transicionar(orcamento, ACEITO, ator, dto.observacao());
    }

    /** Prestador define data e turno: ACEITO -> AGENDADO. */
    @Transactional
    public OrcamentoResponseDTO agendar(UUID id, OrcamentoAgendamentoDTO dto) {
        SolicitacaoOrcamento orcamento = buscarEntidade(id);
        Usuario ator = exigirPrestador(orcamento, dto.usuarioId());
        validarTransicao(orcamento, AGENDADO);

        orcamento.setDataAgendamento(dto.dataAgendamento());
        if (dto.turnoPreferencia() != null) {
            orcamento.setTurnoPreferencia(dto.turnoPreferencia());
        }
        return transicionar(orcamento, AGENDADO, ator, "Serviço agendado");
    }

    /** Prestador inicia o serviço: AGENDADO -> EM_ANDAMENTO. */
    @Transactional
    public OrcamentoResponseDTO iniciar(UUID id, OrcamentoTransicaoDTO dto) {
        SolicitacaoOrcamento orcamento = buscarEntidade(id);
        Usuario ator = exigirPrestador(orcamento, dto.usuarioId());
        return transicionar(orcamento, EM_ANDAMENTO, ator, dto.observacao());
    }

    /** Prestador conclui o serviço: EM_ANDAMENTO -> CONCLUIDO. */
    @Transactional
    public OrcamentoResponseDTO concluir(UUID id, OrcamentoTransicaoDTO dto) {
        SolicitacaoOrcamento orcamento = buscarEntidade(id);
        Usuario ator = exigirPrestador(orcamento, dto.usuarioId());
        return transicionar(orcamento, CONCLUIDO, ator, dto.observacao());
    }

    /** Cliente ou prestador cancelam enquanto o serviço não foi concluído. */
    @Transactional
    public OrcamentoResponseDTO cancelar(UUID id, OrcamentoTransicaoDTO dto) {
        SolicitacaoOrcamento orcamento = buscarEntidade(id);
        Usuario ator = exigirParticipante(orcamento, dto.usuarioId());
        return transicionar(orcamento, CANCELADO, ator, dto.observacao());
    }

    SolicitacaoOrcamento buscarEntidade(UUID id) {
        return orcamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado: " + id));
    }

    private void validarTransicao(SolicitacaoOrcamento orcamento, StatusOrcamento novo) {
        StatusOrcamento atual = orcamento.getStatusAtual();
        if (!TRANSICOES.get(atual).contains(novo)) {
            throw new ConflitoException("Transição inválida: " + atual + " -> " + novo);
        }
    }

    private OrcamentoResponseDTO transicionar(SolicitacaoOrcamento orcamento, StatusOrcamento novo,
                                              Usuario ator, String observacao) {
        validarTransicao(orcamento, novo);
        StatusOrcamento anterior = orcamento.getStatusAtual();
        orcamento.setStatusAtual(novo);
        orcamento = orcamentoRepository.save(orcamento);
        registrarHistorico(orcamento, anterior, novo, ator, observacao);
        return OrcamentoResponseDTO.de(orcamento);
    }

    private void registrarHistorico(SolicitacaoOrcamento orcamento, StatusOrcamento anterior,
                                    StatusOrcamento novo, Usuario ator, String observacao) {
        HistoricoStatus h = new HistoricoStatus();
        h.setOrcamento(orcamento);
        h.setStatusAnterior(anterior);
        h.setStatusNovo(novo);
        h.setAlteradoPor(ator);
        h.setObservacao(observacao);
        historicoRepository.save(h);
    }

    private Usuario exigirCliente(SolicitacaoOrcamento orcamento, UUID usuarioId) {
        if (!orcamento.getCliente().getId().equals(usuarioId)) {
            throw new ConflitoException("Somente o cliente do orçamento pode realizar esta ação");
        }
        return orcamento.getCliente();
    }

    private Usuario exigirPrestador(SolicitacaoOrcamento orcamento, UUID usuarioId) {
        Usuario prestador = orcamento.getPerfilPrestador().getUsuario();
        if (!prestador.getId().equals(usuarioId)) {
            throw new ConflitoException("Somente o prestador do orçamento pode realizar esta ação");
        }
        return prestador;
    }

    private Usuario exigirParticipante(SolicitacaoOrcamento orcamento, UUID usuarioId) {
        if (orcamento.getCliente().getId().equals(usuarioId)) {
            return orcamento.getCliente();
        }
        Usuario prestador = orcamento.getPerfilPrestador().getUsuario();
        if (prestador.getId().equals(usuarioId)) {
            return prestador;
        }
        throw new ConflitoException("Somente o cliente ou o prestador do orçamento podem realizar esta ação");
    }
}
