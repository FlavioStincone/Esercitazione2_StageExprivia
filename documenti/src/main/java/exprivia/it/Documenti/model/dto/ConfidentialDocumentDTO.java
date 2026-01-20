package exprivia.it.documenti.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfidentialDocumentDTO(
    
    @Schema(description = "Identificativo univoco del documento", example = "DOC-C-001") // scrive sotto ogni proprietà la descrizone/esempio
    @NotBlank
    String protocolNumber, 

    @Schema(description = "Ragione della privatezza del Documento")
    @NotBlank
    @Size(max = 255)
    String confidentialityReason, 

    @Schema(description = "Titolo del Documento")
    @NotBlank
    @Size(max = 255)
    String title, 

    @Schema(description = "Contenuto del Documento")
    @NotBlank
    String content, 
    
    @Schema(description = "Autore del Documento")
    @NotBlank
    @Size(max = 100)
    String author, 
    
    @Schema(description = "Firma di colui che ha rilasciato il Documento")
    @NotBlank
    String hashSignature
){} 
