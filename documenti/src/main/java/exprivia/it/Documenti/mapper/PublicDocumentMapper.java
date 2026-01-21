package exprivia.it.documenti.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import exprivia.it.documenti.model.dto.PublicDocumentDTO;
import exprivia.it.documenti.model.entity.PublicDocument;

@Mapper(componentModel = "spring")
public interface PublicDocumentMapper {
    // Documento Pubblico
    PublicDocumentDTO toDTO(PublicDocument document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "viewsNumber", ignore = true)
    PublicDocument toEntity(PublicDocumentDTO publicDocumentDTO);

    List<PublicDocumentDTO> toListDTO(List<PublicDocument> documents);

    List<PublicDocument> toListEntity(List<PublicDocumentDTO> documentDTOS);
}
