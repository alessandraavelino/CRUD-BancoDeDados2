
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



public class Utils {
	private static Scanner teclado = new Scanner(System.in);
        private static int opcao = 0;
	
	public static HttpClient conectar() {
		HttpClient conn = HttpClient.newBuilder().build();
		return conn;
	}
	
	public static void desconectar() {
		System.out.println("Desconectando...");
	}
	
	public static void listar() {
		HttpClient conn = conectar();
		
		String link = "http://127.0.0.1:5984/jcouch/_all_docs?include_docs=true";
		HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(link)).build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if((int)obj.get("total_rows") > 0) {
				JSONArray produtos = (JSONArray)obj.get("rows");
				
				System.out.println("LISTANDO PRODUTO");
				System.out.println("--------------------");
				for(Object produto : produtos) {
					JSONObject doc = (JSONObject) produto;
					JSONObject prod = (JSONObject)doc.get("doc");
					
					System.out.println("ID: " + prod.get("_id"));
					System.out.println("Rev: " + prod.get("_rev"));
					System.out.println("Produto: " + prod.get("nome"));
					System.out.println("Preço: " + prod.get("preco"));
                                        System.out.println("C. Barras: " + prod.get("codbar"));
					System.out.println("Estoque: " + prod.get("estoque"));
					System.out.println("----------------------");
				}
			}else {
				System.out.println("Não existem produtos cadastrados.");
			}
		}catch(IOException e) {
			System.out.println("Houve um erro durante a conexão.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro durante a conexão. ");
			e.printStackTrace();
		}
		
		
	}
	
	public static void inserir() {
		HttpClient conn = conectar();
                System.out.println("INSERIR PRODUTO");
                System.out.println("----------------");
                
		Scanner entradaTexto = new Scanner(System.in);
		String link = "http://127.0.0.1:5984/jcouch";
		
		System.out.println("Digite o nome do produto: ");
		String nome = entradaTexto.nextLine();
		
		System.out.println("Digite o preço: ");
		float preco = teclado.nextFloat();
                
                System.out.println("Digite o código de barras: ");
		int codbar = teclado.nextInt();
		
		System.out.println("Informe o estoque: ");
		int estoque = teclado.nextInt();
		
		JSONObject nproduto = new JSONObject();
		nproduto.put("nome", nome);
		nproduto.put("preco", preco);
                nproduto.put("codbar", codbar);
		nproduto.put("estoque", estoque);
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.POST(BodyPublishers.ofString(nproduto.toString()))
				.header("Content-Type", "application/json")
				.build();
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if(resposta.statusCode() == 201) {
				System.out.println("O produto " + nome + " foi cadastrado com sucesso.");
			}else {
				System.out.println(obj);
				System.out.println("Status: " + resposta.statusCode());
			}
		}catch(IOException e) {
			System.out.println("Houve um erro durante a conexão.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro durante a conexão.");
			e.printStackTrace();
		}
		
	}
	
	public static void atualizar() {
		HttpClient conn = conectar();
                System.out.println("ATUALIZAR PRODUTO");
                System.out.println("----------------");
                
                Scanner entradaTexto = new Scanner(System.in);
		
		System.out.println("Informe o ID do produto: ");
		String id = entradaTexto.nextLine();
		
		System.out.println("informe a Rev do produto: ");
		String rev = entradaTexto.nextLine();
		
		System.out.println("Informe o nome do produto alterado: ");
		String nome = entradaTexto.nextLine();
		
		System.out.println("Informe o preço alterado: ");
		float preco = teclado.nextFloat();
                
                System.out.println("Informe o codbar alterado: ");
		int codbar = teclado.nextInt();
		
		System.out.println("Informe o estoque alterado: ");
		int estoque = teclado.nextInt();
		
		String link = "http://localhost:5984/jcouch/" + id + "/" + "?rev=" + rev;
		
		JSONObject nproduto = new JSONObject();
		nproduto.put("nome", nome);
		nproduto.put("preco", preco);
                nproduto.put("codbar", codbar);
		nproduto.put("estoque", estoque);
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.PUT(BodyPublishers.ofString(nproduto.toString()))
				.header("Content-Type", "application/json")
				.build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if(resposta.statusCode() == 201) {
				System.out.println("O produto " + nome + " foi atualizado com sucesso.");
			}else if(resposta.statusCode() == 400) {
				System.out.println("ID e REV inválidos, tente novamente!");
			}else {
				System.out.println(obj);
				System.out.println("Status: " + resposta.statusCode());
			}
		}catch(IOException e) {
			System.out.println("Houve erro na execução.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve erro na execução.");
			e.printStackTrace();
		}
	}
	
	public static void deletar() {
		HttpClient conn = conectar();
                System.out.println("DELETAR PRODUTO");
                System.out.println("----------------");
                
                Scanner entradaTexto = new Scanner(System.in);
		System.out.println("Digite o ID do produto: ");
		String id = entradaTexto.nextLine();
		
		System.out.println("Digite a Rev do produto: ");
		String rev = entradaTexto.nextLine();
		
		String link = "http://localhost:5984/jcouch/" + id + "/" + "?rev=" + rev;
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.DELETE()
				.build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			if(resposta.statusCode() == 200) {
				System.out.println("O produto foi deletado com sucesso.");
			}else if(resposta.statusCode() == 404) {
				System.out.println("Não existe produto com o id e rev informados.");
			}else {
				System.out.println(resposta.body());
				System.out.println("Status: " + resposta.statusCode());
			}
		}catch(IOException e) {
			System.out.println("Houve erro na execução.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve erro na execução.");
			e.printStackTrace();
		}
		
	}
	
	public static void menu() {
                boolean exit = false;
		while (!exit){
                    System.out.println("***** CONTROLE DE ESTOQUE ******");
                    System.out.println("Selecione uma opção: ");
                    System.out.println("1 - Listar produtos");
                    System.out.println("2 - Inserir produtos");
                    System.out.println("3 - Atualizar produtos");
                    System.out.println("4 - Deletar produtos");
		
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
                            System.out.println("Opção inválida.");
                            
                    }
                }
	}   
}
