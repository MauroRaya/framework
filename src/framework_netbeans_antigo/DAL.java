/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework_netbeans_antigo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author unisanta
 */
public class DAL {
    public static void createTable(Object obj) {
        Class<?> classe = obj.getClass();
        Field[] campos = classe.getDeclaredFields();
     
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE Tab").append(classe.getSimpleName());
        
        query.append(" (");
        
        query.append(
                Arrays.stream(campos)
                        .map(campo -> campo.getName() + " " + (campo.getType().getSimpleName().equals("String")
                                ? "VARCHAR(60)"
                                : campo.getType().getSimpleName().toUpperCase()))
                        .collect(Collectors.joining(", "))
        );
        
        query.append(")");
        
        System.out.println(query);
    }
    
    private static Method[] obterMetodosGet(Class<?> classe, Field[] campos) {
        return Arrays.stream(campos)
            .map(campo -> {
                try {
                    return classe.getMethod("get" + campo.getName().substring(0, 1).toUpperCase() + campo.getName().substring(1));
                } catch (Exception ex) {
                    throw new RuntimeException();
                }
            })
            .toArray(Method[]::new);
    }
    
    private static String formatarRetornoDoInvoke(Object valor) {
        if (valor == null) {
            return "NULL";
        } else if (valor instanceof String) {
            return "'" + valor + "'";
        } else {
            return valor.toString();
        }
    }
    
    public static void add(Object obj) {
        Class<?> classe = obj.getClass();
        Field[] campos = classe.getDeclaredFields();
        
        Method[] metodosGet = obterMetodosGet(classe, campos);
        
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO Tab").append(classe.getSimpleName());
        
        query.append(" (");
        query.append(Arrays.stream(campos)
                .map(campo -> campo.getName())
                .collect(Collectors.joining(", "))
        );
        
        query.append(") VALUES (");
        
        query.append(Arrays.stream(metodosGet)
                .map(metodo -> {
                    try {
                        return formatarRetornoDoInvoke(metodo.invoke(obj));
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                })
                .collect(Collectors.joining(", "))
        );
        query.append(")");
        
        System.out.println(query);
    }
    
    public static void delete(Object obj) {
        Class<?> classe = obj.getClass();
        Field[] campos = classe.getDeclaredFields();
        Method[] metodosGet = obterMetodosGet(classe, campos);
        
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM Tab").append(classe.getSimpleName()).append(" WHERE ");
        
        for (int i = 0; i < metodosGet.length; i++) {
            String valor = "";
            
            try {
                valor = formatarRetornoDoInvoke(metodosGet[i].invoke(obj));
            } catch (Exception e) {
                throw new RuntimeException();
            }
            
            if (valor.equals("NULL")) {
                continue;
            }
            
            String nomeColuna = campos[i].getName();
            
            query.append(nomeColuna).append(" = ").append(valor);
            
            if (i != metodosGet.length - 1) {
                query.append(" AND ");
            }
        }
        
        System.out.println(query);
    }
    
    public static void get(Object obj) {
        Class<?> classe = obj.getClass();
        Field[] campos = classe.getDeclaredFields();
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        
        query.append(
            Arrays.stream(campos)
                    .map(campo -> campo.getName())
                    .collect(Collectors.joining(", "))
        );
        
        query.append(" FROM Tab").append(classe.getSimpleName());
        
        System.out.println(query);
    }
    
    public static void update(Object chaves, Object dados) {
        Class<?> classe = dados.getClass();
        
        Field[] camposChaves = classe.getDeclaredFields();
        Field[] camposDados = classe.getDeclaredFields();
        
        Method[] metodosGet = obterMetodosGet(classe, camposDados);
        
        StringBuilder query = new StringBuilder();
        query.append("UPDATE Tab").append(classe.getSimpleName()).append(" SET ");
        
        for (int i = 0; i < camposDados.length; i++) {
            String nomeCampo = camposDados[i].getName();
            String valor = "";
            
            try {
                valor = formatarRetornoDoInvoke(metodosGet[i].invoke(dados));
            } catch (Exception e) {
                throw new RuntimeException();
            }
            
            if (valor.equals("NULL")) {
                continue;
            }
            
            query.append(nomeCampo).append(" = ").append(valor);
            
            if (i != camposDados.length - 1) {
                query.append(", ");
            }
        }
        
        query.append(" WHERE ");
        
        List<Field> camposChaveNaoNulo = new ArrayList<>();
        
        for (int i = 0; i < camposChaves.length; i++) {
            String valor = "";
            
            try {
                valor = formatarRetornoDoInvoke(metodosGet[i].invoke(chaves));
            } catch (Exception e) {
                throw new RuntimeException();
            }
            
            if (valor.equals("NULL")) {
                continue;
            }
            
            camposChaveNaoNulo.add(camposChaves[i]);
        }
        
        for (int i = 0; i < camposChaveNaoNulo.size(); i++) {
            String valor = "";
            String nomeCampo = camposChaveNaoNulo.get(i).getName();
            
            try {
                valor = formatarRetornoDoInvoke(metodosGet[i].invoke(chaves));
            } catch (Exception e) {
                throw new RuntimeException();
            }
            
            if (valor.equals("NULL")) {
                continue;
            }

            query.append(nomeCampo).append(" = ").append(valor);
            
            if (i !=  camposChaveNaoNulo.size() - 1) {
                query.append(" AND ");
            }
        }
        
        System.out.println(query);
    }
}
