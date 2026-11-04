package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.UsuarioCreateDTO;
import br.com._hire.projeto_web.dto.UsuarioResponseDTO;
import br.com._hire.projeto_web.dto.UsuarioUpdateDTO;
import br.com._hire.projeto_web.entity.Usuario;
import br.com._hire.projeto_web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpf(dto.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
        usuario.setTelefone(dto.telefone());
        usuario.setCpf(dto.cpf());

        return UsuarioResponseDTO.de(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDTO::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(UUID id) {
        return UsuarioResponseDTO.de(buscarEntidade(id));
    }

    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarEntidade(id);

        boolean mudouEmail = !usuario.getEmail().equals(dto.email());
        if (mudouEmail && usuarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());

        return UsuarioResponseDTO.de(usuarioRepository.save(usuario));
    }

    @Transactional
    public void deletar(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarEntidade(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}