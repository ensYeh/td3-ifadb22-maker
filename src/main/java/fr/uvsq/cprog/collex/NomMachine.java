import java.util.Objects;

public class NomMachine {
    private final String nom;

    public NomMachine(String nom) {
        if (nom == null) {
            throw new IllegalArgumentException("Le nom de machine ne doit pas être nul");
        }
        
        if (!nom.contains(".")) {
            throw new IllegalArgumentException("Format de nom de machine invalide : " + nom);
        }
        
        // Validation supplémentaire : ne doit pas commencer ou finir par un point
        if (nom.startsWith(".") || nom.endsWith(".")) {
            throw new IllegalArgumentException("Le nom de machine ne peut pas commencer ou finir par un point : " + nom);
        }
        
        // Validation : au moins un caractère avant et après le premier point
        int firstDotIndex = nom.indexOf('.');
        if (firstDotIndex == 0 || firstDotIndex == nom.length() - 1) {
            throw new IllegalArgumentException("Le nom de machine doit avoir des caractères avant et après le point : " + nom);
        }
        
        // Validation des caractères autorisés
        if (!nom.matches("[a-zA-Z0-9.-]+")) {
            throw new IllegalArgumentException("Le nom de machine contient des caractères invalides : " + nom);
        }
        
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
    
    // Méthode pour obtenir le nom de la machine (partie avant le premier point)
    public String getNomMachine() {
        return nom.substring(0, nom.indexOf('.'));
    }
    
    // Méthode pour obtenir le domaine (partie après le premier point)
    public String getDomaine() {
        return nom.substring(nom.indexOf('.') + 1);
    }
    
    // Méthode utilitaire pour vérifier si une chaîne est un nom de machine valide
    public static boolean estNomMachineValide(String nom) {
        try {
            new NomMachine(nom);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NomMachine that = (NomMachine) obj;
        return Objects.equals(nom, that.nom);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
    
    @Override
    public String toString() {
        return nom;
    }
}