package exprivia.it.Documenti.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PublicDocument extends Document{
    
    private int viewsNumber;

    public PublicDocument()
    {
        super();
    }
    
    public PublicDocument(String protocolNumber, String title, String content, String author, String hashSignature){

        super(protocolNumber,title,content,author,hashSignature);
        this.viewsNumber = 0;
        
    }

    public int documentoVisualizzato()
    {
        this.viewsNumber ++;
        return viewsNumber;
    }
}
