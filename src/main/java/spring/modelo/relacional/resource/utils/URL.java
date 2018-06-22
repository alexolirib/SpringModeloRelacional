package spring.modelo.relacional.resource.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	//(classe produtoResource) é para pegar a lista de ids que irá vir  
	//no método get do findPage em lista de inteiros e assim fazer minha busca
	public static List<Integer> decodeIntList(String s) {
		//o split função que quebra em vetor baseado no que está no ()
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		
		for(String valor: vet) {
			list.add(Integer.parseInt(valor));
		}
		
		return list;
		//outra forma mais simples
		//return Arrays.asList(s.split(",").stream().map(x-> Integer.parseInt(x)).collect(Collectors.ToList());
	}
	
	//(classe produtoResource) fazer o encode pois se for escrito espaço na url é para ter a separação 
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			//aso der erro retorno string vazia 
			return "";
		}
	}
}
