package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.PerfilPrestadorCreateDTO;
import br.com._hire.projeto_web.dto.PerfilPrestadorResponseDTO;
import br.com._hire.projeto_web.dto.PerfilPrestadorUpdateDTO;
import br.com._hire.projeto_web.dto.PerfilPrestadorVerificacaoDTO;
import br.com._hire.projeto_web.entity.PerfilPrestador;
import br.com._hire.projeto_web.entity.Usuario;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.PerfilPrestadorRepository;
import br.com._hire.projeto_web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilPrestadorService {

    private final PerfilPrestadorRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PerfilPrestadorResponseDTO criar(PerfilPrestadorCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + dto.usuarioId()));

        if (perfilRepository.existsByUsuarioId(usuario.getId())) {
            throw new ConflitoException("Este usuário já possui um perfil de prestador");
        }

        PerfilPrestador perfil = new PerfilPrestador();
        perfil.setUsuario(usuario);
        perfil.setBiografia(dto.biografia());
        perfil.setRaioAtendimentoKm(dto.raioAtendimentoKm());
        perfil.setLatitudeBase(dto.latitudeBase());
        perfil.setLongitudeBase(dto.longitudeBase());
        return PerfilPrestadorResponseDTO.de(perfilRepository.save(perfil));
    }

    @Transactional(readOnly = true)
    public PerfilPrestadorResponseDTO buscarPorId(UUID id) {
        return PerfilPrestadorResponseDTO.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public PerfilPrestadorResponseDTO buscarPorUsuario(UUID usuarioId) {
        PerfilPrestador perfil = perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Perfil de prestador não encontrado para o usuário: " + usuarioId));
        return PerfilPrestadorResponseDTO.de(perfil);
    }

    @Transactional(readOnly = true)
    public List<PerfilPrestadorResponseDTO> listar(boolean apenasAtivos) {
        List<PerfilPrestador> perfis = apenasAtivos ? perfilRepository.findByAtivoTrue() : perfilRepository.findAll();
        return perfis.stream().map(PerfilPrestadorResponseDTO::de).toList();
    }

    @Transactional
    public PerfilPrestadorResponseDTO atualizar(UUID id, PerfilPrestadorUpdateDTO dto) {
        PerfilPrestador perfil = buscarEntidade(id);
        perfil.setBiografia(dto.biografia());
        perfil.setRaioAtendimentoKm(dto.raioAtendimentoKm());
        perfil.setLatitudeBase(dto.latitudeBase());
        perfil.setLongitudeBase(dto.longitudeBase());
        return PerfilPrestadorResponseDTO.de(perfilRepository.save(perfil));
    }

    @Transactional
    public PerfilPrestadorResponseDTO alterarAtivo(UUID id, boolean ativo) {
        PerfilPrestador perfil = buscarEntidade(id);
        perfil.setAtivo(ativo);
        return PerfilPrestadorResponseDTO.de(perfilRepository.save(perfil));
    }

    @Transactional
    public PerfilPrestadorResponseDTO atualizarVerificacao(UUID id, PerfilPrestadorVerificacaoDTO dto) {
        PerfilPrestador perfil = buscarEntidade(id);
        perfil.setVerificadoAntecedentes(dto.verificadoAntecedentes());
        perfil.setVerificadoTecnico(dto.verificadoTecnico());
        return PerfilPrestadorResponseDTO.de(perfilRepository.save(perfil));
    }

    PerfilPrestador buscarEntidade(UUID id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil de prestador não encontrado: " + id));
    }
}
