package exprivia.it.Documenti.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicDocumentDTO(

    @Schema(description = "Identificativo univoco del Documento", example = "DOC-P-001")
    @NotBlank 
    String protocolNumber,
    
    @Schema(description = "Titolo del Documento")
    @NotBlank 
    @Size(max = 255)
    String title,
    
    @Schema(description = "Contenuto del documento")
    @NotBlank 
    String content,
    
    @Schema(description = "Autore del documento")
    @NotBlank 
    @Size(max = 100)
    String author,
    
    @Schema(description = "Firma di colui che ha rilasciato il Documento", example = "DOC-001")
    @NotBlank 
    String hashSignature
) {}
