/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.produtos;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author Alessandra
 */
public class Utils {
    public static Scanner teclado = new Scanner(System.in);

    public static HttpClient conectar(){
        HttpClient conn = HttpClient.newBuilder().build();
        return conn;

    }
    
    public static void listar(){
        
        HttpClient conn = conectar();
        String link = "http://ale:123456@127.0.0.1:5984/jcouch/_all_docs?include_docs=true";
        HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(link)).build(); 
        
        try {
            HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
            JSONObject obj = new JSONObject(resposta.body());
            
            if ((int)obj.get("total_rows") > 0){
                JSONArray produtos = (JSONArray)obj.get("rows");
                System.out.println("Listando pródutos");
                for(Object produto : produtos){
                    JSONObject doc = (JSONObject) produto;
                    JSONObject prod = (JSONObject)doc.get("doc");
                    System.out.println("ID: " + prod.get("_id"));
                    System.out.println("Rev: " + prod.get("_rev"));
                    System.out.println("Produto: " + prod.get("nome"));
                    System.out.println("Código de Barras: " + prod.get("codbar"));
                    
                    System.out.println("Preço: " + prod.get("preco"));
                    System.out.println("Quantidade: " + prod.get("quantidade"));
                    
                }
                
            } else {
                System.out.println("n há produtos");
            }
     
                     
            
        }catch(IOException e){
            System.out.println("erro");
            e.printStackTrace();
            
        }catch(InterruptedException e){
            System.out.println("rero");
            e.printStackTrace();
        }
        
    }
    
    
    
    public static void menu(){
        System.out.println("1 - INSERIR");
        System.out.println("2- LISTAR");
        System.out.println("3- ATUALIZAR");
        System.out.println("4- DELETAR");
        
        int opcao = Integer.parseInt(teclado.nextLine());
        if (opcao == 1){
            listar();
        
        } else {
            System.out.println("opc invalida");
        }
        
    }

    
}










