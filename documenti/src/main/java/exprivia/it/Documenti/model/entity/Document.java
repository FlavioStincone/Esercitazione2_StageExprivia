package exprivia.it.Documenti.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;
import lombok.Getter;
 
@MappedSuperclass //Jakarta
@Getter //Lombok
@Setter //Lombok
public abstract class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String protocolNumber;
  private String title;
  private String content;
  private String author;
  private String hashSignature;
  

  //Costruttore Vuoto
  public Document(){

  }

  //Costruttore Pieno
  public Document(String protocolNumber, String title, String content,String author,String hashSignature){

    this.protocolNumber = protocolNumber;
    this.title = title;
    this.content = content;
    this.author = author;
    this.hashSignature = hashSignature;

  } 

}
