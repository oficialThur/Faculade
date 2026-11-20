# Apresentação — Sistema Acadêmico (POO)

Documento de apoio para a apresentação do trabalho. Aqui está o que o sistema faz, como ele está organizado por dentro, um roteiro de demonstração ao vivo e as perguntas que o professor costuma fazer.

---

## 1. O que é o projeto (resumo em 30 segundos)

É um sistema de terminal que gerencia **Alunos**, **Cursos** e **Disciplinas** de uma faculdade. Roda em Java, salva tudo em **MySQL** (subido com Docker) e foi construído separando cada responsabilidade em uma camada diferente, seguindo os padrões **MVC** e **DAO**.

Pontos que o trabalho precisava cumprir:

- Menu com submenus de Aluno, Disciplina e Curso, cada um com **cadastrar, consultar, remover e atualizar**.
- Todo **Aluno** tem um **Curso** escolhido entre os já existentes.
- Todo **Curso** tem uma ou mais **Disciplinas**.
- Ao consultar um aluno, o sistema mostra o curso dele **e todas as disciplinas**.
- Só sai do programa na opção **SAIR**.
- Usa **listas** para guardar objetos e **banco de dados** para persistir.

---

## 2. Como o sistema funciona por dentro (a analogia do restaurante)

Pensa num restaurante. Cada parte do sistema é uma pessoa com uma função bem específica:

| Camada | Quem seria | O que faz no sistema |
|---|---|---|
| **Menu** | O cardápio | Mostra as opções e espera você escolher |
| **View** | O garçom | Só conversa com você: pergunta os dados e mostra os resultados |
| **Controller** | O gerente | Decide o que fazer, valida as regras e chama quem precisa |
| **DAO** | O estoquista | Guarda e busca as coisas no depósito (banco) |
| **Model** | A ficha de cadastro | Guarda os dados em si (aluno, curso, disciplina) |
| **DatabaseConnection** | A chave do depósito | Uma única chave compartilhada por todos |

O garçom (View) **nunca** entra no depósito. Ele fala com o gerente (Controller), que fala com o estoquista (DAO). Isso evita bagunça: se amanhã o depósito mudar de lugar, só o estoquista precisa aprender o caminho novo.

```
Usuário → Menu → Controller → View (pergunta/mostra)
                          ↘ DAO → Banco de dados
```

---

## 3. Estrutura de pastas

```
Faculade/
 ├── infra/
 │   └── compose.yaml              # MySQL 8 em Docker (porta 3307, limite de 512 MB)
 ├── libs/
 │   └── mysql-connector-j-9.1.0.jar
 ├── Docs/
 │   ├── README.md                 # Enunciado do trabalho
 │   ├── Exemplos/                 # Exemplos do professor
 │   └── Diagrama/
 │       └── diagrama-classes.drawio  # Diagrama UML 2.5
 └── src/
     ├── Main.java                 # Ponto de entrada
     ├── model/                    # Pessoa, Aluno, Curso, Disciplina
     ├── view/                     # AlunoView, CursoView, DisciplinaView, InputHelper
     ├── controller/               # AlunoController, CursoController, DisciplinaController
     ├── dao/                      # AlunoDAO, CursoDAO, DisciplinaDAO, DatabaseConnection
     └── menu/                     # Menu e os três Submenus
```

**Onde está cada coisa** (para achar rápido se o professor pedir):

| O professor pede | Abra |
|---|---|
| "Mostra a herança" | `src/model/Pessoa.java` e `src/model/Aluno.java` |
| "Mostra a composição Curso–Disciplina" | `src/model/Curso.java` (`adicionarDisciplina`) |
| "Como salva no banco?" | `src/dao/AlunoDAO.java` (`Create`) |
| "Como conecta no banco?" | `src/dao/DatabaseConnection.java` |
| "Onde está o menu?" | `src/menu/Menu.java` |
| "Onde trata entrada inválida?" | `src/view/InputHelper.java` |

---

## 4. Os Models — onde mora a POO

### 4.1 Herança: `Pessoa` → `Aluno`

`Pessoa` é **abstrata**: ela não existe sozinha no sistema, serve só de base. Guarda o que é comum a qualquer pessoa (`nome` e `idade`), e o `Aluno` herda isso e adiciona o que é só dele (`matricula` e `curso`).

```java
public abstract class Pessoa {
    protected String nome;
    protected int idade;
}

public class Aluno extends Pessoa {
    private int matricula;
    private Curso curso;

    public Aluno(int matricula, String nome, int idade) {
        super(nome, idade);   // reaproveita o construtor da mãe
        this.matricula = matricula;
    }
}
```

> **Por que `protected` e não `private`?** Porque o filho (`Aluno`) precisa acessar `nome` e `idade` diretamente. Para o resto do mundo, continua encapsulado.

