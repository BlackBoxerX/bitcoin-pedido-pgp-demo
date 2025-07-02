package bitcoin_pedido.meuprojeto.service;

import bitcoin_pedido.meuprojeto.model.Pedido;
import bitcoin_pedido.meuprojeto.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Cria um novo pedido, gerando um endereço Bitcoin na BlockCypher (Testnet)
    public Pedido criarPedido(String nomeCliente, String email, Double valor) {
        Pedido pedido = new Pedido();
        pedido.setNomeCliente(nomeCliente);
        pedido.setEmail(email);
        pedido.setValor(valor);

        // Usa endereço real da BlockCypher testnet
        String enderecoReal = gerarEnderecoBitcoinReal();
        pedido.setEnderecoBitcoin(enderecoReal);

        pedido.setStatus("AGUARDANDO");
        return pedidoRepository.save(pedido);
    }

    // Gera endereço Bitcoin Testnet usando BlockCypher
    public String gerarEnderecoBitcoinReal() {
        String token = "SUA_API_TOKEN_BLOCKCYPHER"; // Troque pelo seu token!
        String url = "https://api.blockcypher.com/v1/btc/test3/addrs?token=" + token;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        return (String) response.getBody().get("address");
    }

    // Verifica se pagamento foi recebido no endereço Bitcoin (Testnet)
    public boolean verificarPagamento(String enderecoBitcoin, Double valorEsperado) {
        String url = "https://api.blockcypher.com/v1/btc/test3/addrs/" + enderecoBitcoin;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Object saldo = response.getBody().get("final_balance");

        long saldoSatoshis = 0;
        if (saldo instanceof Integer) {
            saldoSatoshis = ((Integer) saldo).longValue();
        } else if (saldo instanceof Long) {
            saldoSatoshis = (Long) saldo;
        }
        double saldoBTC = saldoSatoshis / 1e8;
        return saldoBTC >= valorEsperado;
    }

    // Marca como pago (usando simulação manual ou checagem real)
    public Pedido marcarComoPago(String enderecoBitcoin) {
        Pedido pedido = pedidoRepository.findByEnderecoBitcoin(enderecoBitcoin);
        if (pedido != null && !"PAGO".equals(pedido.getStatus())) {
            pedido.setStatus("PAGO");
            pedidoRepository.save(pedido);
        }
        return pedido;
    }

    // Buscar por endereço
    public Pedido buscarPorEndereco(String enderecoBitcoin) {
        return pedidoRepository.findByEnderecoBitcoin(enderecoBitcoin);
    }

    // Salvar pedido (pode ser usado para atualizar)
    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    // Listar todos pedidos
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }
}



