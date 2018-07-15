package spring.modelo.relacional.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.modelo.relacional.domain.Cidade;
import spring.modelo.relacional.domain.Estado;
import spring.modelo.relacional.repositories.CidadeRepository;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    CidadeRepository repo;

    @Autowired
    EstadoService estadoService;

    public List<Cidade> findByEstados(Integer id){
        Estado estado = estadoService.findById(id);
        return repo.findByEstado(estado);
    }
}
