import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Dns {
    private final List<DnsItem> items;
    private final String fichierBase;

    public Dns() throws IOException {
        // Chargement du fichier de propriétés
        Properties proprietes = new Properties();
        try {
            proprietes.load(Files.newInputStream(Paths.get("config.properties")));
        } catch (IOException e) {
            throw new IOException("Impossible de charger le fichier config.properties", e);
        }
        
        this.fichierBase = proprietes.getProperty("dns.file");
        if (fichierBase == null) {
            throw new IOException("La propriété 'dns.file' n'est pas définie dans config.properties");
        }
        
        this.items = new ArrayList<>();
        chargerBaseDeDonnees();
    }

    public Dns(String cheminFichier) throws IOException {
        this.fichierBase = cheminFichier;
        this.items = new ArrayList<>();
        chargerBaseDeDonnees();
    }

    private void chargerBaseDeDonnees() throws IOException {
        Path chemin = Paths.get(fichierBase);
        
        // Créer le répertoire parent si nécessaire
        if (chemin.getParent() != null) {
            Files.createDirectories(chemin.getParent());
        }
        
        // Créer le fichier s'il n'existe pas
        if (!Files.exists(chemin)) {
            Files.createFile(chemin);
            return;
        }
        
        // Charger les lignes du fichier
        List<String> lignes = Files.readAllLines(chemin);
        
        for (int i = 0; i < lignes.size(); i++) {
            String ligne = lignes.get(i).trim();
            if (!ligne.isEmpty()) {
                try {
                    DnsItem item = DnsItem.fromLigne(ligne);
                    items.add(item);
                } catch (IllegalArgumentException e) {
                    throw new IOException("Erreur de format à la ligne " + (i + 1) + ": " + ligne, e);
                }
            }
        }
    }

    public DnsItem getItem(AdresseIP adresseIP) {
        if (adresseIP == null) {
            throw new IllegalArgumentException("L'adresse IP ne peut pas être nulle");
        }
        
        return items.stream()
                .filter(item -> item.getAdresseIP().equals(adresseIP))
                .findFirst()
                .orElse(null);
    }

    public DnsItem getItem(NomMachine nomMachine) {
        if (nomMachine == null) {
            throw new IllegalArgumentException("Le nom de machine ne peut pas être nul");
        }
        
        return items.stream()
                .filter(item -> item.getNomMachine().equals(nomMachine))
                .findFirst()
                .orElse(null);
    }

    public DnsItem getItem(String ipOuNom) {
        if (ipOuNom == null || ipOuNom.trim().isEmpty()) {
            throw new IllegalArgumentException("La chaîne ne peut pas être nulle ou vide");
        }
        
        // Détermine si c'est une IP ou un nom de machine
        if (AdresseIP.estAdresseIPValide(ipOuNom)) {
            return getItem(new AdresseIP(ipOuNom));
        } else if (NomMachine.estNomMachineValide(ipOuNom)) {
            return getItem(new NomMachine(ipOuNom));
        } else {
            throw new IllegalArgumentException("La chaîne n'est ni une adresse IP valide ni un nom de machine valide: " + ipOuNom);
        }
    }

    public List<DnsItem> getItems(String domaine) {
        if (domaine == null || domaine.trim().isEmpty()) {
            throw new IllegalArgumentException("Le domaine ne peut pas être nul ou vide");
        }
        
        return items.stream()
                .filter(item -> item.getNomMachine().getDomaine().equals(domaine))
                .collect(Collectors.toList());
    }

    public void addItem(AdresseIP adresseIP, NomMachine nomMachine) throws IOException {
        if (adresseIP == null || nomMachine == null) {
            throw new IllegalArgumentException("L'adresse IP et le nom de machine ne peuvent pas être nuls");
        }
        
        // Vérifier si l'adresse IP existe déjà
        DnsItem itemExistanteParIP = getItem(adresseIP);
        if (itemExistanteParIP != null) {
            throw new IllegalArgumentException("ERREUR : L'adresse IP existe déjà !");
        }
        
        // Vérifier si le nom de machine existe déjà
        DnsItem itemExistanteParNom = getItem(nomMachine);
        if (itemExistanteParNom != null) {
            throw new IllegalArgumentException("ERREUR : Le nom de machine existe déjà !");
        }
        
        // Ajouter le nouvel item
        DnsItem nouvelItem = new DnsItem(adresseIP, nomMachine);
        items.add(nouvelItem);
        
        // Mettre à jour le fichier
        sauvegarderBaseDeDonnees();
    }

    public void addItem(String ip, String nomComplet) throws IOException {
        addItem(new AdresseIP(ip), new NomMachine(nomComplet));
    }

    private void sauvegarderBaseDeDonnees() throws IOException {
        List<String> lignes = items.stream()
                .map(DnsItem::toLigne)
                .collect(Collectors.toList());
        
        Files.write(Paths.get(fichierBase), lignes);
    }

    public List<DnsItem> getTousLesItems() {
        return new ArrayList<>(items);
    }

    public int getNombreItems() {
        return items.size();
    }
}