package exprivia.it.Documenti.service;

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
import exprivia.it.Documenti.exception.EmergencyProtocolException;
import exprivia.it.Documenti.mapper.SecretDocumentMapper;
import exprivia.it.Documenti.model.dto.SecretDocumentDTO;
import exprivia.it.Documenti.model.entity.SecretDocument;
import exprivia.it.Documenti.repository.SecretDocumentRepository;
import exprivia.it.Documenti.service.impl.SecretDocumentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SecretDocumentServiceTest {

    @InjectMocks
    private SecretDocumentServiceImpl service;

    @Mock
    private SecretDocumentRepository repository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private SecretDocumentMapper mapper;

    @Nested 
    class createSecretDocumentTest{

        @Test
        void should_DeleteDocument_And_ThrowException_When_CodeIsWrong() {
            
            //given
            String protocollo = "AREA-51";
            String codiceSbagliato = "PIPPO"; 

            SecretDocumentDTO documentDTO = new SecretDocumentDTO(protocollo, "confidentialityReason", "title", "content", "author", "hashSignature");
            SecretDocument document = new SecretDocument();
            document.setProtocolNumber(protocollo);

            //when then
            assertThrows(EmergencyProtocolException.class, () -> {
                service.createSecretDocument(documentDTO, codiceSbagliato);
            });
        }

        @Test
        void should_CreateSecretDocument_When_CodeIsCorrect() {
            
            //given
            SecretDocumentDTO dto = new SecretDocumentDTO("1234","confidentialityReason", "title", "content", "author",  "hashSignature");

            SecretDocument entity = new SecretDocument();
            entity.setHashSignature("hashSignature"); 

            when(mapper.toEntity(dto)).thenReturn(entity);
            when(passwordEncoder.encode("hashSignature")).thenReturn("HASH_XYZ");
            when(repository.save(any())).thenReturn(entity); 
            when(mapper.toDTO(entity)).thenReturn(dto);

            //when
            SecretDocumentDTO result = service.createSecretDocument(dto,"PRT45");

            //then
            assertNotNull(result); 
            verify(passwordEncoder, times(1)).encode("hashSignature");
            verify(repository, times(1)).save(any());
        }
    }

    @Nested
    class getSecretDocumentByNumProtocolloTest {

        @Test
        void should_DeleteDocument_And_ThrowException_When_CodeIsWrong() {
            
            //given
            String protocollo = "AREA-51";
            String codiceSbagliato = "PIPPO"; 

            SecretDocument document = new SecretDocument();
            document.setProtocolNumber(protocollo);

            when(repository.findByProtocolNumber(protocollo)).thenReturn(Optional.of(document));

            //when
            assertThrows(EmergencyProtocolException.class, () -> {
                service.getSecretDocumentByNumProtocollo(protocollo, codiceSbagliato);
            });

            //then
            verify(repository, times(1)).delete(document);
        }

        @Test
        void should_ThrowExcwption_When_DocumentoNotFound() {
            
            //given
            String protocollo = "INVALID_PROTOCOL";
            String presidentCode = "PRT45"; 

            SecretDocument document = new SecretDocument();
            document.setProtocolNumber("AREA-51");

            //when then
            assertThrows(DocumentNotFoundException.class, () -> {
                service.getSecretDocumentByNumProtocollo(protocollo, presidentCode);
            });
        }

        @Test
        void should_ReturnDTO_When_DocumentoFound() {
            
            //given
            String protocollo = "AREA-51";
            String presidentCode = "PRT45"; 

            SecretDocument document = new SecretDocument();
            SecretDocumentDTO documentDTO = new SecretDocumentDTO(protocollo,"confidentialityReason", "title", "content", "author", "hashSignature");
            document.setProtocolNumber(protocollo);

            when(repository.findByProtocolNumber(protocollo)).thenReturn(Optional.of(document));
            when(mapper.toDTO(document)).thenReturn(documentDTO);
            
            //when 
            SecretDocumentDTO result = service.getSecretDocumentByNumProtocollo(protocollo, presidentCode);

            //then
            assertNotNull(result);
            verify(repository, times(1)).findByProtocolNumber(protocollo); 
        }
    }

    @Nested
    class getSecretDocumentsTest {

        @Test
        void should_DeleteDocument_And_ThrowException_When_CodeIsWrong() {
            
            //given
            String codiceSbagliato = "PIPPO"; 

            //when then
            assertThrows(EmergencyProtocolException.class, () -> {
                service.getSecretDocuments(codiceSbagliato);
            });
        }

        @Test
        void should_GetDocuments_When_PresidentCOdeIsCorrect() {
            
            //given
            String presidentCode = "PRT45";
            SecretDocumentDTO dto = new SecretDocumentDTO("1234","confidentialityReason", "title", "content", "author",  "hashSignature");
            List<SecretDocumentDTO> dtos = List.of(dto);

            SecretDocument entity = new SecretDocument();
            List<SecretDocument> entities = List.of(entity);

            entity.setHashSignature("hashSignature"); 

            //when then
            List<SecretDocumentDTO> result = service.getSecretDocuments(presidentCode);

            assertNotNull(result); 
            verify(repository, times(1)).findAll();
        }
        
    }
}