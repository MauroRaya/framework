/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework_netbeans_antigo;

/**
 *
 * @author unisanta
 */
public class Livro {
    private  String titulo;
    private  String autor;
    private  String editora;
    private  String ano;
    private  String localizacao;

    public Livro() {}
    public  void setTitulo(String _titulo) {titulo=_titulo;}
    public  void setAutor(String _autor) {autor=_autor;}
    public  void setEditora(String _editora) {editora=_editora;}
    public  void setAno(String _ano) {ano = _ano;}
    public  void setLocalizacao(String _localizacao) {localizacao=_localizacao;}

    public  String getTitulo() {return titulo;}
    public  String getAutor() {return autor;}
    public  String getEditora() {return editora;}
    public  String getAno() {return ano;}
    public  String getLocalizacao() {return localizacao;}    
}