> **Por que `abstract`?** Porque não faz sentido criar uma "Pessoa" solta no sistema. Só existem alunos. Se alguém tentar `new Pessoa()`, o Java nem deixa compilar.

### 4.2 Associação: `Aluno` tem 1 `Curso`

```java
private Curso curso;   // referência ao objeto inteiro, não só o código
```

Guardar o objeto `Curso` (e não um `int cursoCodigo`) é o que permite, na hora de mostrar o aluno, acessar `aluno.getCurso().getDisciplinas()` de uma vez.

### 4.3 Composição: `Curso` tem várias `Disciplinas`

```java
public class Curso {
    private List<Disciplina> disciplinas = new ArrayList<>();

    public void adicionarDisciplina(Disciplina d) {
        this.disciplinas.add(d);
        if (d.getCurso() != this) {
            d.setCurso(this);   // mantém os dois lados da relação coerentes
        }
    }
}
```

Esse método é o ponto mais interessante do projeto: ele mantém a relação **bidirecional**. Se eu adiciono uma disciplina no curso, a disciplina passa a apontar de volta para o curso automaticamente. Sem isso, os dados ficariam inconsistentes.

> **Composição × associação:** no diagrama, o losango cheio do lado de `Curso` significa que a disciplina faz parte do curso. Já o aluno apenas *está ligado* a um curso (seta simples).

### 4.4 Onde entra "LISTAS" (premissa g)

Cada DAO tem uma lista estática que serve de espelho dos dados do banco:

```java
public static List<Aluno> alunoList = new ArrayList<>();
```

Ela é preenchida e devolvida pelo `ListAll()`, que busca tudo no banco:

```java
public static List<Aluno> ListAll() {
    // ... percorre os códigos vindos do banco ...
    alunoList = resultado;
    return alunoList;
}
```

Assim o trabalho atende **as duas premissas ao mesmo tempo**: usa `List<>` (item g) e usa banco de dados (item i).

---

## 5. Os DAOs — a conversa com o MySQL

### 5.1 Conexão única (`DatabaseConnection`)

Usa o padrão **Singleton**: existe uma única conexão aberta para o programa inteiro, criada só na primeira vez que alguém pede.

```java
if (connection == null) {
    synchronized (DatabaseConnection.class) {   // evita criar duas em threads diferentes
        if (connection == null) {
            connection = DriverManager.getConnection(HOST + DATABASE, DB_USER, DB_PASS);
        }
    }
}
```

> **Por que não abrir uma conexão a cada consulta?** Abrir conexão é caro (handshake, autenticação). É como ligar para o depósito toda vez que você quer uma caneta.

### 5.2 SQL sempre com `?` (`PreparedStatement`)

```java
String sql = "INSERT INTO alunos (matricula, nome, idade, curso_codigo) VALUES (?, ?, ?, ?)";
stmt.setInt(1, aluno.getMatricula());
stmt.setString(2, aluno.getNome());
```

Os `?` são preenchidos depois, e o driver trata cada valor como **dado**, nunca como comando. Sem eles, alguém digitando `' OR 1=1 --` no nome conseguiria mexer no banco. É a proteção contra **SQL Injection**.

### 5.3 Tabelas e relacionamentos

```sql
cursos      (codigo PK, nome, turno)
disciplinas (codigo PK, nome, carga_horaria, curso_codigo FK → cursos)
alunos      (matricula PK, nome, idade, curso_codigo FK → cursos)
```

**Por que a ordem de criação importa:** `disciplinas` e `alunos` apontam para `cursos` com **chave estrangeira**. Não dá para criar uma tabela que referencia outra que ainda não existe. Por isso no `Main` a ordem é:

```java
CursoDAO.CreateTable();
DisciplinaDAO.CreateTable();
AlunoDAO.CreateTable();
```

### 5.4 A consulta que atende o item c-ii

Quando você consulta um aluno, o `AlunoDAO.Read()` monta o objeto completo:

```java
Aluno aluno = new Aluno(matricula, nome, idade);

Optional<Curso> curso = CursoDAO.Read(rs.getInt("curso_codigo"));
curso.ifPresent(aluno::setCurso);   // curso já vem com as disciplinas anexadas

return Optional.of(aluno);
```

E o `CursoDAO.Read()` por sua vez chama `DisciplinaDAO.ListByCurso()` e anexa tudo na lista `disciplinas` do curso. Resultado: **um único `Read` no aluno traz aluno + curso + todas as disciplinas**, que é exatamente o que o enunciado pede.

> **Detalhe importante:** o `ListByCurso` devolve as disciplinas **sem** o curso preenchido de propósito. Se ele preenchesse, o `CursoDAO` chamaria `DisciplinaDAO`, que chamaria `CursoDAO` de novo... e viraria um laço infinito.

