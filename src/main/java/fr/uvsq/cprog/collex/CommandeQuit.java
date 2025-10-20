package fr.uvsq.cprog.collex;

public class CommandeQuit implements Commande {
    @Override
    public String execute() {
        return "quit";
    }
}