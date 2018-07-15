package spring.modelo.relacional.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.modelo.relacional.domain.Estado;
import spring.modelo.relacional.repositories.EstadoRepository;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    EstadoRepository repo;

    public List<Estado> findByAll(){
        List<Estado> estados = repo.findAllByOrderByNome();
        return estados;
    }

    public Estado findById(Integer id){
        Optional<Estado> obj = repo.findById(id);
        return obj.orElseThrow(()-> new ObjectNotFoundException(
                "Objeto não encontrado! id:" + id + ", tipo:" + Estado.class.getName()
        ));
    }
}
