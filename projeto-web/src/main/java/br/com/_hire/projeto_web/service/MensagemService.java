package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.MensagemCreateDTO;
import br.com._hire.projeto_web.dto.MensagemResponseDTO;
import br.com._hire.projeto_web.entity.ChatConversa;
import br.com._hire.projeto_web.entity.Mensagem;
import br.com._hire.projeto_web.entity.Usuario;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.MensagemRepository;
import br.com._hire.projeto_web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ChatConversaService conversaService;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MensagemResponseDTO enviar(UUID conversaId, MensagemCreateDTO dto) {
        ChatConversa conversa = conversaService.buscarEntidade(conversaId);
        Usuario remetente = buscarUsuario(dto.remetenteId());
        exigirParticipante(conversa, remetente.getId());

        Mensagem mensagem = new Mensagem();
        mensagem.setConversa(conversa);
        mensagem.setRemetente(remetente);
        mensagem.setConteudoTexto(dto.conteudoTexto());
        return MensagemResponseDTO.de(mensagemRepository.save(mensagem));
    }

    @Transactional(readOnly = true)
    public List<MensagemResponseDTO> listar(UUID conversaId) {
        conversaService.buscarEntidade(conversaId);
        return mensagemRepository.findByConversaIdOrderByDataEnvioAsc(conversaId).stream()
                .map(MensagemResponseDTO::de).toList();
    }

    /** Marca como lidas as mensagens que o OUTRO participante mandou. Retorna quantas foram marcadas. */
    @Transactional
    public int marcarComoLidas(UUID conversaId, UUID leitorId) {
        ChatConversa conversa = conversaService.buscarEntidade(conversaId);
        exigirParticipante(conversa, leitorId);

        List<Mensagem> naoLidas = mensagemRepository
                .findByConversaIdAndRemetenteIdNotAndLidaEmIsNull(conversaId, leitorId);
        LocalDateTime agora = LocalDateTime.now();
        naoLidas.forEach(m -> m.setLidaEm(agora));
        mensagemRepository.saveAll(naoLidas);
        return naoLidas.size();
    }

    @Transactional(readOnly = true)
    public long contarNaoLidas(UUID conversaId, UUID usuarioId) {
        ChatConversa conversa = conversaService.buscarEntidade(conversaId);
        exigirParticipante(conversa, usuarioId);
        return mensagemRepository.countByConversaIdAndRemetenteIdNotAndLidaEmIsNull(conversaId, usuarioId);
    }

    private Usuario buscarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }

    private void exigirParticipante(ChatConversa conversa, UUID usuarioId) {
        UUID clienteId = conversa.getCliente().getId();
        UUID prestadorUsuarioId = conversa.getPerfilPrestador().getUsuario().getId();
        if (!usuarioId.equals(clienteId) && !usuarioId.equals(prestadorUsuarioId)) {
            throw new ConflitoException("Este usuário não participa da conversa");
        }
    }
}