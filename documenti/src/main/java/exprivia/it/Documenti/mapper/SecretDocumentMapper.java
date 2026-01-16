package exprivia.it.Documenti.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import exprivia.it.Documenti.model.dto.SecretDocumentDTO;
import exprivia.it.Documenti.model.entity.SecretDocument;

@Mapper(componentModel = "spring")
public interface SecretDocumentMapper {
    //Documento Segreto
    SecretDocumentDTO toDTO(SecretDocument secretDocument);

    @Mapping(target = "id", ignore = true)
    SecretDocument toEntity(SecretDocumentDTO secretDocumentDTO);

    List<SecretDocumentDTO> toListDTO(List<SecretDocument> documents);

    @Mapping(target = "id", ignore = true)
    List<SecretDocument> toListEntity(List<SecretDocumentDTO> documentsDTO);
    
}
