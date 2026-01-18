package ModelosPets;

public class Pet {
    public static final String NAO_INFORMADO = "NÃO INFORMADO";

    private TipoPet tipo;
    private SexoPet sexo;
    private String nome;
    private double idade;
    private double peso;
    private String raca;

    private String rua;
    private String cidade;
    private String numero;

    public Pet() {}

    // CORRETO - O dado é salvo no objeto
    public Pet(TipoPet tipo, SexoPet sexo, String nome, double idade, double peso, String raca, String rua, String numero, String cidade) {
        this.tipo = tipo;    // <--- O 'this' é obrigatório aqui!
        this.sexo = sexo;
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.raca = raca;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
    }

    public SexoPet getSexo() {
        return sexo;
    }

    public void setSexo(SexoPet sexo) {
        this.sexo = sexo;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    public void setTipo(TipoPet tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getIdade() {
        return idade;
    }

    public void setIdade(double idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "ModeloPet.Pet [Nome=" + nome + ", Tipo=" + tipo + ", Idade=" + idade + " anos]";
    }
}