### 5.5 Exclusão protegida

```java
public static boolean Delete(int codigo) {
    try {
        return stmt.executeUpdate() > 0;   // true só se apagou de verdade
    } catch (SQLIntegrityConstraintViolationException e) {
        System.out.println("Não é possível remover: existem disciplinas ou alunos neste curso!");
        return false;
    }
}
```

Diferente do exemplo do professor, que sempre devolvia `true`, aqui o retorno é real. Além disso, se o curso tiver filhos, o banco barra pela chave estrangeira e o sistema avisa em vez de quebrar.

---

## 6. Views e Controllers — a dupla que conversa com o usuário

### 6.1 View: só pergunta e mostra

`AlunoView.Show()` é o trecho que prova o item c-ii:

```java
System.out.println("Matrícula: " + aluno.getMatricula());
System.out.println("Nome:      " + aluno.getNome());

Curso curso = aluno.getCurso();
System.out.println("CURSO: [" + curso.getCodigo() + "] " + curso.getNome());

for (Disciplina d : curso.getDisciplinas()) {
    System.out.println("  - [" + d.getCodigo() + "] " + d.getNome());
}
```

Nenhuma decisão de negócio mora aqui. Só formatação e `Scanner`.

### 6.2 Controller: valida antes de salvar

No `AlunoController.Create()`:

```java
List<Curso> cursos = CursoDAO.ListAll();
if (cursos.isEmpty()) {
    AlunoView.Message("Nenhum curso cadastrado! Cadastre um curso primeiro.");
    return;                       // early return: sai logo, sem aninhar if
}
```

Aqui está a regra do enunciado: **não existe aluno sem curso**, e o curso precisa existir de verdade. O mesmo vale para disciplina.

### 6.3 O `InputHelper`: bugs do exemplo do professor que foram corrigidos

| Problema no exemplo original | Como foi resolvido |
|---|---|
| `scan.next()` cortava nomes compostos ("Ana Maria" virava "Ana") | `nextLine()` + `trim()` |
| `nextInt()` seguido de `nextLine()` pulava a leitura | Tudo é lido como linha e convertido depois |
| Digitar letra onde se espera número quebrava o programa | `try/catch` com `NumberFormatException` e nova tentativa |
| Vários `new Scanner` espalhados | Um único `Scanner` estático |

---

## 7. Menu e Submenus

O `Menu` principal usa `do...while` justamente para atender a premissa **f** (só sair na opção SAIR):

```java
do {
    // mostra as opções
    opcao = InputHelper.readInt("Opção: ");
    switch (opcao) {
        case 1 -> SubmenuAluno.Show();
        case 2 -> SubmenuDisciplina.Show();
        case 3 -> SubmenuCurso.Show();
        case 4 -> System.out.println("Encerrando o sistema. Até logo!");
        default -> System.out.println("Opção inválida! Escolha entre 1 e 4.");
    }
} while (opcao != 4);
```

Duas diferenças em relação ao exemplo do professor:

- `!= 4` em vez de `< 4` (senão digitar `9` repetia o menu).
- `default` para avisar sobre opção inválida em vez de ignorar.

---

## 8. Roteiro de demonstração ao vivo

Rode o `Main` e siga esta ordem (obrigatória por causa das chaves estrangeiras):

### Passo 1 — Banco no ar
```bash
docker compose -f infra/compose.yaml up -d
```

### Passo 2 — Cadastrar um curso
`3` → `1`
```
Código do curso: 1
Nome do curso: Analise e Desenvolvimento de Sistemas
Turno: Noturno
```

### Passo 3 — Cadastrar duas disciplinas nesse curso
`2` → `1`
```
Código da disciplina: 10
Nome da disciplina: Programacao Orientada a Objetos
Carga horaria: 80
Informe o codigo do curso: 1
```
Repita com `11` / `Banco de Dados` / `60` / curso `1`.

### Passo 4 — Cadastrar um aluno
`1` → `1`
```
Matricula: 2024001
Nome completo: Ana Maria de Souza
Idade: 20
Informe o codigo do curso: 1
```

### Passo 5 — A consulta que fecha o trabalho
`1` → `2` → `2024001`

Saída esperada:
```
===========================================
DADOS DO ALUNO
-------------------------------------------
Matrícula: 2024001
Nome:      Ana Maria de Souza
Idade:     20

CURSO: [1] Analise e Desenvolvimento de Sistemas (Noturno)
Disciplinas da grade (2):
  - [10] Programacao Orientada a Objetos (80h)
  - [11] Banco de Dados (60h)
===========================================
```

### Passo 6 — Mostrar o banco direto
```bash
docker exec -it faculdade-mysql mysql -uroot -proot faculdade -e "SELECT * FROM alunos;"
```

Isso prova que não é só tela: o dado está persistido.

