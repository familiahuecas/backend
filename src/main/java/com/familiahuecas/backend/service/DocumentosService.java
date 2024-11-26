package com.familiahuecas.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familiahuecas.backend.entity.Documento;
import com.familiahuecas.backend.repository.DocumentosRepository;
import com.familiahuecas.backend.rest.request.DocumentoRequest;
import com.familiahuecas.backend.rest.response.DocumentTreeResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DocumentosService {

    private final DocumentosRepository documentosRepository;

    public DocumentosService(DocumentosRepository documentosRepository) {
        this.documentosRepository = documentosRepository;
    }

    // ------------------------------
    // Métodos CRUD Básicos
    // ------------------------------

    public Documento save(Documento document) {
        return documentosRepository.save(document);
    }

    public Optional<Documento> findById(Long id) {
        return documentosRepository.findById(id);
    }

    public List<Documento> findAll() {
        return documentosRepository.findAll();
    }

    // ------------------------------
    // Métodos de Paginación
    // ------------------------------

    public Page<Documento> getAllPaginated(Pageable pageable) {
        return documentosRepository.findAll(pageable);
    }

    // ------------------------------
    // Métodos de Árbol
    // ------------------------------

    public List<Documento> getDocumentTree() {
        List<Documento> rootDocuments = documentosRepository.findByParentIsNull();
        rootDocuments.forEach(this::populateChildren);
        return rootDocuments;
    }

    private void populateChildren(Documento documento) {
        try {
            log.info("Buscando hijos del documento con ID: {}", documento.getId());
            List<Documento> children = documentosRepository.findByParent(documento);
            log.info("Hijos encontrados: {}", children);

            documento.setChildren(children);
            children.forEach(this::populateChildren);
        } catch (Exception e) {
            log.error("Error al poblar hijos para el documento con ID: {}", documento.getId(), e);
            throw e;
        }
    }


/*    public List<DocumentTreeResponse> getDocumentTreeResponse() {
        return getDocumentTree().stream()
                .map(this::convertToTreeResponse)
                .toList();
    }
*/
    public List<DocumentTreeResponse> getDocumentTreeResponse() {
        try {
            log.info("Iniciando construcción del árbol...");
            List<Documento> rootDocuments = documentosRepository.findByParentIsNull();
            log.info("Documentos raíz encontrados: {}", rootDocuments);

            List<DocumentTreeResponse> tree = rootDocuments.stream()
                    .map(this::convertToTreeResponse)
                    .toList();

            log.info("Árbol construido: {}", tree);
            return tree;
        } catch (Exception e) {
            log.error("Error al construir el árbol de documentos", e);
            throw e; // Re-lanzar la excepción para que el controlador la maneje
        }
    }

    private DocumentTreeResponse convertToTreeResponse(Documento documento) {
        log.info("Convirtiendo documento a respuesta de árbol: {}", documento.getNombre());
        return new DocumentTreeResponse(
                documento.getId(),
                documento.getNombre(),
                documento.isEsCarpeta(),
                documento.getPath(),
                documento.getChildren().stream()
                        .map(this::convertToTreeResponse)
                        .toList()
        );
    }


    @Transactional
    public Documento addNode(Long parentId, DocumentoRequest node) {
    	
    	Documento documento = new Documento();
    	
    	documento.setId(node.getId());
    	documento.setEsCarpeta(node.isEsCarpeta());
    	documento.setPath(node.getPath());
    	documento.setNombre(node.getNombre());
    	
        // Si hay un `parentId`, busca y asigna el padre
        if (parentId != null) {
            Documento parent = documentosRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Carpeta padre no encontrada"));

            if (!parent.isEsCarpeta()) {
                throw new IllegalArgumentException("El documento especificado como padre no es una carpeta");
            }

            documento.setParent(parent);
        } else {
            // Nodo raíz no tiene padre
        	documento.setParent(null);
        }

        return documentosRepository.save(documento);
    }

    @Transactional
    public void removeChildFromParent(Long parentId, Long childId) {
        Documento parent = documentosRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Carpeta padre no encontrada"));

        Documento childToRemove = parent.getChildren().stream()
                .filter(child -> child.getId().equals(childId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hijo no encontrado en la carpeta padre"));

        parent.getChildren().remove(childToRemove);
        childToRemove.setParent(null);

        documentosRepository.save(parent);
    }
}
