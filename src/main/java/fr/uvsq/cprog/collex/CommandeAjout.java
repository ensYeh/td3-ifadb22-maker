package fr.uvsq.cprog.collex;

import java.io.IOException;

public class CommandeAjout implements Commande {
    private final Dns dns;
    private final String ip;
    private final String nomMachine;

    public CommandeAjout(Dns dns, String ip, String nomMachine) {
        this.dns = dns;
        this.ip = ip;
        this.nomMachine = nomMachine;
    }

    @Override
    public String execute() {
        try {
            dns.addItem(ip, nomMachine);
            return "Entrée ajoutée avec succès: " + nomMachine + " -> " + ip;
        } catch (IllegalArgumentException e) {
            return "ERREUR: " + e.getMessage();
        } catch (IOException e) {
            return "ERREUR: Impossible de sauvegarder: " + e.getMessage();
        }
    }
}