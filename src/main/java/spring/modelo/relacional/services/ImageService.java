package spring.modelo.relacional.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import spring.modelo.relacional.services.Exception.FileException;

@Service
public class ImageService {
	//obtém uma imagem jpg -> BufferedImage - tipo imagem do java(já vai esta jpg 
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		//pegar extensão do arquivo 
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if(!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("somente imagens PNG e JPG são permitidas"); 
		}
		
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if("png".equals(ext)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao tentar ler arquivo ");
		}
	}

	private BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), 
				BufferedImage.TYPE_INT_RGB);
		//Color.WHITE- se tiver fundo transparente irá colocar um branco 
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}
	
	//retorna obj que encapsula litura a partir de uma leitura
	//metodo que vai upload para o s3 é do tipo imputStram
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo ");
		}
	}
	
	//metodo para cortar a foto
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		//vai descobrir se mecer na largura ou altura 
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		//recortar img
		return Scalr.crop(
				sourceImg,
				(sourceImg.getWidth()/2) - (min/2),
				(sourceImg.getHeight()/2) - (min/2),
				min,
				min);
	}
	
	//redimencionar a imagem 
	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
