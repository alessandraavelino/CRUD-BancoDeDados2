package com.mycompany.produtojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Properties;

public class Utils {
    public static Scanner teclado = new Scanner(System.in);
    private static int opcao = 0;
    public static Connection conectar(){
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "12345");
        props.setProperty("ssl", "false");
        String url_banco = "jdbc:postgresql://localhost:5432/postgres";
        
        try {
                return DriverManager.getConnection(url_banco, props);
        }catch(Exception e) {
            e.printStackTrace();
            if (e instanceof ClassNotFoundException){
                System.out.println("Erros nos drivers de conexão");
                
            } else {
                System.out.println("Verifique se o seu server está ativo");
            }
            System.exit(-42);
            return null;
        }
    }
    
    public static void desconectar(Connection conn){
        if (conn != null) {
            try {
                    conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public static void listar() {
        String buscar_table = "SELECT * FROM produto";
        
        
        try {
                Connection conn = conectar();
                PreparedStatement produto = conn.prepareStatement (
                                  buscar_table,
                                  ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_READ_ONLY
                );
                
                ResultSet res = produto.executeQuery();
                res.last();
                int row = res.getRow();
                res.beforeFirst();
                
                if (row > 0) {
                    System.out.println("LISTANDO PRODUTOS");
                    System.out.println("--------------------");
                    while (res.next()) {
                        System.out.println("ID: " + res.getInt(1));
                        System.out.println("Produto: " + res.getString(2));
                        System.out.println("Preço: " + res.getFloat(3));
                        System.out.println("C. Barras: " + res.getInt(4));
                        System.out.println("Estoque: " + res.getInt(5));    
                        System.out.println("--------------------");
                    }
                  
                } else {
                    System.out.println("O estoque está vazio!");
                }
                
        } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ocorreu um erro, durante a busca!");
                System.exit(-42);
        }
    }
    
    public static void inserir(){
        Scanner entradaTexto = new Scanner(System.in);
        System.out.println("INSERIR PRODUTO");
        System.out.println("----------------");
        
        
        System.out.println("Digite o nome do produto: ");
        String nome = entradaTexto.nextLine();
        
        System.out.println("Digite o valor: ");
        float preco = teclado.nextFloat();
        
        System.out.println("Digite o código de barras: ");
        int codbar = teclado.nextInt();
        
        System.out.println("Informe o estoque: ");
        int estoque = teclado.nextInt();
        
        String inserir = "INSERT INTO produto(nome, preco, codbar, estoque) VALUES (?, ?, ?, ?)";
        
        try {
                Connection conn = conectar();
                PreparedStatement salvar = conn.prepareStatement(inserir);

                
                salvar.setString(1, nome);
                salvar.setFloat(2, preco);
                salvar.setInt(3, codbar);
                salvar.setInt(4, estoque);

                salvar.executeUpdate();
                salvar.close();
                desconectar(conn);
                System.out.println("O produto " + nome + " foi inserido com sucesso.");
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Erro ao salvar produto.");
            System.exit(-42);
            
        }
    }
    
    public static void atualizar(){
        
        System.out.println("Digite o id do produto que deseja atualizar: ");
        int id = teclado.nextInt();
        System.out.println("ATUALIZAR PRODUTO");
        System.out.println("________________");
        
        String buscar_id = "SELECT * FROM produto WHERE id=?";
        
        try {
                Connection conn = conectar();
                PreparedStatement produt = conn.prepareStatement(
                                buscar_id,
                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY
                );
                produt.setInt(1, id);
                ResultSet res = produt.executeQuery();
                res.last();
                int row = res.getRow();
                res.beforeFirst();
                
                if (row > 0){
                    Scanner entradaTexto = new Scanner(System.in);
                    System.out.println("Informe o nome atualizado: ");
                    String nome = entradaTexto.nextLine();
                    
                    System.out.println("Informe o preço atualizado: ");
                    float preco = teclado.nextFloat();
                    
                    System.out.println("Informe o código de barras atualizado: ");
                    int codbar = teclado.nextInt();
                    
                    System.out.println("Informe o estoque atualizado: ");
                    int estoque = teclado.nextInt();
                    
                    String atualizar = "UPDATE produto SET nome=?, preco=?, codbar=?, estoque=? WHERE id=?";
                    PreparedStatement updt = conn.prepareStatement(atualizar);
                    updt.setString(1, nome);
                    updt.setFloat(2, preco);
                    updt.setInt(3, codbar);
                    updt.setInt(4, estoque);
                    updt.setInt(5,  id);
				
                    updt.executeUpdate();
                    updt.close();
                    desconectar(conn);
                    System.out.println("O produto " + nome + " foi atualizado com sucesso.");

                } else {
                    System.out.println("Nao há nenhum produto com este id.");
                }
        }catch(Exception e ){
                e.printStackTrace();
                System.out.println("Não foi possível atualizar o produto.");
                System.exit(-42);
        }
  
    }
    
    public static void deletar(){
        String deletar = "DELETE FROM produto WHERE id=?";
        String buscar_id = "SELECT * FROM produto WHERE id=?";
        System.out.println("DELETAR PRODUTO");
        System.out.println("________________");
        System.out.println("Digite o id do produto: ");
        int id = teclado.nextInt();
        
        try {
            Connection conn = conectar();
            PreparedStatement produto = conn.prepareStatement(
					buscar_id,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
            );
            produto.setInt(1, id);
            ResultSet res = produto.executeQuery();
            res.last();
            int row = res.getRow();
            res.beforeFirst();
            
            if (row > 0) {
                PreparedStatement del = conn.prepareStatement(deletar);
                    del.setInt(1, id);
                    del.executeUpdate();
                    del.close();
                    desconectar(conn);
                    System.out.println("O produto foi deletado com sucesso.");
                
            } else {
                System.out.println("Não existe produto com o id informado.");
            }
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Erro ao deletar produto");
            System.exit(-42);
            
        }
    }
    
    
    public static void menu(){
        boolean exit = false;
        while (!exit){
            System.out.println("***** CONTROLE DE ESTOQUE ******");
            System.out.println("Selecione uma opção: ");
            System.out.println("1 - Listar produtos");
            System.out.println("2 - Inserir produtos");
            System.out.println("3 - Atualizar produtos");
            System.out.println("4 - Deletar produtos");
            System.out.println("5 - Sair");
            
            opcao = teclado.nextInt();
            
            switch(opcao){
                case 1:
                    listar();
                    break;
                case 2:
                    inserir();
                    break;
                case 3:
                    atualizar();
                    break;
                case 4:
                    deletar();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Opção invalida!");
            }
            
            
        }
    }
}

