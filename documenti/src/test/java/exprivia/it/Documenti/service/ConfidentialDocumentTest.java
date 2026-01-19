package exprivia.it.Documenti.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import exprivia.it.Documenti.exception.DocumentAlreadyExistsException;
import exprivia.it.Documenti.exception.DocumentNotFoundException;
import exprivia.it.Documenti.exception.InvalidPresidentCodeException;
import exprivia.it.Documenti.mapper.ConfidentialDocumentMapper;
import exprivia.it.Documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.Documenti.model.entity.ConfidentialDocument;
import exprivia.it.Documenti.repository.ConfidentialDocumentRepository;
import exprivia.it.Documenti.service.impl.ConfidentialDocumentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ConfidentialDocumentTest {

    @InjectMocks
    private ConfidentialDocumentServiceImpl service;

    @Mock
    private ConfidentialDocumentRepository repository;

    @Mock
    private ConfidentialDocumentMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested 
    class getDocumentByProtocolNumberTest{
        
        @Test
        public void should_ReturnDto_When_PasswordIsCorrect(){

            //given
            String protocoloNumber = "1234";
            String passwordInput = "passwordSegreta"; 
            String passwordInDB = "HASH_XYZ";           

            ConfidentialDocument entity = new ConfidentialDocument(protocoloNumber, "motivo", "titolo", "content", "author", passwordInDB);

            ConfidentialDocumentDTO expectedDto = new ConfidentialDocumentDTO(protocoloNumber, "motivo", "titolo", "content", "author", passwordInDB);

            when(repository.findByProtocolNumber(protocoloNumber)).thenReturn(Optional.of(entity));
            when(passwordEncoder.matches(passwordInput, entity.getHashSignature())).thenReturn(true);
            when(mapper.toDTO(entity)).thenReturn(expectedDto);

            //when
            ConfidentialDocumentDTO result = service.getDocumentByProtocolNumber(protocoloNumber, passwordInput);

            //then
            assertNotNull(result);
            assertEquals(expectedDto, result);
            
            verify(passwordEncoder).matches(passwordInput, passwordInDB);
        }

        @Test
        void should_ThrowException_When_PasswordIsWrong() {

            //given
            String protocoloNumber = "1234";
            String wrongPassword = "pippo";
            String hashNelDb = "HASH_VERO";       
  
            ConfidentialDocument entity = new ConfidentialDocument(protocoloNumber, "motivo", "titolo", "content", "author", hashNelDb);

            when(repository.findByProtocolNumber(protocoloNumber)).thenReturn(Optional.of(entity));
            when(passwordEncoder.matches(wrongPassword, hashNelDb)).thenReturn(false);

            //when then
            assertThrows(DocumentNotFoundException.class, () -> {
                service.getDocumentByProtocolNumber(protocoloNumber, wrongPassword);
            });
        }

        @Test
        void should_ThrowException_When_PresidentCodeIsInvalid() {

            // given
            String invalidPresidentCode = "INVALID_CODE"; 
            
            // when  then
            assertThrows(DocumentNotFoundException.class, () -> {
                service.getDocumentByProtocolNumber("123", invalidPresidentCode);
            });
        }

        @Test
        void should_ThrowException_When_PresidentCodeIsvalid() {

            //given
            String protocoloNumber = "1234";
            String presidentCode = "PRT45";

            ConfidentialDocument entity = new ConfidentialDocument(protocoloNumber, "motivo", "titolo", "content", "author", "hashSignature");

            ConfidentialDocumentDTO expectedDto = new ConfidentialDocumentDTO(protocoloNumber, "motivo", "titolo", "content", "author", "hashSignature");

            when(repository.findByProtocolNumber(protocoloNumber)).thenReturn(Optional.of(entity));
            when(passwordEncoder.matches(presidentCode, entity.getHashSignature())).thenReturn(true);
            when(mapper.toDTO(entity)).thenReturn(expectedDto);

            //when
            ConfidentialDocumentDTO result = service.getDocumentByProtocolNumber(protocoloNumber, presidentCode);

            //then
            assertNotNull(result);
            assertEquals(expectedDto, result);
        }
    
    }

    @Nested
    class getConfidentialDocumentsTest{

        @Test
        void should_ReturnList_When_PresidentCodeIsValid() {

            // given
            String validPresidentCode = "PRT45";
            
            ConfidentialDocument doc1 = new ConfidentialDocument();
            doc1.setProtocolNumber("123");
            ConfidentialDocument doc2 = new ConfidentialDocument();
            doc2.setProtocolNumber("456");
            List<ConfidentialDocument> entities = List.of(doc1, doc2);

            ConfidentialDocumentDTO dto1 = new ConfidentialDocumentDTO("123", "ConfidentialityReason", "title", "author", "", "hashSignature"); 
            ConfidentialDocumentDTO dto2 = new ConfidentialDocumentDTO("456", "ConfidentialityReason", "title", "author", "", "hashSignature"); 
            List<ConfidentialDocumentDTO> expectedDtos = List.of(dto1, dto2);
            
            when(repository.findAll()).thenReturn(entities);
            
            when(mapper.toListDTO(entities)).thenReturn(expectedDtos);

            // when
            List<ConfidentialDocumentDTO> result = service.getConfidentialDocuments(validPresidentCode);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("123", result.get(0).protocolNumber());
            
            verify(repository).findAll();
        }

         @Test
        void should_ThrowException_When_PresidentCodeIsInvalid() {

            // given
            String invalidPresidentCode = "INVALID_CODE";
            
           //when then
            assertThrows(InvalidPresidentCodeException.class, () -> {
                service.getConfidentialDocuments(invalidPresidentCode);
            });

            verify(repository, never()).findAll();
        }
    }

    @Nested
    class createConfidentialDocumentTest{

        @Test
        void should_SaveDocument_When_PresidentCodeIsValid() {

            // given
            String validPresidentCode = "PRT45"; 
            
            ConfidentialDocumentDTO inputDto = new ConfidentialDocumentDTO("123", "motivo", "titolo", "cont", "auth", "PasswordChiara");
            ConfidentialDocument entity = new ConfidentialDocument();
            entity.setHashSignature("PasswordChiara");

            ConfidentialDocument savedEntity = new ConfidentialDocument();
            savedEntity.setHashSignature("ENCODED_PASSWORD");

            ConfidentialDocumentDTO expectedDto = new ConfidentialDocumentDTO("123", "motivo", "titolo", "cont", "auth", "ENCODED_PASSWORD");

            when(mapper.toEntity(inputDto)).thenReturn(entity);
            when(passwordEncoder.encode("PasswordChiara")).thenReturn("ENCODED_PASSWORD");
            when(repository.save(entity)).thenReturn(savedEntity);
            when(mapper.toDTO(savedEntity)).thenReturn(expectedDto);

            ConfidentialDocumentDTO result = service.createConfidentialDocument(inputDto, validPresidentCode);

            assertNotNull(result);
            assertEquals("ENCODED_PASSWORD", result.hashSignature());
            
            verify(passwordEncoder).encode("PasswordChiara");
            verify(repository).save(entity);
        }

        @Test
        void should_ThrowException_When_PresidentCodeIsInvalid(){

            // given
            String invalidPresidentCode = "INVALID"; 
            
            ConfidentialDocumentDTO inputDTO = new ConfidentialDocumentDTO("123", "motivo", "titolo", "cont", "auth", "PasswordChiara");
            
            //when then
            assertThrows(InvalidPresidentCodeException.class, () -> {
                service.createConfidentialDocument(inputDTO, invalidPresidentCode);
            });
        }

        @Test
        void should_ThrowException_When_DocumentAlredyExist(){

            // given
            String validCode = "PRT45";
            String protocolloEsistente = "DOC-123";
            ConfidentialDocumentDTO inputDto = new ConfidentialDocumentDTO(protocolloEsistente, "Reason", "Title", "Content", "Auth", "Pass");

            when(repository.existsByProtocolNumber(protocolloEsistente)).thenReturn(true);

            // when then
            assertThrows(DocumentAlreadyExistsException.class, () -> {
                service.createConfidentialDocument(inputDto, validCode);
            });

            verify(repository, never()).save(any());

        }
    }
    
}