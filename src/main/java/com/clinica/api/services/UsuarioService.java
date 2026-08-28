package com.clinica.api.services;

import com.clinica.api.config.mappers.UsuarioMapper;
import com.clinica.api.exception.ConflictException;
import com.clinica.api.exception.ContaBloqueadaException;
import com.clinica.api.exception.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.clinica.api.dto.request.UsuarioRequest;
import com.clinica.api.dto.response.UsuarioResponse;
import com.clinica.api.entities.Usuario;
import com.clinica.api.repositories.UsuarioRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UsuarioService {

    private static final int MAX_TENTATIVAS = 3;
    private static final long MINUTOS_BLOQUEIO = 15;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);


    @Cacheable("usuarios")
    public List<UsuarioResponse> buscarTodos(){
        log.info("Buscando todos os usuarios");
        return usuarioRepository.findAll().stream().map(usuarioMapper::toResponse).toList();
    }

    @Cacheable(value = "usuario", key = "#id")
    public UsuarioResponse buscarPorId(Long id) {
        log.info("Buscando usuario com ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFound("Usuário não encontrada"));

        return usuarioMapper.toResponse(usuario);
    }

    @CacheEvict(value = {"usuarios", "usuario"}, allEntries = true)
    public UsuarioResponse salvar(UsuarioRequest request) {
        log.info("Salvando usuario com email {}", request.getEmail());

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Esse e-mail já está cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(request);

        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioSalvo);
    }

    @CacheEvict(value = {"usuarios", "usuario"}, allEntries = true)
    public UsuarioResponse atualizar(Long id, UsuarioRequest request){
        log.info("Atualizando usuario com ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFound("Usuario não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        usuario.setAtivo(request.getAtivo());


        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioAtualizado);
    }

    @CacheEvict(value = {"usuarios", "usuario"}, allEntries = true)
    public void deletar(Long id){
        log.info("Deletando usuario com ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFound("Usuáio não encontrado"));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }

    //bloqueio do login

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));
    }

    //verificando se o usuario esta bloqueado e se ja passou o tempo
    public void validarBloqueio(Usuario usuario) {
        if (usuario.getBloqueadoAte() == null) {
            return;
        }

        if (usuario.getBloqueadoAte().isAfter(LocalDateTime.now())) {
            String ate = usuario.getBloqueadoAte().format(FORMATO_DATA);
            throw new ContaBloqueadaException(
                    "Conta bloqueada por excesso de tentativas. Tente novamente após " + ate);
        }

        usuario.setTentativasFalhas(0);
        usuario.setBloqueadoAte(null);
        usuarioRepository.save(usuario);
    }


     //Registra uma tentativa de login com senha incorreta cada vez que errar

    public void registrarTentativaFalha(Usuario usuario) {
        int tentativas = usuario.getTentativasFalhas() == null ? 0 : usuario.getTentativasFalhas();
        tentativas++;

        log.warn("Tentativa de login falhou para o usuario {} ({}/{})",
                usuario.getEmail(), tentativas, MAX_TENTATIVAS);

        if (tentativas >= MAX_TENTATIVAS) {
            usuario.setTentativasFalhas(0);
            usuario.setBloqueadoAte(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEIO));
            log.warn("Usuario {} bloqueado até {}", usuario.getEmail(), usuario.getBloqueadoAte());
        } else {
            usuario.setTentativasFalhas(tentativas);
        }

        usuarioRepository.save(usuario);
    }

    //zera o contador quando o login der certo
    public void registrarLoginSucesso(Usuario usuario) {
        if (usuario.getTentativasFalhas() != null && usuario.getTentativasFalhas() != 0
                || usuario.getBloqueadoAte() != null) {
            usuario.setTentativasFalhas(0);
            usuario.setBloqueadoAte(null);
            usuarioRepository.save(usuario);
        }
    }
}