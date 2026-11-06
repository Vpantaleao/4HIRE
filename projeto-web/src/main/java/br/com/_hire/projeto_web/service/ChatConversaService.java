package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.ConversaCreateDTO;
import br.com._hire.projeto_web.dto.ConversaResponseDTO;
import br.com._hire.projeto_web.entity.ChatConversa;
import br.com._hire.projeto_web.entity.PerfilPrestador;
import br.com._hire.projeto_web.entity.Usuario;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.ChatConversaRepository;
import br.com._hire.projeto_web.repository.PerfilPrestadorRepository;
import br.com._hire.projeto_web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatConversaService {

    private final ChatConversaRepository conversaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilPrestadorRepository perfilPrestadorRepository;

    @Transactional
    public ConversaResponseDTO criar(ConversaCreateDTO dto) {
        Usuario cliente = usuarioRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + dto.clienteId()));
        PerfilPrestador perfil = perfilPrestadorRepository.findById(dto.perfilPrestadorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestador não encontrado: " + dto.perfilPrestadorId()));

        if (perfil.getUsuario().getId().equals(cliente.getId())) {
            throw new ConflitoException("O prestador não pode abrir conversa consigo mesmo");
        }
        if (conversaRepository.existsByClienteIdAndPerfilPrestadorId(cliente.getId(), perfil.getId())) {
            throw new ConflitoException("Já existe uma conversa entre este cliente e este prestador");
        }

        ChatConversa conversa = new ChatConversa();
        conversa.setCliente(cliente);
        conversa.setPerfilPrestador(perfil);
        return ConversaResponseDTO.de(conversaRepository.save(conversa));
    }

    @Transactional(readOnly = true)
    public ConversaResponseDTO buscarPorId(UUID id) {
        return ConversaResponseDTO.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<ConversaResponseDTO> listarPorCliente(UUID clienteId) {
        return conversaRepository.findByClienteId(clienteId).stream()
                .map(ConversaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<ConversaResponseDTO> listarPorPrestador(UUID perfilPrestadorId) {
        return conversaRepository.findByPerfilPrestadorId(perfilPrestadorId).stream()
                .map(ConversaResponseDTO::de).toList();
    }

    ChatConversa buscarEntidade(UUID id) {
        return conversaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conversa não encontrada: " + id));
    }
}