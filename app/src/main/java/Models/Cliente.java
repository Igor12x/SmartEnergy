package Models;

public class Cliente {
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private  int codigo;

    public Cliente(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Cliente(String nome, String sobrenome, String cpf, String email, String telefone, String senha, int codigo) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.codigo = codigo;
    }

    public Cliente(String nome, String sobrenome, String cpf, String email, String telefone, String senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }
    public String getSobrenome() {
        return sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenha() {
        return senha;
    }

    public int getCodigo() {
        return codigo;
    }
}
