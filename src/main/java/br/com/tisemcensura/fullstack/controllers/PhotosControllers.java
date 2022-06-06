package br.com.tisemcensura.fullstack.controllers;

//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.tisemcensura.fullstack.entities.Product;
import br.com.tisemcensura.fullstack.repositories.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("/photos")
public class PhotosControllers {
	
	@Autowired
	private ProductRepository repository;
	
	private Optional<Product> product;
	
	private String path;	
	
//	@Value("${application.disco.raiz}")
//	private String raiz;
//	
//	@Value("${application.disco.diretorio}")
//	private String diretorio;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	public void updateProduct(@PathVariable long id, String caminho) {
		this.product = repository.findById(id);
		product.ifPresent(product -> {
			product.setPath(caminho);
			repository.save(product);
		});
	}
	
//	public void save(String diretorio, MultipartFile file) {
//		Path diretorioPath = Paths.get(this.raiz, diretorio);
//		Path arquivoPath = diretorioPath.resolve(file.getOriginalFilename());
//		try {
//			Files.createDirectories(diretorioPath);
//			file.transferTo(arquivoPath.toFile());
//			
//		}catch (Exception e) {
//			throw new RuntimeException("Falhou Upload " + e);
//		}
//		
//	}
	
	@CrossOrigin
	@PostMapping(value = {"/{id}"})
	public String upload(@RequestParam MultipartFile file, @PathVariable long id) throws Exception {
		try {
				this.updateProduct(id, file.getOriginalFilename());
//				this.save(this.diretorio, file);
//				this.setPath(this.raiz + this.diretorio + "/" + file.getOriginalFilename());
		}catch (Exception e) {
			throw new Exception("Upload Fail  - " + e);			
		}
		return file.getOriginalFilename();
	}
	
}



