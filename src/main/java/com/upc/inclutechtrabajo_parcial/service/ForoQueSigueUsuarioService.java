package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.ForoQueSigueUsuarioDTO;
import com.upc.inclutechtrabajo_parcial.model.Foro;
import com.upc.inclutechtrabajo_parcial.model.ForoQueSigueUsuario;
import com.upc.inclutechtrabajo_parcial.model.Usuario;
import com.upc.inclutechtrabajo_parcial.repository.ForoQueSigueUsuarioRepository;
import com.upc.inclutechtrabajo_parcial.repository.ForoRepository;
import com.upc.inclutechtrabajo_parcial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForoQueSigueUsuarioService {
    @Autowired private ForoQueSigueUsuarioRepository foroSeguidoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ForoRepository foroRepository;

    public ForoQueSigueUsuario registrar(ForoQueSigueUsuario seguimiento) {
        seguimiento.setFechaSeguimiento(LocalDateTime.now());
        seguimiento.setNotificacionesActivas(false);

        if (seguimiento.getUsuario() != null && seguimiento.getUsuario().getId() != null) {
            Usuario u = usuarioRepository.findById(seguimiento.getUsuario().getId()).orElse(null);
            seguimiento.setUsuario(u);
        }
        if (seguimiento.getForo() != null && seguimiento.getForo().getId() != null) {
            Foro f = foroRepository.findById(seguimiento.getForo().getId()).orElse(null);
            seguimiento.setForo(f);
        }

        return foroSeguidoRepository.save(seguimiento);
    }

    public List<ForoQueSigueUsuarioDTO> listar() {
        return foroSeguidoRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }
    public void eliminar(Integer id) {
        foroSeguidoRepository.deleteById(id);
    }
    public ForoQueSigueUsuario actualizar(Integer id, ForoQueSigueUsuario s) {
        ForoQueSigueUsuario existente = foroSeguidoRepository.findById(id).orElseThrow();
        existente.setUsuario(s.getUsuario());
        existente.setForo(s.getForo());
        return foroSeguidoRepository.save(existente);
    }
    private ForoQueSigueUsuarioDTO convertirADTO(ForoQueSigueUsuario f) {
        return ForoQueSigueUsuarioDTO.builder()
                .id(f.getId())
                .fechaSeguimiento(f.getFechaSeguimiento())
                .usuarioId(f.getUsuario() != null ? f.getUsuario().getId() : null)
                .foroId(f.getForo() != null ? f.getForo().getId() : null)
                .build();
    }
}