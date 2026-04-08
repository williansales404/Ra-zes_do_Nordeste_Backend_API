# Raízes do Nordeste - Backend API

API para gerenciamento da rede de lanchonetes Raízes do Nordeste.

## Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security + JWT
- H2 Database (em memória local)
- Maven
- Swagger/OpenAPI (Springdoc)

## Pré-requisitos

- JDK 17
- Maven (ou use o wrapper `./mvnw`)

## Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/williansales404/Ra-zes_do_Nordeste_Backend_API.git
   cd raizes-backend
   
2. Execute a aplicação:
    ```bash
    ./mvnw spring-boot:run

3. Acesse:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Console H2: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:raizesdb`, usuário `sa`, senha em branco)

## Variáveis de ambiente
Crie um arquivo .env ou configure diretamente no application.properties:

    jwt.secret=XLQ8QVJ5dEj2Or2R2Ir5JXoSLemBiOjbzYsOCvi4lFs
    jwt.expiration=3600000

## Endpoints principais

|Método|Rota|Descrição|Autenticação|
| --- | --- | --- | --- |
|POST|/auth/register|Registrar novo cliente|Público|
|POST|/auth/login|Login e obtenção de token|Público|
|GET|/unidades|Listar unidades|Público|
|GET|/cardapio?unidadeId={id}||Consultar cardápio|Público|
|POST|/pedidos|Criar pedido|JWT (Cliente)|
|GET|/pedidos|Listar pedidos (com filtros)|JWT|
|PATCH|/pedidos/{id}/status	Atualizar status|JWT (Cozinha/Atendente)|
|DELETE|/pedidos/{id}|Cancelar pedido|JWT (Cliente/Atendente)|
|GET|/fidelidade|Consultar pontos|JWT (Cliente)|
|POST|/consentimento|Registrar consentimento LGPD|JWT (Cliente)|

## Testes com Postman

1. Importe a coleção `Raizes_Postman_Collection.json` no Postman.
2. Configure as variáveis de ambiente (ex: `tokenCliente`, `tokenGerente`, `tokenCozinha`) conforme os logins realizados.
3. Execute as requisições na seguinte ordem recomendada:

   - **Auth**: Registrar cliente → Login (guarde o token)
   - **Consentimento**: Registrar consentimento LGPD
   - **Cardápio**: Consultar cardápio público
   - **Pedidos**: Criar pedido → Consultar pedido → Listar pedidos
   - **Fidelidade**: Consultar pontos → Resgatar pontos
   - **Estoque** (opcional, com token de gerente): Consultar estoque → Atualizar estoque
   - **Atualização de status**: Login como cozinha → atualizar para EM_PREPARO/PRONTO; login como atendente → atualizar para ENTREGUE
   - **Cancelamento**: Criar outro pedido e cancelar
   - **Cenários de erro**: Testar 401, 403, 409, 422 conforme descrito na coleção

Todos os cenários positivos e negativos estão contemplados na coleção.

## Logs
Os logs são exibidos no console com nível **DEBUG** para o pacote `com.raizes`.
