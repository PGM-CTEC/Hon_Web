### Api de Gerenciamento de Honorários Advocatícios de Procuradores da PGM-RJ

## 1) application.properties 


```
spring.application.name=uhscope

# Configurando banco de dados (MySQL)
spring.datasource.url="Insira o url do seu banco de dados"
spring.datasource.username="Insira o username do seu banco de dados"
spring.datasource.password="Insira a sua senha do seu banco de dados"
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configurando o Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Configurando o servidor
server.port="Insira a porta aqui"

# Token
security.token.secret="Insira uma chave secreta aqui"
```
