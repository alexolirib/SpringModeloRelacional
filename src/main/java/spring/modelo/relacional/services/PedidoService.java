package spring.modelo.relacional.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.domain.ItemPedido;
import spring.modelo.relacional.domain.PagamentoComBoleto;
import spring.modelo.relacional.domain.Pedido;
import spring.modelo.relacional.domain.enums.EstadoPagamento;
import spring.modelo.relacional.repositories.ItemPedidoRepository;
import spring.modelo.relacional.repositories.PagamentoRepository;
import spring.modelo.relacional.repositories.PedidoRepository;
import spring.modelo.relacional.security.UserSS;
import spring.modelo.relacional.services.Exception.AuthorizationException;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired EnderecoService enderecoService;
	
	public Pedido findById(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado! : id"
				+ id + ", tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert( Pedido obj) {
		obj.setId(null);		
		//new Date instancia data atual 
		obj.setInstante(new Date());
		//para poder fazer o toString do cliente
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		//para poder fazer o toString do endereco
		obj.setEnderecoDeEntrega(enderecoService.findById(obj.getEnderecoDeEntrega().getId()));
		//pedido acabando de inserir estará pendente
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		//pagamento tem conhecer o pedido
		obj.getPagamento().setPedido(obj);
		//se pagamento é tipo PagamentoComBoleto
		if(obj.getPagamento() instanceof  PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			//aqui normalmente iria para uma webservice para gerar o boleto, porem como não temos
			//iremos gerar vencimento automático 
			boletoService.preencherPagementoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			//para saberconseguir pegar o produto para o toString
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			//preciso botar o preço do pedido o mesmo do produto
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		//System.out.println(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
		emailService.sendOrderConfirmationAdmEmail(obj);
		return obj;
	}
	
	//pedido que terá que fazer a regra de negócio para retornar o pedido somente
	//daquele usuario que estiver logado no sistema
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		UserSS user = UserService.authenticad();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Cliente cliente = clienteService.findById(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}

}
