# 12-Week-Method

## Link de acesso API:
https://12-week-method.up.railway.app/

## Como Rodar

O projeto utiliza um banco de dados PostgreSQL. Para executar localmente, siga as etapas abaixo:

1. Abra o arquivo de configuração: **"12WeekMethod\src\main\resources\application-dev.properties"**.

2. Modifique as seguintes configurações com as informações do seu banco de dados PostgreSQL:
   - Usuário PostgreSQL
   - Senha PostgreSQL
     
```
## Conexão com o banco de dados
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/12-week-method
spring.datasource.username= Seu Usuário
spring.datasource.password= Sua Senha
```

3. Crie uma base de dados no PostgreSQL com o nome **12-week-method**.

4. No terminal, execute o seguinte comando para construir o projeto:

```bash
mvn clean install

mvn spring-boot:run
```

Parabéns a aplicação subiu localmente na porta 8080.

## Descrição da API

A API 12-Week-Method é baseada na metodologia descrita no livro "1 Ano Em 12 Semanas" de Brian P. Moran e Michael Lennington. A metodologia envolve a ideia de definir prazos mais curtos para alcançar nossos objetivos. Nessa abordagem, os objetivos são divididos em diferentes camadas. A camada principal é o objetivo principal (Goal) que buscamos alcançar em um período de 12 semanas, o que se assemelha a um objetivo que estabeleceríamos para um ano inteiro. Por exemplo, um objetivo principal poderia ser perder peso.

Dentro de cada semana das 12 semanas, definimos cartões semanais (WeekCard) que nos ajudam a progredir em direção ao objetivo principal. Esses cartões semanais consistem em metas alcançáveis para aquela semana. Por exemplo, uma meta para uma semana pode ser perder 1,5kg. Dentro do cartão, definimos resultados-chave (KeyResults) que desejamos alcançar, bem como tarefas (Task) que nos ajudarão a atingir esses resultados-chave.

A API 12-Week-Method permite que os usuários criem, gerenciem e acompanhem seus objetivos, cartões semanais, resultados-chave e tarefas dentro do período de 12 semanas. Seguindo essa metodologia, os usuários podem focar em metas de curto prazo, manter a motivação e progredir consistentemente em direção a seus objetivos gerais.

**Observação: A implementação da API está alinhada com os princípios descritos no livro, mas não possui afiliação com o autor ou a organização oficial do "1 Ano Em 12 Semanas".**

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Data JPA (Interface de repositório)
- Spring Security (JWT Token)
- PostgreSQL (Banco de Dados)
- JUnit (Testes unitários)

## Organização Banco de Dados

![alt image](/Assets/ER%2012%20Week%20Method.png)
