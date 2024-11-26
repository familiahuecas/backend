package com.familiahuecas.backend.rest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.familiahuecas.backend.entity.Documento;
import com.familiahuecas.backend.rest.request.DocumentoRequest;
import com.familiahuecas.backend.rest.response.DocumentTreeResponse;
import com.familiahuecas.backend.rest.response.DocumentoResponse;
import com.familiahuecas.backend.rest.response.UploadResponse;
import com.familiahuecas.backend.service.DocumentosService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/documentos")
@Slf4j
public class DocumentosRest {

    private final DocumentosService documentoService;

    public DocumentosRest(DocumentosService documentoService) {
        this.documentoService = documentoService;
    }

    // ------------------------------
    // Métodos de Creación
    // ------------------------------

    /**
     * Añadir un nodo (carpeta o archivo) con opción de asignar un padre
     */
    @PostMapping("/add-node")
    public ResponseEntity<?> addNode(
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestBody DocumentoRequest node) {
        try {
            Documento savedNode = documentoService.addNode(parentId, node);
            return ResponseEntity.ok(savedNode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear nodo: " + e.getMessage());
        }
    }

    /**
     * Subir un archivo a un directorio específico
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "parentId", required = false) Long parentId) {
        try {
            Path directory = Paths.get("/var/www/familiahuecas/documents/");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Generar nombre único y guardar archivo
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = directory.resolve(fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Crear el documento
            /*Documento document = Documento.builder()
                    .nombre(file.getOriginalFilename())
                    .path(path.toString())
                    .esCarpeta(false) // Siempre archivo
                    .build();*/
            DocumentoRequest documentoRequest = DocumentoRequest.builder()
                    .nombre(file.getOriginalFilename())
                    .path(path.toString())
                    .esCarpeta(false) // Siempre archivo
                    .build();
            
            
            Documento savedDocument = documentoService.addNode(parentId, documentoRequest);
            return ResponseEntity.ok(new UploadResponse("Archivo subido con éxito", savedDocument.getId(), savedDocument.getPath()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo: " + e.getMessage());
        }
    }

    // ------------------------------
    // Métodos de Descarga
    // ------------------------------

    /**
     * Descargar un archivo por su ID
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        try {
            Documento document = documentoService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado con ID: " + id));

            Path path = Paths.get(document.getPath());
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado en el servidor");
            }

            byte[] fileContent = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + document.getNombre() + "\"")
                    .body(fileContent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al descargar el archivo: " + e.getMessage());
        }
    }

    // ------------------------------
    // Métodos de Consulta
    // ------------------------------

    /**
     * Listar documentos paginados
     */
    @GetMapping("/list")
    public ResponseEntity<Page<DocumentoResponse>> getAllDocumentsPaginated(
            @PageableDefault(size = 10) Pageable pageable) {
        try {
            Page<DocumentoResponse> paginatedDocuments = documentoService.getAllPaginated(pageable)
                    .map(doc -> new DocumentoResponse(doc.getId(), doc.getNombre(), doc.getPath(), doc.getFecha()));
            return ResponseEntity.ok(paginatedDocuments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Page.empty());
        }
    }

    @GetMapping("/tree")
    public ResponseEntity<List<DocumentTreeResponse>> getDocumentTree() {
        try {
            log.info("Iniciando generación del árbol de documentos...");
            List<DocumentTreeResponse> response = documentoService.getDocumentTreeResponse();
            log.info("Árbol generado correctamente: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al generar el árbol de documentos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }


    /**
     * Eliminar un nodo hijo de su padre
     */
    @DeleteMapping("/{parentId}/remove-child/{childId}")
    public ResponseEntity<?> removeChildFromParent(
            @PathVariable Long parentId,
            @PathVariable Long childId) {
        try {
            documentoService.removeChildFromParent(parentId, childId);
            return ResponseEntity.ok("Hijo eliminado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el hijo: " + e.getMessage());
        }
    }
}
