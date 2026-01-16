package exprivia.it.Documenti.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import exprivia.it.Documenti.exception.DocumentNotFoundException;
import exprivia.it.Documenti.model.dto.PublicDocumentDTO;
import exprivia.it.Documenti.service.IPublicDocument;

@WebMvcTest(PublicDocumenController.class)
class PublicDocumenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private IPublicDocument service; 

    @Autowired
    private ObjectMapper objectMapper; 

    @Test
    void should_ReturnList_When_GetPublicDocuments() throws Exception {
        // given
        List<PublicDocumentDTO> docs = List.of(
            new PublicDocumentDTO("A1", "T1", "C1", "Auth1", "H1"),
            new PublicDocumentDTO("A2", "T2", "C2", "Auth2", "H2")
        );
        given(service.getPublicDocuments()).willReturn(docs);

        // when then 
        mockMvc.perform(get("/document/public"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void should_ReturnDocument_When_Found() throws Exception {
        // given
        String protocol = "A123";
        PublicDocumentDTO doc = new PublicDocumentDTO(protocol, "Title", "Content", "Author", "Hash");
        
        given(service.getPublicDocumentByProtocolNumber(protocol)).willReturn(doc);

        // when then
        mockMvc.perform(get("/document/public/{numProtocollo}", protocol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.protocolNumber").value(protocol));
    }

    @Test
    void should_Return404_When_NotFound() throws Exception {
        // given
        String protocol = "NON_ESISTENTE";
        
        given(service.getPublicDocumentByProtocolNumber(protocol))
                .willThrow(new DocumentNotFoundException("Not found"));

        // when then
        mockMvc.perform(get("/document/public/{numProtocollo}", protocol))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_CreateDocument_When_Valid() throws Exception {
        // given
        PublicDocumentDTO input = new PublicDocumentDTO("N1", "Title", "Content", "Author", "clearPass");
        PublicDocumentDTO output = new PublicDocumentDTO("N1", "Title", "Content", "Author", "HASHED_PASS");
        
        given(service.createPublicDocument(any(PublicDocumentDTO.class))).willReturn(output);

        // when then
        mockMvc.perform(post("/document/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hashSignature").value("HASHED_PASS"));
    }
}