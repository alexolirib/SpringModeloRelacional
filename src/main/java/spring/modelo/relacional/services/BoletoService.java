package spring.modelo.relacional.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagementoComBoleto(PagamentoComBoleto pagto, Date InstanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(InstanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataPagamento(cal.getTime());
	}
}
