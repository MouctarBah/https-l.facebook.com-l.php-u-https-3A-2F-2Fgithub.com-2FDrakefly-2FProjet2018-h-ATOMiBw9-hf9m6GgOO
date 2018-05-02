package Modele;

import Controleur.FileFormatException;
import Vue.AffichageBD;
import Vue.Fenetre;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

/**
 * Modele.Simulation est la classe executant une simulation du jeu de la vie
 * <p>
 * Une simulation est caracterisee par sa duree, sa carte, ses regles.
 */
public class Simulation {
    public String fichier;
    final private int duree;
    private transient Liste carte;
    private transient LinkedList<Integer> survie;
    private transient LinkedList<Integer> naissance;
    public boolean gui;
    int vitesse;

    /**
     * Constructeur Modele.Simulation
     *
     * @param duree   La duree de la simulation
     * @param fichier Le fichier qui est l'objet de cette simulation
     */
    public Simulation(int duree, String fichier) {
        this.duree = duree;
        this.carte = new Liste();
        try {
            Lecture l = new Lecture();
            this.fichier = fichier;
            this.carte = l.lis(fichier);
            this.naissance = l.getNaissance();
            this.survie = l.getSurvie();
        } catch (FileFormatException | FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Constructeur de Modele.Simu dans le cas ou l'uttilisateur na pas rentre au debut de fichier
     */
    public Simulation() {
        this.duree = 500;
        this.carte = new Liste();
        try {
            Lecture l = new Lecture();
            this.fichier = String.valueOf(AffichageBD.selectFichier());
            this.carte = l.lis(fichier);
            this.naissance = l.getNaissance();
            this.survie = l.getSurvie();
        } catch (FileFormatException | FileNotFoundException e) {
            e.printStackTrace();
        }
     }

    /**
     * Renvoie la configuration finale de la simulation de la carte pour la duree donnee.
     *
     * @param html Si le renvoie se fait en html
     * @return La configuration de notre Modele.Simulation.
     */
    public String detect(boolean html) {//lol ce genre de methode qui serve pas a grand chose stp on dirait un controleur
        Detection d = new Detection();
        return d.detecte(carte, duree, html, survie, naissance);
    }

    /**
     * Methode tourne qui fait avancer la simulation autant de fois que la duree donnee le demande.
     * A chaque tour elle affiche l'evolution de la carte.
     */
    public void tourne() {
        Fenetre fenetre = new Fenetre();
        vitesse = 300;
        if (gui) {
            fenetre.setVisible(true);
            vitesse=fenetre.vitesse;
        } else {
            System.out.println("Voici la carte ");
            carte.afficher();
        }

        for (int i = 1; i < this.duree; i++) {
            carte = carte.maj(survie, naissance);
            if(gui)vitesse=fenetre.vitesse;
            if (carte.vide()) {
                System.out.println("Deces de la totalite des cellules");
                AffichageBD.information("Deces de la totalite des cellules");
                break;
            }

            if (gui) {
                fenetre.go(carte, i);

            } else {
                carte.afficher();
                System.out.println(i);
            }
            try {
                sleep(vitesse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Lance une simulation spherique
     *
     * @param hauteur coordonnes de la carte
     * @param largeur coordonnes de la carte
     * @param originex coordonnes de la carte
     * @param originey coordonnes de la carte
     */
     public void simuSpherique(int hauteur, int largeur, int originex, int originey) {
        carte.afficher(originex, originey, originex + hauteur, originey + largeur);
        for (int tour = 1; tour < this.duree; tour++) {
            System.out.println("Tour n°" + tour);
            carte = carte.majSphe(survie, naissance, hauteur, largeur, originex, originey);
            carte = carte.supprimerHorsLimite(hauteur, largeur, originex, originey);
            carte.afficher(originex, originey, originex + hauteur, originey + largeur);
            try {
                sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lance une simulation limitee
     *
     * @param hauteur coordonnes de la carte
     * @param largeur coordonnes de la carte
     * @param originex coordonnes de la carte
     * @param originey coordonnes de la carte
     */
     public void simulation(int hauteur, int largeur, int originex, int originey) {//Il y a moyen d'alleger le code et de bcp.

        carte = carte.supprimerHorsLimite(hauteur, largeur, originex, originey);
        new Liste().afficher();
        carte.afficher(originex, originey, originex + largeur, originey + hauteur);
        if (this.carte.vide()) {
            System.out.println("carte vide.");//z
        }
        System.out.println("Voici la carte ");
        carte.afficher(originex, originey, originex + largeur, originey + hauteur);
        for (int i = 1; i < this.duree; i++) {
            carte = carte.maj(survie, naissance);
            carte = carte.supprimerHorsLimite(hauteur, largeur, originex, originey);
            carte.afficher(originex, originey, originex + largeur, originey + hauteur);
            System.out.println(i);
            try {
                sleep(vitesse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
