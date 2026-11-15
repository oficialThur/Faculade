package model;

/**
 * Representa uma Disciplina lecionada na faculdade.
 * Toda disciplina pertence obrigatoriamente a um Curso específico.
 */
public class Disciplina {

    private int codigo;
    private String nome;
    private int cargaHoraria;
    private Curso curso;

    public Disciplina() {
    }

    public Disciplina(int codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public Disciplina(int codigo, String nome, int cargaHoraria, Curso curso) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.curso = curso;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria + "h" +
                ", curso=" + (curso != null ? curso.getNome() : "Sem curso") +
                '}';
    }
}
