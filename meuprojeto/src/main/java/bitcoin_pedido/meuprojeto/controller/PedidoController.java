package bitcoin_pedido.meuprojeto.controller;


import bitcoin_pedido.meuprojeto.model.Pedido;
import bitcoin_pedido.meuprojeto.service.PedidoService;
import bitcoin_pedido.meuprojeto.util.PgpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Criar um novo pedido
    @PostMapping
    public Pedido criarPedido(@RequestBody Pedido pedidoRequest) {
        return pedidoService.criarPedido(
                pedidoRequest.getNomeCliente(),
                pedidoRequest.getEmail(),
                pedidoRequest.getValor()
        );
    }

    // Checar se o pagamento caiu (consultando saldo real na BlockCypher)
    @PutMapping("/pago/{enderecoBitcoin}")
    public Pedido checarPagamentoReal(@PathVariable String enderecoBitcoin) {
        Pedido pedido = pedidoService.buscarPorEndereco(enderecoBitcoin);
        if (pedido != null && !"PAGO".equals(pedido.getStatus())) {
            boolean pago = pedidoService.verificarPagamento(enderecoBitcoin, pedido.getValor());
            if (pago) {
                pedido.setStatus("PAGO");
                // >>>> AQUI ENTRA O PGP <<<<
                // Chama a utilitária para criptografar mensagem e salva no pedido
                try {
                    String mensagem = "Seu pedido foi pago com sucesso! Parabéns! 🎉";
                    String msgCriptografada = PgpUtil.encryptWithPGP(pedido.getChavePgp(), mensagem);
                    pedido.setMensagemCriptografada(msgCriptografada);
                } catch (Exception e) {
                    pedido.setMensagemCriptografada("Falha ao criptografar mensagem: " + e.getMessage());
                }
                pedidoService.salvar(pedido);
            }
        }
        return pedido;
    }


    // Listar todos os pedidos
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }


}


