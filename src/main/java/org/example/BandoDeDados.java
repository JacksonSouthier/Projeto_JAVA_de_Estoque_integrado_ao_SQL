package org.example;

import java.sql.*;
import java.util.Scanner;

public class BandoDeDados {
Scanner entrada = new Scanner(System.in);
Connection connection = Conectar();

public Connection  Conectar() {
    String url = "jdbc:mysql://localhost:3306";
    String usuario = "root";
    String senha = "1234";

    try {
        return DriverManager.getConnection(url, usuario, senha);

    } catch (SQLException e) {
        System.out.println("Erro ao conectar ao SQL " + e.getMessage());
        return null;
    }
}

void conectarSQL() {
    criarDATABASE();
    criarTABLEestoque();
    criarTABLEvendas();
}

void criarDATABASE() {
    try (PreparedStatement comando = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS projetoSQL")) {
        comando.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'criarDATABASE'");
        throw new RuntimeException(e);
    }
}

void criarTABLEestoque() {
    try (PreparedStatement comando2 = connection.prepareStatement("USE projetoSQL")) {
        comando2.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'criarTABLEestoque'" + e.getMessage());
    }
    try (PreparedStatement comando = connection.prepareStatement("CREATE TABLE IF NOT EXISTS estoque(id INTEGER PRIMARY KEY AUTO_INCREMENT, nome VARCHAR (30), preco DOUBLE, quantidade INTEGER)")) {
        comando.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'criarTABLE'");
        throw new RuntimeException(e);
    }
}

void criarTABLEvendas() {
    try (PreparedStatement comando = connection.prepareStatement("CREATE TABLE IF NOT EXISTS vendas (id INTEGER PRIMARY KEY AUTO_INCREMENT, produto VARCHAR (20), preco DOUBLE, quantidade INTEGER, total DOUBLE)")) {
        comando.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

void adicionarProduto(String nome, double preco, int quantidade) {
    try (PreparedStatement comando = connection.prepareStatement("INSERT INTO estoque(nome, preco, quantidade) VALUES (?,?,?)")) {
        comando.setString(1, nome);
        comando.setDouble(2, preco);
        comando.setInt(3, quantidade);
        comando.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'adicionarProdutos'");
        throw new RuntimeException(e);
    }
}

void excluirProduto(int id) {
    try (PreparedStatement comando = connection.prepareStatement("DELETE FROM estoque WHERE id = ?")) {
        comando.setInt(1, id);
        int linhas = comando.executeUpdate();

        if (linhas > 0) {
            System.out.println("Produto excluido com sucesso!");
        } else {
            System.out.println("Produto não encontrado!");
        }

    } catch (Exception e) {
        System.out.println("Erro 'excluirProdutos'");
        throw new RuntimeException(e);
    }
}

void mostrarProdutos() {
    try (PreparedStatement comando = connection.prepareStatement("SELECT * FROM estoque"); ResultSet resultSet = comando.executeQuery()) {
        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id") + " | Nome: " + resultSet.getString("nome") + " | Preço: $" + resultSet.getDouble("preco") + " | Quantidade: " + resultSet.getInt("quantidade"));
        }
    } catch (SQLException e) {
        System.out.println("Erro 'mostrarProdutos'");
        throw new RuntimeException(e);
    }
}

void alterarNome(int id, String nome) {
    try (PreparedStatement comando = connection.prepareStatement("UPDATE estoque SET nome = ? WHERE id = ?")) {
        comando.setString(1, nome);
        comando.setInt(2, id);
        comando.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'alterarProdutos");
        throw new RuntimeException(e);
    }
}

void alterarPreco(int id, double preco) {
    try (PreparedStatement comando = connection.prepareStatement("UPDATE estoque SET preco = ? WHERE id = ?")) {
        comando.setDouble(1, preco);
        comando.setInt(2, id);
        comando.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'alterarProdutos");
        throw new RuntimeException(e);
    }
}

void alterarQuantidade(int id, int quantidade) {
    try (PreparedStatement comando = connection.prepareStatement("UPDATE estoque SET quantidade = ? WHERE id = ?")) {
        comando.setInt(1, quantidade);
        comando.setInt(2, id);
        comando.executeUpdate();
    } catch (Exception e) {
        System.out.println("Erro 'alterarProdutos");
        throw new RuntimeException(e);
    }
}

void venda(int id, int quantidade) {
    try {
        connection.setAutoCommit(false);

        try (PreparedStatement comando2 = connection.prepareStatement("SELECT * FROM estoque WHERE id = ?")) {
            comando2.setInt(1, id);

            try (ResultSet resultSet = comando2.executeQuery()) {
                if (resultSet.next()) {

                    int quantidadeEstoque = resultSet.getInt("quantidade");
                    double preco = resultSet.getDouble("preco");
                    double total = quantidade * preco;
                    if (quantidade <= quantidadeEstoque) {
                        System.out.println("ID: " + id + " | Nome: " + resultSet.getString("nome") + " | Preco por unidade: " + preco + " | Total: " + total);
                        System.out.println("""
                                [1] - Confirmar Venda
                                [2] - Cancelar""");
                        int opc = entrada.nextInt();
                        if (opc == 1) {

                            try (PreparedStatement comando3 = connection.prepareStatement("INSERT INTO vendas (produto, preco, quantidade, total) VALUES (?,?,?,?)")) {
                                comando3.setString(1, resultSet.getString("nome"));
                                comando3.setDouble(2, resultSet.getDouble("preco"));
                                comando3.setInt(3, quantidade);
                                comando3.setDouble(4, total);
                                comando3.executeUpdate();
                                try (PreparedStatement comando4 = connection.prepareStatement("UPDATE estoque SET quantidade = quantidade - ? WHERE id = ?")) {
                                    comando4.setInt(1, quantidade);
                                    comando4.setInt(2, id);
                                    comando4.executeUpdate();
                                    connection.commit();
                                    System.out.println("Venda realizada com sucesso!");
                                }
                            }
                        } else {
                            connection.rollback();
                            System.out.println("Compra cancelada!");
                        }
                    } else {
                        System.out.println("Quantidade insuficiente!");
                    }

                } else {
                    System.out.println("Produto nao encontrado");
                }
            }
        }
        } catch (Exception e) {
        try {
            connection.rollback();
            System.out.println("Erro 'alterarProdutos'");
            throw new RuntimeException(e);
        } catch (SQLException ex) {
            System.out.println("Erro 'alterarProdutos'");
            throw new RuntimeException(ex);
        }
    }finally {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            throw  new RuntimeException(ex);
        }
        }
    }

    void historicoDeVendas() {
    try (PreparedStatement comando = connection.prepareStatement("SELECT * FROM vendas")){
        try(ResultSet resultSet = comando.executeQuery()){
            double faturamento = 0;
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String produto = resultSet.getString("produto");
                double preco = resultSet.getDouble("preco");
                int quantidade = resultSet.getInt("quantidade");
                double total = resultSet.getDouble("total");
                System.out.println("ID: " + id + " | Nome: " + produto +  " | Preco: " + preco + " | Quantidade: " + quantidade + " | Total: " + total);
                faturamento += total;
            }

            System.out.println("Faturamento Total: " + faturamento);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }
    void limparHistoricoDeVendas() {
    try(PreparedStatement comando = connection.prepareStatement("DELETE FROM vendas")){
        comando.executeUpdate();
    }catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }
}