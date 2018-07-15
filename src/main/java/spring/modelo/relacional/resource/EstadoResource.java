package spring.modelo.relacional.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring.modelo.relacional.domain.Cidade;
import spring.modelo.relacional.domain.Estado;
import spring.modelo.relacional.dto.CidadeDTO;
import spring.modelo.relacional.dto.EstadoDTO;
import spring.modelo.relacional.services.CidadeService;
import spring.modelo.relacional.services.EstadoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

    @Autowired
    EstadoService service;

    @Autowired
    CidadeService cidadeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EstadoDTO>> findByAll(){
        List<Estado> estados = service.findByAll();

        List<EstadoDTO> estadosDTO = estados.stream().map(e-> new EstadoDTO(e)).collect(Collectors.toList());
        return ResponseEntity.ok().body(estadosDTO);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public  ResponseEntity<Estado> findById(@PathVariable Integer id){
        Estado estado = service.findById(id);
        return ResponseEntity.ok().body(estado);
    }

    @RequestMapping(value = "/{id}/cidades", method = RequestMethod.GET)
    public ResponseEntity<List<CidadeDTO>> findByCidades(@PathVariable Integer id){
        List<Cidade> cidades = cidadeService.findByEstados(id);
        List<CidadeDTO> cidadesDTO = cidades.stream().map(c->new CidadeDTO(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(cidadesDTO);
    }
}
