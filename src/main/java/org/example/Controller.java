package org.example;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Controller {
String nome;
int quantidade;
double preco;
double total;

Scanner entrada = new Scanner(System.in);
int opc;

public Controller(BandoDeDados banco) {
    this.banco = banco;
}

BandoDeDados banco;

void menuPrincipal(){
        do{
            System.out.println("===== Menu Principal =====");
            System.out.println("""
                    [1] - Nova Venda
                    [2] - Novo Produto
                    [3] - Alterar Estoque
                    [4] - Estoque
                    [5] - Historico de Vendas
                    [0] - Sair""");
            if (!entrada.hasNextInt()){
                System.out.println("Invalido!");
                entrada.nextLine();
                continue;
            }
            opc = entrada.nextInt();
            entrada.nextLine();
            switch (opc){
                case 1:
                    novaVenda();
                    break;
                case 2:
                    novoProduto();
                    break;
                case 3:
                    alterarProduto();
                    break;
                case 4:
                    banco.mostrarProdutos();
                    break;
                case 5:
                    banco.historicoDeVendas();
                    System.out.println("""
                            [1] - Limpar Historico
                            [2] - Sair""");
                    if (!entrada.hasNextInt()){
                        System.out.println("Invalido!");
                        entrada.nextLine();
                        continue;
                    }
                    opc = entrada.nextInt();
                    entrada.nextLine();
                    switch (opc){
                        case 1:
                            banco.limparHistoricoDeVendas();
                            break;
                        case 2:
                             System.out.println("Voltando");
                             break;
                    }
                    break;
                case 0:
                     System.out.println("Saindo");
                     break;
                default:
                    System.out.println("Invalido!");
                    break;
            }
        }while(opc != 0);
    }

    void alterarProduto(){
    do {
        banco.mostrarProdutos();
        System.out.println("ID do produto:");
        if (!entrada.hasNextInt()){
            System.out.println("Invalido!");
            entrada.nextLine();
            continue;
        }
        int id = entrada.nextInt();
        entrada.nextLine();
        System.out.println("""
                Digite oque deseja alterar:
                [1] - Nome
                [2] - Preco
                [3] - Quantidade
                [4] - Excluir Produto
                [0] - Voltar""");
        if (!entrada.hasNextInt()) {
            System.out.println("Invalido!");
            entrada.nextLine();
            continue;
        }
        opc = entrada.nextInt();
        entrada.nextLine();
        switch (opc){

            case 1:
                System.out.println("Digite o nome:");
                nome = entrada.nextLine();
                banco.alterarNome(id, nome);
                break;
            case 2:
                System.out.println("Digite o preco:");
                if (!entrada.hasNextDouble()){
                    System.out.println("Invalido!");
                    entrada.nextLine();
                    continue;
                }
                preco = entrada.nextDouble();
                entrada.nextLine();
                banco.alterarPreco(id, preco);
                break;
            case 3:
                System.out.println("Digite o quantidade:");
                if (!entrada.hasNextInt()){
                    entrada.nextLine();
                    System.out.println("Invalido!");
                    continue;
                }
                quantidade = entrada.nextInt();
                entrada.nextLine();
                banco.alterarQuantidade(id, quantidade);
                break;
            case 4:
                banco.excluirProduto(id);
                break;
            case 0:
                System.out.println("Saindo");
                break;
            default:
                System.out.println("Invalido!");
                break;
        }
    }while (opc != 0);
    }
    void novoProduto(){
    while (true) {
        System.out.println("Nome do produto:");
        nome = entrada.nextLine();
        System.out.println("Preco:");
        if (!entrada.hasNextDouble()){
            System.out.println("Invalido!");
            entrada.nextLine();
            continue;
        }
        preco = entrada.nextDouble();
        entrada.nextLine();
        System.out.println("Quantidade:");
        if (!entrada.hasNextInt()){
            System.out.println("Invalido!");
            entrada.nextLine();
            continue;
        }
        quantidade = entrada.nextInt();
        entrada.nextLine();
        banco.adicionarProduto(nome, preco, quantidade);
        break;
    }

    }
    void novaVenda(){
        while (true) {
            banco.mostrarProdutos();
            System.out.println("ID do Produto:");
            if (!entrada.hasNextInt()) {
                System.out.println("Invalido!");
                entrada.nextLine();
                continue;
            }
            int id = entrada.nextInt();
            entrada.nextLine();
            System.out.println("Quantidade:");
            if (!entrada.hasNextInt()) {
                entrada.nextLine();
                System.out.println("Invalido!");
                continue;
            }
            quantidade = entrada.nextInt();
            entrada.nextLine();
            banco.venda(id, quantidade);
            break;
        }
    }
}
