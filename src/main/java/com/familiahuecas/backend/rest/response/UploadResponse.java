
package com.familiahuecas.backend.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponse {
    private String message;
    private Long documentId;
    private String path;
}

