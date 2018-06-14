package spring.modelo.relacional;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.domain.Cidade;
import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.domain.Endereco;
import spring.modelo.relacional.domain.Estado;
import spring.modelo.relacional.domain.Pagamento;
import spring.modelo.relacional.domain.PagamentoComBoleto;
import spring.modelo.relacional.domain.PagamentoComCartao;
import spring.modelo.relacional.domain.Pedido;
import spring.modelo.relacional.domain.Produto;
import spring.modelo.relacional.domain.enums.EstadoPagamento;
import spring.modelo.relacional.domain.enums.TipoCliente;
import spring.modelo.relacional.repositories.CategoriaRepository;
import spring.modelo.relacional.repositories.CidadeRepository;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.repositories.EnderecoRepository;
import spring.modelo.relacional.repositories.EstadoRepository;
import spring.modelo.relacional.repositories.PagamentoRepository;
import spring.modelo.relacional.repositories.PedidoRepository;
import spring.modelo.relacional.repositories.ProdutoRepository;

@SpringBootApplication
public class SpringModeloRelacionalApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringModeloRelacionalApplication.class, args);
	}

	// gerar no banco automaticamente
	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		// como a cidade precisa ter os estados é preciso ser salvo primeiro
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "872894791285", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("387389278", "93244555"));

		Endereco e1 = new Endereco(null, "Rua Flo", "300", "ap 302", "Jardim", "65465484", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "655554111", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"),cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("05/10/2019 15:10"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("09/03/2019 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
	}
}
