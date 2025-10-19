public class NomMachine{
    private final String nom;

    public NomMachine(String nom) {
        if (nom == null) {
        throw new IllegalArgumentException("L'adresse IP ne doit pas être nulle");
        }
        if (!nom.contains(".")) {
            throw new IllegalArgumentException("Format de nom de machine invalide : " + nom);
        }
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
    }
