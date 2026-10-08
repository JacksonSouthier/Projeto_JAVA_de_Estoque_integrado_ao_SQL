package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
static void main(){
    BandoDeDados banco = new BandoDeDados();
    Controller controller = new Controller(banco);

    banco.conectarSQL();
    controller.menuPrincipal();
}
}
