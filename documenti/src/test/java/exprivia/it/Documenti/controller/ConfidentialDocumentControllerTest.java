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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;

import exprivia.it.Documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.Documenti.service.IConfidentialDocument;

@WebMvcTest(ConfidentialDocumentController.class)
public class ConfidentialDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean 
    private IConfidentialDocument service; 

    @Nested
    class getConfidentialDocumentsTest{

        @Test
        void should_ReturnList_When_GetConfidentialDocuments() throws Exception{
            // given
            List<ConfidentialDocumentDTO> dtos = List.of(
                new ConfidentialDocumentDTO("123", "COnfidentialirtyReason", "title", "content", "author", "hashSignature"),
                new ConfidentialDocumentDTO("456", "COnfidentialirtyReason", "title", "content", "author","hashSignature")
            );

            when(service.getConfidentialDocuments("PRT45")).thenReturn(dtos);

            // when then 
            mockMvc.perform(get("/document/confidential/PRT45"))
                    .andExpect(status().isOk()) 
                    .andExpect(jsonPath("$.size()").value(2));
        }
    }

    @Nested
    class getConfidentialDocumentTest{

        @Test
        void should_ReturnDTO_When_GetConfidentialDocument() throws Exception{
            // given
            ConfidentialDocumentDTO dto = new ConfidentialDocumentDTO("123", "COnfidentialirtyReason", "title", "content", "author", "hashSignature");
            when(service.getDocumentByProtocolNumber("123","PRT45")).thenReturn(dto);

            // when then 
            mockMvc.perform(get("/document/confidential/123/PRT45"))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    class createConfidentialDocument{

        @Test 
        void should_CreateDocument_When_Valid() throws Exception{
            // given
            ConfidentialDocumentDTO input = new ConfidentialDocumentDTO("123","ConfidentialityReason", "Title", "Content", "Author", "hashSignature");
            ConfidentialDocumentDTO output = new ConfidentialDocumentDTO("456", "ConfidentialityReason", "Title", "Content", "Author", "HASHED_PASS");
            
            when(service.createConfidentialDocument(input, "PRT45")).thenReturn(output);

            // when then
            mockMvc.perform(post("/document/confidential/PRT45")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.hashSignature").value("HASHED_PASS"));
        }
    }

    
}
