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
public class Framework_netbeans_antigo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Livro dados  = new Livro();
        Livro chaves = new Livro();
        
        chaves.setTitulo("harry potter");
        chaves.setAutor("jk rowling");
        
        chaves.setEditora("aaa");
        dados.setAno("aaa");
        dados.setLocalizacao("aaa");
        
        DAL.createTable(dados);
        
        DAL.get(dados);
        DAL.add(dados);
        DAL.update(chaves, dados);
        DAL.delete(dados);
    }   
}
