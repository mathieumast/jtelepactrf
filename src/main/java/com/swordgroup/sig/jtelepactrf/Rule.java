package com.swordgroup.sig.jtelepactrf;

/**
 * Définition d'une règle de transformation.
 *
 * @author mmast Sword/Groupama
 */
public class Rule {
    
    public static final String ADD = "add";
    
    public static final String REMOVE = "remove";
    
    public static final String UPDATE = "update";
    
    public static final String RENAME = "rename";

    /**
     * Type de règle (add, remove, update, rename)
     */
    private String type;

    /**
     * Nom de la propriété cible
     */
    private String property;

    /**
     * Valeur cible
     */
    private Object value;

    /**
     * Nom cible
     */
    private String name;

    private Rule() {
    }

    public static Rule add(String property, Object value) {
        Rule rule = new Rule();
        rule.type = ADD;
        rule.property = property;
        rule.value = value;
        return rule;
    }

    public static Rule remove(String property) {
        Rule rule = new Rule();
        rule.type = REMOVE;
        rule.property = property;
        return rule;
    }

    public static Rule update(String property, Object value) {
        Rule rule = new Rule();
        rule.type = UPDATE;
        rule.property = property;
        rule.value = value;
        return rule;
    }

    public static Rule rename(String property, String name) {
        Rule rule = new Rule();
        rule.type = RENAME;
        rule.property = property;
        rule.name = name;
        return rule;
    }

    public String getType() {
        return type;
    }

    public String getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Rule{" + "type=" + type + ", property=" + property + ", value=" + value + ", name=" + name + '}';
    }

}
