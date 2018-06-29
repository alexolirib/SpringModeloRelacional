package spring.modelo.relacional.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.modelo.relacional.domain.ItemPedido;
import spring.modelo.relacional.domain.PagamentoComBoleto;
import spring.modelo.relacional.domain.Pedido;
import spring.modelo.relacional.domain.Produto;
import spring.modelo.relacional.domain.enums.EstadoPagamento;
import spring.modelo.relacional.repositories.ItemPedidoRepository;
import spring.modelo.relacional.repositories.PagamentoRepository;
import spring.modelo.relacional.repositories.PedidoRepository;
import spring.modelo.relacional.repositories.ProdutoRepository;
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
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
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
			ip.setProduto(findByProduto(ip));
			//preciso botar o preço do pedido o mesmo do produto
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		//System.out.println(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	private Produto findByProduto(ItemPedido ip) {
		Optional<Produto> obj = produtoRepository.findById(ip.getProduto().getId());
		return obj.orElseThrow(()->new ObjectNotFoundException("Objeto não encontrado! : id"
				+ ip.getProduto().getId() + ", tipo: " + Produto.class.getName()));
	}

}
