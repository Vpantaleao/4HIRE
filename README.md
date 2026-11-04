# 4Hire

Plataforma de prestadores de serviço e orçamentos (Programação Web).

## Requisitos
- JDK 21
- PostgreSQL

## Como rodar
1. Clone o repositório e abra a pasta `projeto-web` no IntelliJ.
2. Crie o banco no PostgreSQL: `CREATE DATABASE "4hire_db";`
3. Configure o acesso ao banco. O `application.properties` está preparado para ler
   o usuário e a senha de variáveis de ambiente, com `postgres`/`postgres` como padrão:
   - **Se as variáveis estiverem configuradas na IDE** (`DB_USER` e `DB_PASSWORD` em
     *Run > Edit Configurations > Environment variables*), o arquivo usa esses valores
     e você não precisa mexer em nada.
   - **Se não estiverem configuradas**, e seu usuário/senha do PostgreSQL forem
     diferentes de `postgres`/`postgres`, altere manualmente os valores padrão em
     `src/main/resources/application.properties` (sem enviar sua senha real para o GitHub).
4. Rode a classe `ProjetoWebApplication`.
5. A API sobe em http://localhost:8080
