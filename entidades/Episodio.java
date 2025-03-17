package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Episodio {

    private int id;
    private int serieId;
    private String name;
    private int temporada;
    private String lancamento;
    private int duracao;
    private String sinopse;

    //construtores
    public Episodio() throws Exception {
        this(-1, -1, "", -1, "", -1, "");
    }

    public Episodio(int serieId, String name, int temporada, String lancamento, int duracao, String sinopse) throws Exception {
        this(-1, serieId, name, temporada,lancamento, duracao, sinopse);
    }

    public Episodio(int id, int serieId, String name, int temporada, String lancamento, int duracao, String sinopse) throws Exception {
        this.id = id;
        this.serieId = serieId;
        this.name = name;
        this.temporada = temporada;
        this.lancamento = lancamento;
        this.duracao = duracao;
        this.sinopse = sinopse;
    }

    //gets
    public int getId() {
        return id;
    }

    public int getSerieId() {
        return serieId;
    }

    public String getName() {
        return name;
    }
    
    public int getTemporada() {
        return temporada;
    }

    public String getLancamento() {
        return lancamento;
    }
    
    public int getDuracao() {
        return duracao;
    }

    public String getSinopse() {
        return sinopse;
    }

    //seters
    public void setId(int id) {
        this.id = id;
    }

    public void setSerieId(int serieId) {
        this.serieId = serieId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public void setLancamento(String lancamento) {
        this.lancamento = lancamento;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(serieId);
        dos.writeUTF(name);
        dos.writeInt(temporada);
        dos.writeUTF(lancamento);
        dos.writeInt(duracao);
        dos.writeUTF(sinopse);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] byteArr) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        serieId = dis.readInt();
        name = dis.readUTF();
        temporada = dis.readInt();
        lancamento = dis.readUTF();
        duracao = dis.readInt();
        sinopse = dis.readUTF();
    }
}
