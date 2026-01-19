package exprivia.it.Documenti.controller;

import static org.mockito.ArgumentMatchers.any;
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

import exprivia.it.Documenti.model.dto.PublicDocumentDTO;
import exprivia.it.Documenti.service.IPublicDocument;

@WebMvcTest(PublicDocumenController.class)
class PublicDocumenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @MockBean 
    private IPublicDocument service; 

    @Nested
    class GetPublicDocumentsTest{

        @Test
        void should_ReturnList_When_GetPublicDocuments() throws Exception {
            // given
            List<PublicDocumentDTO> dtos = List.of(
                new PublicDocumentDTO("A1", "T1", "C1", "Auth1", "H1"),
                new PublicDocumentDTO("A2", "T2", "C2", "Auth2", "H2")
            );
            when(service.getPublicDocuments()).thenReturn(dtos);

            // when then 
            mockMvc.perform(get("/document/public"))
                    .andExpect(status().isOk()) 
                    .andExpect(jsonPath("$.size()").value(2));
        }
    }

    @Nested
    class getPublicDocumentTest{

        @Test
        void should_ReturnDocument_When_getPublicDocument_Found() throws Exception {
            // given
            String protocol = "A123";
            PublicDocumentDTO dto = new PublicDocumentDTO(protocol, "Title", "Content", "Author", "Hash");
            
            when(service.getPublicDocumentByProtocolNumber(protocol)).thenReturn(dto);

            // when then
            mockMvc.perform(get("/document/public/{numProtocollo}", protocol))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.protocolNumber").value(protocol));
        }
    }

    @Nested
    class createDocumentTest{

        @Test
        void should_CreateDocument_When_Valid() throws Exception {
            // given
            PublicDocumentDTO input = new PublicDocumentDTO("N1", "Title", "Content", "Author", "clearPass");
            PublicDocumentDTO output = new PublicDocumentDTO("N1", "Title", "Content", "Author", "HASHED_PASS");
            
            when(service.createPublicDocument(any(PublicDocumentDTO.class))).thenReturn(output);

            // when then
            mockMvc.perform(post("/document/public")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.hashSignature").value("HASHED_PASS"));
        }
    }
}