### Passo 7 — Mostrar as validações (pode impressionar)
- Tentar cadastrar aluno com curso `999` → "Curso não encontrado! Cadastro cancelado."
- Tentar remover o curso `1` → "Não é possível remover: existem disciplinas ou alunos neste curso!"
- Digitar `abc` em vez de número → "Entrada inválida! Digite um número inteiro."

### Passo 8 — Sair com `4`
Mostra que o `finally` fecha a conexão: `Conexão fechada.`

---

## 9. Perguntas prováveis do professor

**1. O que é encapsulamento e onde você usou?**
É esconder os dados e só permitir acesso por métodos controlados. Todos os atributos são `private` (ou `protected` quando o filho precisa) e o acesso é via get/set.

**2. Por que a View não fala direto com o DAO?**
Porque cada camada tem uma responsabilidade. A View cuida de tela, o Controller cuida de regra de negócio. Se amanhã a regra mudar (ex.: aluno precisa ter CPF), mexo só no Controller.

**3. Qual a diferença entre herança e composição no seu projeto?**
`Aluno` **herda** de `Pessoa` (é uma pessoa). `Curso` **compõe** com `Disciplina` (tem disciplinas dentro dele).

**4. Se eu apagar o curso, o que acontece com as disciplinas?**
Hoje o banco **barra** a exclusão, porque `disciplinas` e `alunos` têm chave estrangeira apontando para `cursos`. O `Delete` captura essa exceção e avisa o usuário. Se eu quisesse apagar em cascata, usaria `ON DELETE CASCADE`.

**5. Por que `Optional` em vez de devolver `null`?**
Porque `Optional` obriga quem chama a pensar no "não encontrei". Com `null`, é fácil esquecer e tomar `NullPointerException`.

**6. O que aconteceria se eu colocasse `String sql = "SELECT * FROM alunos WHERE nome = " + nome;`?**
Qualquer pessoa poderia digitar um trecho de SQL no campo e alterar a consulta. É o **SQL Injection**. Por isso uso `PreparedStatement` com `?`.

**7. Por que a conexão é estática?**
Por causa do padrão Singleton: uma única conexão para o programa todo. Se cada tela abrisse uma conexão nova, o banco ficaria sobrecarregado.

**8. Por que você não usou `nextInt()` como no exemplo?**
Porque ele tem dois problemas: corta textos com espaço (`next()` também) e deixa o `\n` no buffer. O `InputHelper` lê linha inteira e converte.

**9. Isso é MVC mesmo?**
É uma variação de MVC com DAO, comum em Java. Model = classes de dados; View = entrada/saída de console; Controller = orquestração; DAO = persistência.

**10. Se eu quisesse adicionar "Professor" no sistema, o que você mexeria?**
Criaria `model/Professor.java` herdando de `Pessoa`, depois `ProfessorDAO`, `ProfessorView`, `ProfessorController` e um `SubmenuProfessor`. Nada nas classes de Aluno precisaria mudar — é o ganho de separar por camadas.

---

## 10. Conceitos de POO usados (resumo para citar)

| Conceito | Onde aparece |
|---|---|
| **Abstração** | `Pessoa` como `abstract class` |
| **Encapsulamento** | Atributos `private`/`protected` + getters/setters |
| **Herança** | `Aluno extends Pessoa` |
| **Polimorfismo** | Sobrescrita de `toString()` nas classes de modelo |
| **Associação** | `Aluno → Curso` |
| **Composição** | `Curso ◆→ Disciplina` |
| **Coleções** | `List<Disciplina>` em `Curso`, listas estáticas nos DAOs |
| **Exceções** | `try/catch` com `SQLException` e `NumberFormatException` |
| **Padrões** | MVC, DAO, Singleton |

---

## 11. Comandos úteis durante a apresentação

```bash
# Subir o banco
docker compose -f infra/compose.yaml up -d

# Ver se está rodando
docker ps

# Consultar as tabelas direto no MySQL
docker exec -it faculdade-mysql mysql -uroot -proot faculdade -e "SHOW TABLES;"

# Ver os alunos gravados
docker exec -it faculdade-mysql mysql -uroot -proot faculdade -e "SELECT * FROM alunos;"

# Derrubar o banco (mantém os dados no volume)
docker compose -f infra/compose.yaml down
```

Para rodar o sistema: abra `src/Main.java` no IntelliJ e clique no ▶ verde.

---

## 12. Frase de fechamento

> "O sistema resolve o problema do enunciado, mas o que ele realmente demonstra é organização: cada classe tem uma função só, as regras de negócio ficam nos controllers, o acesso a dados fica nos DAOs e o modelo reflete as relações do mundo real. Se amanhã eu trocar o MySQL por PostgreSQL ou por memória, só a camada DAO muda — o resto continua igual."
