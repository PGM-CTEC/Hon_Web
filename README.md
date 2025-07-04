## Api de Gerenciamento de Honorários Advocatícios de Procuradores da PGM-RJ

### Antes de mais nada, é necessário clonar o repositório para o seu repositório local, através do seguinte comando no diretório que você deseja:

```
git clone https://github.com/PGM-CTEC/Hon_Web.git
```

Logo em seguida vá até o diretório raiz onde o repositório foi clonado e abra ele no VSCode.

## 1) application.properties 

Agora configure o arquivo application.properties e certifique-se de que seu banco de dados está devidamente configurado e rodando.
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

## 2) Como rodar (VSCODE)

2.1) 
Vá até o diretório raiz e rode o seguinte comando no terminal:
```
mvn spring-boot:run
```
ou

2.2) 
Instale a seguinte dependência: 
![image](https://github.com/user-attachments/assets/04e2e5b0-3d21-4390-8588-f026ae09b763)

Depois disso, vá até o arquivo UhscopeApplication.java e clique em "run"
![image](https://github.com/user-attachments/assets/55b48b33-78f0-43b2-bf9e-e219c7ac06cc)


### Pronto, agora a API está rodando na sua máquina na porta que foi configurada no arquivo application.properties



## 3) Frontend

Basicamente clique em algum arquivo HTML dentro do diretório frontend/assets/pages
![image](https://github.com/user-attachments/assets/a7e18ea3-6368-433d-8260-16abf9192664)

ou então instale a seguinte extensão
![image](https://github.com/user-attachments/assets/78faf8da-0350-4e9e-affd-4cb7e35cba85)

e logo em seguida clique em "Go Live" no menu inferior do VSCode

![image](https://github.com/user-attachments/assets/61277cd3-e7af-4b2f-b69d-49311633b8a0)

Pronto, agora é possível usar a aplicação com o frontend consumindo a API.

![image](https://github.com/user-attachments/assets/7e9a25a8-064e-4370-8ff1-92449a22bd3b)

## Adendo:
A aplicação ainda está em desenvolvimento











