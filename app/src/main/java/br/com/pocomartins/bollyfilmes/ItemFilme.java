package br.com.pocomartins.bollyfilmes;

import java.net.URI;

/**
 * Created by Poço Martins on 1/21/2017.
 */

public class ItemFilme {

    private String id;

    private String titulo;

    private String descricao;

    private URI imagem;

    private float avaliacao;

    private String dataLancamento;

    public ItemFilme(String titulo, String descricao, String dataLancamento, float avaliacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataLancamento = dataLancamento;
        this.avaliacao = avaliacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public URI getImagem() {
        return imagem;
    }

    public void setImagem(URI imagem) {
        this.imagem = imagem;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
}
