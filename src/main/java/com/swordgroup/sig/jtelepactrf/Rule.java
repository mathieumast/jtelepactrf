package com.swordgroup.sig.jtelepactrf;

/**
 * Définition d'une règle de transformation.
 * 
 * @author mmast Sword/Groupama
 */
public class Rule {
 
    /**
     * Type de règle (add, remove, update, rename)
     */
    String type;
    
    /**
     * Nom de la propriété cible
     */
    String property;
    
    /**
     * Valeur cible
     */
    Object value;
    
    /**
     * Nom cible
     */
    String name;
}
