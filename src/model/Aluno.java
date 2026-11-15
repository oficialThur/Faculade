package model;

/**
 * Representa um Aluno da faculdade.
 * Herda nome e idade de Pessoa e possui uma associação N..1 com Curso.
 */
public class Aluno extends Pessoa {

    private int matricula;
    private Curso curso;

    public Aluno() {
        super();
    }

    public Aluno(int matricula, String nome, int idade) {
        super(nome, idade);
        this.matricula = matricula;
    }

    public Aluno(int matricula, String nome, int idade, Curso curso) {
        super(nome, idade);
        this.matricula = matricula;
        this.curso = curso;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "matricula=" + matricula +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                ", curso=" + (curso != null ? curso.getNome() : "Sem curso") +
                '}';
    }
}
