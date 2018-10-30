# API REST para geração de boletos


## Recursos usados no Projeto
### Linguagem de Programação
Java com recursos da versão 8

### SpringBoot
O Spring Boot é um projeto da Spring que facilita o processo de configuração e publicação de aplicações. 
Ele favorece a convenção sobre a configuração.
Basta que seja informado quais módulos a que se utilizar deseja utilizar 
(WEB, Template, Persistência, Segurança, etc.) que ele reconhece e configura.

O maior benefício do Spring Boot é que ele permite focar mais regras de negócio da aplicação.

O start do projeto foi feito usando o SPRING INITIALIZR https://start.spring.io/.
O módulo usandos foram Web, Lombok e DevTools e gerador de depencencias Apache Maven.

### Lombok
O Lombok é um Framework criado sob licença MIT, podendo ser usado livremente em qualquer projeto Java. 
Sua vantagem é evitar a repetição de código "clichê", como a criação de gets e sets para todos os atributos, 
métodos equals e hashCode, toString, Construtores entre outros. Dessa forma, o código fica mais limpo e claro.
Compatível somente com as IDEs Eclipse, IntelliJ IDEA e Netbeans.

### DevTools

DevTools é um módulo do Spring Boot que adiciona algumas ferramentas que são interessantes em tempo de desenvolvimento. 
Oferece configuração de propriedades úteis para desenvolvimento, reinicialização automática do servidor,
 e o LiveReload que envia um aviso para o navegador dizendo que os arquivos estáticos ou os de templates foram alterados.

### Apache Maven
O Maven é uma ferramenta desenvolvida pela Apache para gerenciamento de dependências e automatização de builds.

### Docker Maven Plugin
É um plugin desenvolvido pela Spotify que por meio do Maven facilita a dockerização de uma aplicação SpringBoot.

### Swagger
O Swagger é um projeto composto por algumas ferramentas que auxiliam o desenvolvedor de APIs REST em algumas tarefas.
Nesse projeto ele foi usado para geração de documentação (legível) da API.

### H2 Database Engine
H2 é um banco de dados relacional escrito em Java. Ele pode ser embarcado em aplicações Java ou rodar no modo cliente-servidor. Ele é carregado diretamente na memória, é open source e suporta a JDBC API.

## Instruções para executar a aplicação

### Acesso

Por padrão a aplicação irá rodar na porta 8080.
Pode ser parametrizavel no profile de test (/bankslip/src/main/resources/application-test.properties) 
ou de produção (/bankslip/src/main/resources/application.properties) 
por meio da propriedade: server.port=8080 <- porta definida como exemplo.

A url base para requisições no serviço (endponit) é http://nome_do_servidor_ou_dominio_dns:porta/rest/bankslips. 
Exemplo: http://localhost:8080/rest/bankslips

### Documentação da API
Para visualizar a documentação da API acesse: url_base_da_API/swagger-ui.html#/
Exemplo: http://localhost:8080/swagger-ui.html#/

## Build do projeto

### Maven

#### Build do arquivo JAR
Para fazer um build simples e gerar o arquivo .jar do serviço, acesse por meio do prompt de comandos do seu sistema operacional  o diretório base do projeto por exemplo (windows): D:\Dev\workspace\bankslip

Execute o comando:-> mvn install

Após executar o comando acima o maven irá fazer o build, executará todos os testes e ao final é esperado a mensagem "BUILD SUCCESS". O arquivo .jar será gerado no diretório /bankslip/target/.

#### Build do Docker file utilizando o docker-maven-plugin
Para fazer um build gerando os arquivos .jar do serviço e o Dockerfile, há duas maneiras de fazer.
Acesse por meio do prompt de comandos do seu sistema operacional o diretório base do projeto por exemplo(windows): D:\Dev\workspace\bankslip 

Execute o comando:-> mvn clean package docker:build

Após executar o comando acima o maven irá fazer o build, executará todos os testes e ao final no último goal apresentará uma mensagem de erro "Failed to execute goal com.spotify:docker-maven-plugin:1", mas mesmo assim tanto o arquivo .jar quanto o Docker file serão gerados no diretório /bankslip/target/docker. A mensagem ocorre porque o maven foi executado fora de um ambiente Docker, caso tivesse sito executado de um Docker, o maven faria o upload da imagem para o docker automaticamente.


### Inicialização


#### Diretórios do projeto

  /bankslip/ -> diretório base do projeto.
  
  /bankslip/target/ -> diretório onde os arquivos que foram gerados pelo maven se encontram.
            
#### Windows e Linux - inicialização simples por meio de commandos

Para inicializar a aplicação no Windows ou Linux, é necessário ter o Java 8 Runtime instalado no sistema operacional. 
O arquivo JAR está disponível no diretório do projeto em /target/bankslips-1.0.0.jar. É necessário abrir o prompt de comandos do windows ou linux (bash) e navegar até onde o arquivo está salvo (/bankslip/target/).

Executar o comando: "java -jar bankslips-1.0.0.jar"

Feito isso a aplicação será inicializada.

Observação!
É necessário que o sistema operacional (Windows ou Linux) tenham configuradas as variáveis de ambiente para reconhecer o comando "java". Do contrário é necessário procurar o arquivo binário do java.

### Docker (Avançado)

Seguindo o a documentação, estarão disponiveis em /target/docker os arquivos Dockerfile e bankslips-1.0.0.jar para deploy em ambientes Docker.

Nevegue pelo terminal do Docker até o local onde se encontra os arquivos (sua escolha onde salvá-los).
 
Execute o comando: "docker image build . --tag bankslips-1.0.0.jar"

Feito isso o docker irá subir para seu servidor a imagem enviada.

Também irá fazer o download da imagem openjdk:8-jdk-alpine necessária para o container.

Se tudo ocorrer bem um log de sucesso como esse irá surgirá ao final:


Sending build context to Docker daemon 41.9MB

Step 1/3 : FROM openjdk:8-jdk-alpine

 ---> 4987f7d001a3
Step 2/3 : ADD /bankslips-1.0.0.jar //

 ---> 11g2ff32i7cd

Step 3/3 : ENTRYPOINT ["java", "-jar", "bankslips-1.0.0.jar"]

 ---> Running in def452f0c698

Removing intermediate container def452f0c698

 ---> 671a24548796
Successfully built 671a24548796

Successfully tagged bankslips-1.0.0


Em seguida execute o comando: docker run -it -p 9999:80 bankslips-1.0.0.jar

Obs.: A porta 9999 no comando anterior é uma opção para expor a aplicação numa porta diferente da porta 8080 caso isso seja necessário.

Em seu ambiente Docker a url de acesso da api poderá ficar assim: http://192.168.99.100:9999/rest/bankslips
