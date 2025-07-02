package bitcoin_pedido.meuprojeto.repository;

import bitcoin_pedido.meuprojeto.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Pedido findByEnderecoBitcoin(String enderecoBitcoin);
}

