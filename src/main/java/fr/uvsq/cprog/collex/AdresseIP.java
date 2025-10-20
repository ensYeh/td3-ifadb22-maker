import java.util.Objects;

public class AdresseIP {
    private final String ip;

    public AdresseIP(String ip) {
        // Validation du format IPv4 simple
        if (ip == null) {
            throw new IllegalArgumentException("L'adresse IP ne doit pas être nulle");
        }
        
        // Validation du format de base
        if (!ip.matches("\\d{1,3}(\\.\\d{1,3}){3}")) {
            throw new IllegalArgumentException("Format d'adresse IP invalide : " + ip);
        }
        
        // Validation des valeurs numériques
        String[] parties = ip.split("\\.");
        for (String partie : parties) {
            int valeur = Integer.parseInt(partie);
            if (valeur < 0 || valeur > 255) {
                throw new IllegalArgumentException("Les octets de l'adresse IP doivent être entre 0 et 255 : " + ip);
            }
        }
        
        this.ip = ip;
    }

    public String getAdresse() {
        return ip;
    }
    
    // Méthode pour vérifier si une chaîne représente une adresse IP valide
    public static boolean estAdresseIPValide(String ip) {
        try {
            new AdresseIP(ip);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AdresseIP adresseIP = (AdresseIP) obj;
        return Objects.equals(ip, adresseIP.ip);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
    
    @Override
    public String toString() {
        return ip;
    }
}