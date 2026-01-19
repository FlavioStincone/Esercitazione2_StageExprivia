package exprivia.it.Documenti.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import exprivia.it.Documenti.model.dto.SecretDocumentDTO;
import exprivia.it.Documenti.service.ISecreteDocument;

@WebMvcTest(SecretDocumentController.class)
public class SecretDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean 
    private ISecreteDocument service; 

    @Nested
    class getSecretDocumentsTest{

        @Test
        void should_ReturnList_When_GetSecretDocuments() throws Exception{
            // given
            List<SecretDocumentDTO> dtos = List.of(
                new SecretDocumentDTO("123", "COnfidentialirtyReason", "title", "content", "author", "hashSignature"),
                new SecretDocumentDTO("456", "COnfidentialirtyReason", "title", "content", "author","hashSignature")
            );

            when(service.getSecretDocuments("PRT45")).thenReturn(dtos);

            // when then 
            mockMvc.perform(get("/document/secret/PRT45"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(("$.size()")).value(2));
        }
    }

    @Nested
    class getConfidentialDocumentTest{

        @Test
        void should_ReturnDTO_When_GetConfidentialDocument() throws Exception{
            // given
            SecretDocumentDTO dto = new SecretDocumentDTO("123", "COnfidentialirtyReason", "title", "content", "author", "hashSignature");
            when(service.getSecretDocumentByNumProtocollo("123","PRT45")).thenReturn(dto);

            // when then 
            mockMvc.perform(get("/document/secret/123/PRT45"))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    class createConfidentialDocument{

        @Test 
        void should_CreateDocument_When_Valid() throws Exception{
            // given
            SecretDocumentDTO input = new SecretDocumentDTO("123","ConfidentialityReason", "Title", "Content", "Author", "hashSignature");
            SecretDocumentDTO output = new SecretDocumentDTO("456", "ConfidentialityReason", "Title", "Content", "Author", "HASHED_PASS");
            
            when(service.createSecretDocument(input, "PRT45")).thenReturn(output);

            // when then
            mockMvc.perform(post("/document/secret/PRT45")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.hashSignature").value("HASHED_PASS"));
        }
    
    }
}
