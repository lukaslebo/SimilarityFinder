package ch.propulsion.similarityfinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.propulsion.similarityfinder.domain.Document;
import ch.propulsion.similarityfinder.repository.DocumentRepository;

@Service
@Transactional
public class DefaultDocumentService implements DocumentService {
	
	private final DocumentRepository documentRepository;
	
	@Autowired
	public DefaultDocumentService(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}
	
	@Override
	public Document save(Document document) {
		return this.documentRepository.save(document);
	}

	@Override
	public Document findById(String id) {
		return this.documentRepository.findById(id);
	}

	@Override
	public List<Document> findAll() {
		return this.documentRepository.findAll();
	}

	@Override
	public void deleteById(String id) {
		this.documentRepository.deleteById(id);
	}

}
