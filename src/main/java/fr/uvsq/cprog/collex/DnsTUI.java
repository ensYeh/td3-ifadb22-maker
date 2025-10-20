import java.util.Scanner;
import java.util.List;

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
        
        // Gestion de la commande quit
        if (ligne.equalsIgnoreCase("quit")) {
            return new CommandeQuit();
        }
        
        try {
            return analyserCommande(ligne);
        } catch (IllegalArgumentException e) {
            return new CommandeInvalide("Commande invalide: " + e.getMessage());
        }
    }

    private Commande analyserCommande(String ligne) {
        String[] parties = ligne.split("\\s+");
        
        if (parties.length == 1) {
            String argument = parties[0];
            if (AdresseIP.estAdresseIPValide(argument)) {
                return new CommandeRechercheIP(argument);
            } else if (NomMachine.estNomMachineValide(argument)) {
                return new CommandeRechercheNom(argument);
            } else {
                throw new IllegalArgumentException("Argument non reconnu comme IP ou nom de machine valide");
            }
        }
        
        String premierMot = parties[0].toLowerCase();
        
        switch (premierMot) {
            case "ls":
                return analyserCommandeLs(parties);
            case "add":
                return analyserCommandeAdd(parties);
            default:
                throw new IllegalArgumentException("Commande non reconnue: " + premierMot);
        }
    }

    private Commande analyserCommandeLs(String[] parties) {
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
        
        return new CommandeListeDomaine(domaine, trierParIP);
    }

    private Commande analyserCommandeAdd(String[] parties) {
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
        
        return new CommandeAjout(ip, nomMachine);
    }

    public void affiche(String message) {
        System.out.println(message);
    }

    public void affiche(DnsItem item) {
        if (item != null) {
            affiche(item.toString());
        } else {
            affiche("Aucun résultat trouvé");
        }
    }

    public void affiche(List<DnsItem> items, boolean trierParIP) {
        if (items == null || items.isEmpty()) {
            affiche("Aucun résultat trouvé");
            return;
        }
        
        List<DnsItem> itemsTries;
        if (trierParIP) {
            itemsTries = items.stream()
                .sorted((i1, i2) -> i1.getAdresseIP().getAdresse().compareTo(i2.getAdresseIP().getAdresse()))
                .toList();
        } else {
            itemsTries = items.stream()
                .sorted((i1, i2) -> i1.getNomMachine().getNom().compareTo(i2.getNomMachine().getNom()))
                .toList();
        }
        
        for (DnsItem item : itemsTries) {
            affiche(item.getAdresseIP().getAdresse() + " " + item.getNomMachine().getNom());
        }
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
                
                if (commande instanceof CommandeQuit) {
                    continuer = false;
                    affiche("Au revoir !");
                } else {
                    commande.executer(this, dns);
                }
                
            } catch (Exception e) {
                affiche("ERREUR: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}