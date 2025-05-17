# 🔗 Encurtador de URLs

Este projeto é uma API simples de encurtamento de URLs desenvolvida com **Java + Spring Boot**, utilizando **MongoDB** como base de dados. A aplicação permite gerar URLs curtas que expiram automaticamente após 1 minuto, além de monitorar o número de acessos e armazenar os IPs dos usuários que acessaram o link.

> 📦 **Importante:** A instância do MongoDB utilizada no projeto está rodando via **Docker**, facilitando a configuração e portabilidade do ambiente.

---

## ✨ Funcionalidades

- 🔐 Geração de URL curta com ID aleatório
- ⏱️ Expiração automática da URL (1 minuto)
- 📈 Contador de acessos à URL
- 📍 Armazenamento dos IPs que acessaram a URL
- 🔄 Redirecionamento automático para a URL original

---

## 🛠️ Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Data MongoDB
- MongoDB
- Apache Commons Lang
- Jakarta Servlet API
- Docker (MongoDB)

---

## 🐳 MongoDB com Docker

Para subir a instância do MongoDB via Docker, utilize o seguinte comando:

docker run --name mongodb-url-shortener \
  -p 27017:27017 \
  -d mongo

> 💡 Certifique-se de que o Spring Boot está configurado para se conectar à porta padrão `27017`.

---

## 📦 Endpoints da API

### `POST /shorten`

Cria uma nova URL encurtada.

**Requisição:**

{
  "url": "https://www.exemplo.com"
}

**Resposta:**


{
  "shortenedUrl": "http://localhost:8080/a1b2c"
}

---

### `GET /{id}`

Redireciona para a URL original, se o link ainda estiver válido.

**Exemplo:**

GET /a1b2c

**Comportamento:**

* Redireciona com HTTP 302 para a URL original
* Registra:

  * IP da requisição
  * Contador de visualizações

---

## 🧠 Regras de Negócio

* A URL encurtada expira após **1 minuto**
* Cada acesso incrementa o contador e registra o IP do cliente
* O ID da URL é gerado aleatoriamente (entre 5 e 10 caracteres alfanuméricos)
* A URL só é salva se o ID gerado ainda não existir no banco

---

## 📁 Expiração automática no MongoDB

A expiração dos documentos é feita automaticamente com o uso de um índice TTL (Time-To-Live) no MongoDB:

@Indexed(expireAfter = "0s")
private LocalDateTime expiresAt;

O MongoDB verifica periodicamente esse campo e remove automaticamente os documentos cujo valor de `expiresAt` já passou.

---

## 📂 Estrutura da entidade principal

public class Url {
    private String id;                   // ID curto da URL
    private String fullUrl;              // URL original
    private LocalDateTime expiresAt;     // Data/hora de expiração
    private int accessCount;             // Quantidade de acessos
    private List<String> accessedBy;     // Lista de IPs que acessaram
}

---

## 🧪 Como testar

Você pode usar ferramentas como **Postman**, **Insomnia** ou **cURL** para testar os endpoints.

### Exemplo com cURL:

curl -X POST http://localhost:8080/shorten \
     -H "Content-Type: application/json" \
     -d '{"url": "https://www.google.com"}'

---

## 📌 Organização do projeto

src/
├── controller/
│   ├── UrlController.java
│   └── dto/
│       ├── ShortenUrlRequest.java
│       └── ShortenUrlResponse.java
├── entities/
│   └── Url.java
├── repository/
│   └── UrlRepository.java

---

## 🚧 Melhorias futuras (ideias)

* Autenticação para gerenciamento de URLs
* Tempo de expiração customizável por requisição
* Painel web com estatísticas e histórico
* Sistema de analytics completo com filtros

---

## 🤝 Contribuição

Contribuições são bem-vindas! Se tiver ideias, bugs ou sugestões, abra uma issue ou envie um pull request.

---

## 📜 Licença

Este projeto está sob a licença MIT.
