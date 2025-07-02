package bitcoin_pedido.meuprojeto.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCliente;
    private String email;
    private Double valor;
    private String enderecoBitcoin;
    private String status; // "AGUARDANDO", "PAGO"
    // model/Pedido.java
    private String chavePgp;   // Chave pública PGP do cliente

    @Lob // Se ficar muito grande!
    private String mensagemCriptografada; // Mensagem enviada ao pagar (criptografada)

}

