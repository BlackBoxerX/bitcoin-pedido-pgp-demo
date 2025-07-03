# Bitcoin Order PGP Demo

An educational project for Bitcoin (Testnet) paid orders, with PGP-encrypted secret messages released after payment confirmation.

---

## 🚀 Tech Stack

- Java + Spring Boot
- H2 Database
- Integration with BlockCypher API (Testnet)
- PGP Encryption (BouncyCastle)

---

## 💡 How It Works

1. The client creates an order with name, email, amount, and **PGP public key**.
2. The system generates a unique **Bitcoin (Testnet) address** for payment.
3. The user pays with Testnet BTC.
4. Upon payment confirmation, the system:
    - Sets the order status to `"PAID"`
    - Generates a secret message, encrypted with the client’s PGP key
    - The client can decrypt the message using their private PGP key

---

## 🔨 How to Run

1. **Clone this repository:**
    ```bash
    git clone https://github.com/YOURUSERNAME/bitcoin-order-pgp-demo.git
    cd bitcoin-order-pgp-demo
    ```

2. **Add your BlockCypher API token:**  
   Set your token in `PedidoService.java`

3. **Start the backend:**
    ```bash
    ./mvnw spring-boot:run
    # or
    mvn spring-boot:run
    ```

4. **Interact with the API:**  
   Use Postman, Insomnia, or any REST client.

---

## 📦 API Usage Example

**Create Order (POST /api/pedidos):**
```json
{
  "nomeCliente": "YourName",
  "email": "your@email.com",
  "valor": 0.001,
  "chavePgp": "-----BEGIN PGP PUBLIC KEY BLOCK-----\\nYOUR KEY HERE\\n-----END PGP PUBLIC KEY BLOCK-----"
}

