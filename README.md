# Bitcoin Pedido PGP Demo

Projeto educativo para pedidos pagos via Bitcoin (Testnet), com mensagem secreta PGP liberada após o pagamento.

## 🚀 Tecnologias
- Java + Spring Boot
- H2 Database
- Integração com BlockCypher API (Testnet)
- Criptografia PGP (BouncyCastle)

## 💡 Como funciona

1. O cliente cria um pedido informando nome, email, valor e **chave pública PGP**.
2. O sistema gera um endereço Bitcoin (testnet) para o pagamento.
3. O usuário paga usando testnet BTC.
4. Ao confirmar o pagamento, o sistema:
    - Muda status do pedido para "PAGO"
    - Gera uma mensagem secreta, criptografada com a chave PGP do cliente
5. O cliente pode descriptografar a mensagem usando sua chave privada.

## 🔨 Como rodar

1. Clone este repositório
2. Coloque seu token da BlockCypher em `PedidoService.java`
3. `./mvnw spring-boot:run`
4. Use Postman/Insomnia ou outro cliente REST para interagir com a API

### Exemplos de uso

#### Criar pedido (POST `/api/pedidos`)
```json
{
  "nomeCliente": "SeuNome",
  "email": "seu@email.com",
  "valor": 0.001,
  "chavePgp": "-----BEGIN PGP PUBLIC KEY BLOCK-----\\nSUA CHAVE AQUI\\n-----END PGP PUBLIC KEY BLOCK-----"
}
