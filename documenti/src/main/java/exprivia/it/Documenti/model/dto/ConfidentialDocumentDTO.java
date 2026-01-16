package exprivia.it.Documenti.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfidentialDocumentDTO(
    @NotBlank
    String protocolNumber, 

    @NotBlank
    @Size(max = 255)
    String confidentialityReason, 

    @NotBlank
    @Size(max = 255)
    String title, 

    @NotBlank
    String content, 
    
    @NotBlank
    @Size(max = 100)
    String author, 
    
    @NotBlank
    String hashSignature
){} 
