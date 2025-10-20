package fr.uvsq.cprog.collex;

import java.util.Scanner;

public class DnsTUI {
    private final Dns dns;
    private final Scanner scanner;

    public DnsTUI(Dns dns) {
        this.dns = dns;
        this.scanner = new Scanner(System.in);
    }

    public Commande nextCommande() {
        System.out.print("> ");
        String ligne = scanner.nextLine().trim();
        
        if (ligne.isEmpty()) {
            return null;
        }
        
        if (ligne.equalsIgnoreCase("quit")) {
            return new CommandeQuit();
        }
        
        try {
            String[] parties = ligne.split("\\s+");
            
            if (parties.length == 1) {
                String argument = parties[0];
                if (AdresseIP.estAdresseIPValide(argument)) {
                    return new CommandeRechercheIP(dns, argument);
                } else if (NomMachine.estNomMachineValide(argument)) {
                    return new CommandeRechercheNom(dns, argument);
                } else {
                    throw new IllegalArgumentException("Commande non reconnue: " + argument);
                }
            }
            
            String commande = parties[0].toLowerCase();
            
            if (commande.equals("ls")) {
                return creerCommandeLs(parties);
            } else if (commande.equals("add")) {
                return creerCommandeAdd(parties);
            } else {
                throw new IllegalArgumentException("Commande non reconnue: " + commande);
            }
            
        } catch (IllegalArgumentException e) {
            return new CommandeInvalide(e.getMessage());
        }
    }

    private Commande creerCommandeLs(String[] parties) {
        if (parties.length < 2) {
            throw new IllegalArgumentException("La commande 'ls' nécessite un nom de domaine");
        }
        
        boolean trierParIP = false;
        String domaine;
        
        if (parties[1].equals("-a")) {
            trierParIP = true;
            if (parties.length < 3) {
                throw new IllegalArgumentException("La commande 'ls -a' nécessite un nom de domaine");
            }
            domaine = parties[2];
        } else {
            domaine = parties[1];
        }
        
        return new CommandeListeDomaine(dns, domaine, trierParIP);
    }

    private Commande creerCommandeAdd(String[] parties) {
        if (parties.length != 3) {
            throw new IllegalArgumentException("La commande 'add' nécessite une adresse IP et un nom de machine");
        }
        
        String ip = parties[1];
        String nomMachine = parties[2];
        
        if (!AdresseIP.estAdresseIPValide(ip)) {
            throw new IllegalArgumentException("Adresse IP invalide: " + ip);
        }
        
        if (!NomMachine.estNomMachineValide(nomMachine)) {
            throw new IllegalArgumentException("Nom de machine invalide: " + nomMachine);
        }
        
        return new CommandeAjout(dns, ip, nomMachine);
    }

    public void affiche(String message) {
        System.out.println(message);
    }

    public void demarrer() {
        affiche("Serveur DNS démarré. Commandes disponibles:");
        affiche("  nom.qualifié.machine - Affiche l'adresse IP");
        affiche("  adresse.ip - Affiche le nom qualifié");
        affiche("  ls [-a] domaine - Liste les machines du domaine");
        affiche("  add ip nom.machine - Ajoute une entrée DNS");
        affiche("  quit - Quitte le programme");
        affiche("");
        
        boolean continuer = true;
        while (continuer) {
            try {
                Commande commande = nextCommande();
                
                if (commande == null) {
                    continue;
                }
                
                String resultat = commande.execute();
                
                if (commande instanceof CommandeQuit) {
                    continuer = false;
                    affiche("Au revoir !");
                } else {
                    affiche(resultat);
                }
                
            } catch (Exception e) {
                affiche("ERREUR: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}