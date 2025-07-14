# 🚀 Teste - API cadastro de infrações

API REST em Spring Boot responsável por registrar infrações de trânsito com suporte a autenticação via JWT.

---

## Índice

- [Como Rodar](#-como-rodar)
- [Endpoints](#-endpoints-principais)
- [Considerações](#-consideracoes)
- [Qual foi o aspecto mais difícil deste desafio e por quê?](#-qual-foi-o-aspecto-mais-dificil-deste-desafio-e-por-que)

---

## Como Rodar

```bash

# Clone o projeto
git clone https://github.com/mvismari/teste-infracoes.git

# Compile o projeto
mvn clean package

# (opcional) Relatório em HTML de cobertura de testes
# Basta abrir o relatório no navegador target/site/jacoco/index.html
mvn jacoco:report 

# Rode o comando abaixo para subir os containers
docker compose up —build

# Para acessar o Swagger
http://localhost:5005/swagger-ui.html

```

---

## Endpoints principais

Auth:
- [GET] http://localhost:5005/auth

Equipments:
- [GET] http://localhost:5005/equipments/:serial
- [GET] http://localhost:5005/equipments?page=0&size=10
- [POST] http://localhost:5005/equipments

Violations:
- [GET] http://localhost:5005/equipments/:serial/violations?page=0&size=5
- [GET] http://localhost:5005/violations/:id
- [POST] http://localhost:5005/violations

---

## Considerações

   1. Para o endpoint da violação que recebe uma PICTURE, como o arquivo é pequeno (<1 MB) a solução é usar um serviço de nuvem (AWS EFS - Elastic File System) para ter um diretório compartilhado entre N pods;
   
   2. O problema proposto possui apenas 2 domínios (equipamentos e violações). E também deve ser um microserviço. Ao invés de implementar uma arquitetura "verbosa" e que demanda muito mais esforço e tempo para poucas features, usei conceitos da mesma (port/adapter) juntamente da Vertical Slice para manter cada feature bem separada. Porém, como são poucos endpoints por feature e poucos arquivos, não vejo razão para dividir por endpoint (que seria o conceito à risca dessa última);

   3. Se esse fosse um projeto real e o número de requisições aumentasse absurdamente, o esforço seria:

      3.1 Equipamentos não possuem atualização frequente, poderíamos manter;

      3.2 Infrações poderiam serem mantidas nos arquivos usando EFS e as informações serem enviadas para uma fila (SQS, RabbitMQ ou Kafka) para não exceder a capacidade do banco (conexões e IO);

      3.3 Ainda existe a possibilidade de gerar um link dinamicamente diretamente de um serviço de nuvem (S3 ou Cloud Storage) para o upload acontecer diretamente no storage da nuvem. Dessa forma a infração só receberia a url da PICTURE;

      3.4 Implementando o adapter, herdando a mesma interface do repositório, o sistema teria o mesmo comportamento e demandaria menos tempo para alteração.

---

## Qual foi o aspecto mais difícil deste desafio e por quê?

O maior aspecto quando lidamos com desafios, é supor informações para poder modelar banco de dados e buscar soluções para aplicação que sejam simples e diretas. As vezes optar pelo simples é o caminho que faz mais sentido. Por exemplo, para esse desafio foi necessário levantar o número de multas aplicadas no país, anualmente, para poder definir no banco de dados se a escolha de um ID inteiro atenderia (limitadadamente) ou se seria necessário um UUID (infinito, porém consome muito disco). 


