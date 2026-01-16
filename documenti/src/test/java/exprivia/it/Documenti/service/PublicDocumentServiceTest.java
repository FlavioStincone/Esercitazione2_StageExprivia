package exprivia.it.Documenti.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import exprivia.it.Documenti.exception.DocumentNotFoundException;
import exprivia.it.Documenti.mapper.PublicDocumentMapper;
import exprivia.it.Documenti.model.dto.PublicDocumentDTO;
import exprivia.it.Documenti.model.entity.PublicDocument;
import exprivia.it.Documenti.repository.PublicDocumentRepository;
import exprivia.it.Documenti.service.impl.PublicDocumentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PublicDocumentServiceTest {

    @InjectMocks
    private PublicDocumentServiceImpl service;

    @Mock
    private PublicDocumentRepository repository;

    @Mock
    private PublicDocumentMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class GetByNumeroProtocolloTests {

        @Test
        void should_IncrementVisualizations_When_DocumentFound() {

            //given
            String protocollo = "A123";
            PublicDocument document = new PublicDocument();
            document.setProtocolNumber(protocollo);
            document.setViewsNumber(0); //per sicurezza

            PublicDocumentDTO dtoFinto = new PublicDocumentDTO(protocollo, "Titolo", "content", "author", "hash");

            when(repository.findByProtocolNumber(protocollo)).thenReturn(Optional.of(document));
            when(mapper.toDTO(document)).thenReturn(dtoFinto);

            //when
            PublicDocumentDTO result = service.getPublicDocumentByProtocolNumber(protocollo);

            //then
            assertNotNull(result);
            assertEquals(1, document.getViewsNumber()); 
            verify(repository, times(1)).save(document); 
        }

        @Test
        void should_ThrowException_When_ProtocolNumeberNotFound(){

            //given
            String protocollo = "non esistente";
            
            //when
            when(repository.findByProtocolNumber(protocollo)).thenReturn(Optional.empty());

            //then 
            assertThrows(DocumentNotFoundException.class, () -> {
                service.getPublicDocumentByProtocolNumber(protocollo);
            });
        }
    }

    @Nested
    class CreateDocumentTests{

        @Test
        void should_HashPassword_And_Save_When_Creating() {

            //given
            PublicDocumentDTO dto = new PublicDocumentDTO("1234", "Titolo", "content", "author",  "pippo");

            PublicDocument entity = new PublicDocument();
            entity.setHashSignature("pippo"); 

            when(mapper.toEntity(dto)).thenReturn(entity);
            when(passwordEncoder.encode("pippo")).thenReturn("HASH_XYZ");
            when(repository.save(any())).thenReturn(entity); 
            when(mapper.toDTO(entity)).thenReturn(dto);

            //when
            PublicDocumentDTO result = service.createPublicDocument(dto);

            //then
            assertNotNull(result); 
            verify(passwordEncoder, times(1)).encode("pippo");
            verify(repository, times(1)).save(any());
        }

        @Nested
        class getDocumentsTest{

            @Test
            void should_ReturnListOfDTOs_When_DocumentsExist(){

                //given
                List<PublicDocument> entities = List.of(new PublicDocument(), new PublicDocument());
                List<PublicDocumentDTO> dtos = List.of( new PublicDocumentDTO("1", "T", "C", "A", "H"), new PublicDocumentDTO("2", "T", "C", "A", "H"));

                when(repository.findAll()).thenReturn(entities);
                when(mapper.toListDTO(entities)).thenReturn(dtos);

                //when
                List<PublicDocumentDTO> result = service.getPublicDocuments();

                //then
                assertNotNull(result);
                assertEquals(2, result.size()); // Best practice prima il valore atteso, poi quello attuale
                verify(repository, times(1)).findAll();
            }
        }

    }
}