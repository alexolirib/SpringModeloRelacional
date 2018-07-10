package spring.modelo.relacional.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;

import spring.modelo.relacional.services.Exception.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired // instancia do config
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	// MultipartFile esse é o tpo que envia na requisição
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			// extrai o nome do arquivo enviado
			String fileName = multipartFile.getOriginalFilename();
			// obj basico de leitura(encapsula processo se leitura de uma origem
			InputStream is;
			is = multipartFile.getInputStream();
			// tipo do arquivo enviado
			String contenType = multipartFile.getContentType();
			return uploadFile(is, fileName, contenType);
		}catch(AmazonS3Exception e) {
			throw new AmazonS3Exception("Erro pois está faltando chave de acesso");
		} catch (IOException e) {
			throw new FileException("Erro de IO: "+ e.getMessage());
		}

	}

	public URI uploadFile(InputStream is, String fileName, String contenType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contenType);

			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");

			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			// quase impossivel de acontecer
			throw new RuntimeException("Erro ao converter  Url em Uri");
		}
	}

}

// caminho de um arquivo local(fazer upload do arquivo para o s3)
/*
 * public void uploadFile(String localFilePath) { try { File file = new
 * File(localFilePath); LOG.info("Iniciando upload"); s3client.putObject(new
 * PutObjectRequest(bucketName, "teste.jpg", file));
 * LOG.info("Upload finalizado"); }catch (AmazonServiceException e) {
 * LOG.info("AmazonServiceException: "+ e.getErrorMessage());
 * LOG.info("Status code: "+ e.getErrorCode());
 * 
 * } catch(AmazonClientException e) { LOG.info("AmazonClientException: " +
 * e.getMessage()); } }
 */
