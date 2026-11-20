# Sistema de Gestão Acadêmica - POO

Sistema desenvolvido para a disciplina de **Programação Orientada a Objetos (POO)**. O projeto consiste em uma aplicação de terminal para gerenciamento completo de **Alunos**, **Cursos** e **Disciplinas**, estruturado sob a arquitetura **MVC + DAO** e integrado ao banco de dados relacional **MySQL** via Docker.

---

##  Objetivo do Projeto

Atender a todas as premissas acadêmicas propostas:
- **Gerenciamento Completo (CRUD):** Criar, Consultar, Atualizar e Remover Alunos, Cursos e Disciplinas.
- **Relacionamentos Fortes em POO:**
  - **Herança:** `Aluno` herda dados comuns de `Pessoa`.
  - **Associação:** Um `Aluno` pertence obrigatoriamente a um `Curso` existente.
  - **Composição:** Um `Curso` é composto por uma ou mais `Disciplinas`.
  - **Visualização Completa:** Ao consultar um aluno, exibe automaticamente seus dados, o curso vinculado e a grade de disciplinas daquele curso.
- **Persistência Dupla:** Manipulação de dados através de coleções em memória (`List<>`) e persistência física em banco de dados **MySQL**.

---

##  Arquitetura e Estrutura do Projeto

O código-fonte segue o padrão oficial da linguagem Java, dividido em pacotes com responsabilidades bem definidas:

```
Faculade/
 ├── infra/
 │   └── compose.yaml         # Configuração do MySQL 8 em container (limite < 512MB RAM)
 ├── libs/
 │   └── mysql-connector-j... # Driver JDBC de conexão com MySQL
 ├── Docs/
 │   ├── README.md            # Enunciado do trabalho
 │   ├── Exemplos/            # Exemplos do professor
 │   └── Diagrama/
 │       └── diagrama-classes.drawio  # Modelagem visual do sistema (UML 2.5)
 └── src/
     ├── Main.java            # Ponto de entrada (conecta banco e abre o menu)
     ├── model/               # Entidades de negócio (Pessoa, Aluno, Curso, Disciplina)
     ├── view/                # Entrada e saída via console (AlunoView, CursoView, etc.)
     ├── controller/          # Regras e intermediação entre View e DAO
     ├── dao/                 # Persistência SQL e controle de conexão (Singleton)
     └── menu/                # Menus e submenus interativos do console
```

---

##  Como Executar o Projeto no IntelliJ IDEA

### Pré-requisitos
- **Java JDK 17** ou superior configurado no IntelliJ.
- **Docker** e **Docker Compose** instalados e em execução.

### Passo a Passo

1. **Subir o Banco de Dados:**
   No terminal, suba o container do MySQL:
   ```bash
   docker compose -f infra/compose.yaml up -d
   ```
   *(O banco estará disponível em `localhost:3307`)*

2. **Abrir o Projeto no IntelliJ:**
   - Abra a pasta do projeto no IntelliJ IDEA.
   - O projeto já está configurado:
     - A pasta `src/` está marcada como raiz de código-fonte (**Sources Root**).
     - A biblioteca JDBC `libs/mysql-connector-j-9.1.0.jar` já está adicionada às dependências do módulo.

3. **Executar a Aplicação:**
   - Localize o arquivo `src/Main.java`.
   - Clique com o botão direito no arquivo (ou no ícone verde de **Play** ao lado do método `main`) e selecione **Run 'Main'**.
   - O sistema iniciará diretamente na aba **Run / Terminal** do IntelliJ.

---

##  Navegação no Sistema

### Menu Principal
```text
==================================
     SISTEMA ACADÊMICO - POO      
==================================
1 - Gerenciar ALUNOS
2 - Gerenciar DISCIPLINAS
3 - Gerenciar CURSOS
4 - SAIR
==================================
```

### Regra de Cadastro
Para respeitar a integridade referencial:
1. Cadastre primeiro pelo menos um **Curso** (Menu `3` ➔ Opção `1`).
2. Cadastre as **Disciplinas** vinculando-as ao curso criado (Menu `2` ➔ Opção `1`).
3. Cadastre os **Alunos** vinculando-os ao curso desejado (Menu `1` ➔ Opção `1`).

---

##  Conceitos de POO e Boas Práticas Utilizados

- **Encapsulamento:** Atributos declarados como `private` ou `protected`, acessíveis via getters e setters com validações.
- **Herança:** A classe abstrata `Pessoa` concentra nome e idade, herdados por `Aluno` via `extends`.
- **Padrão MVC (Model-View-Controller):** Separação estrita entre dados (`model`), telas de interação (`view`) e regras de orquestração (`controller`).
- **Padrão DAO (Data Access Object):** Centraliza toda a manipulação SQL e mapeamento objeto-relacional.
- **Padrão Singleton:** Empregado em `DatabaseConnection` para manter uma única conexão ativa e otimizada com o banco.
- **Segurança com PreparedStatement:** Prevenção contra injeção de SQL (`SQL Injection`) em todas as operações de banco.
- **Tratamento de Exceções:** Validação de entradas no terminal através do `InputHelper`, evitando quebras de fluxo por tipos de dados incorretos.

---

##  Autor
Arthur Henrique.# Faculade
