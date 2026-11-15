package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Curso da faculdade.
 * Possui uma composição 1..* com Disciplina.
 */
public class Curso {

    private int codigo;
    private String nome;
    private String turno;
    private List<Disciplina> disciplinas;

    public Curso() {
        this.disciplinas = new ArrayList<>();
    }

    public Curso(int codigo, String nome, String turno) {
        this.codigo = codigo;
        this.nome = nome;
        this.turno = turno;
        this.disciplinas = new ArrayList<>();
    }

    public Curso(int codigo, String nome, String turno, List<Disciplina> disciplinas) {
        this.codigo = codigo;
        this.nome = nome;
        this.turno = turno;
        this.disciplinas = (disciplinas != null) ? disciplinas : new ArrayList<>();
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

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public void adicionarDisciplina(Disciplina d) {
        if (d != null) {
            this.disciplinas.add(d);
            if (d.getCurso() != this) {
                d.setCurso(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Curso{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", turno='" + turno + '\'' +
                ", totalDisciplinas=" + (disciplinas != null ? disciplinas.size() : 0) +
                '}';
    }
}
