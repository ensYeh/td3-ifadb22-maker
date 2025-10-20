package fr.uvsq.cprog.collex;

public class CommandeInvalide implements Commande {
    private final String messageErreur;

    public CommandeInvalide(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    @Override
    public String execute() {
        return "ERREUR: " + messageErreur;
    }
}