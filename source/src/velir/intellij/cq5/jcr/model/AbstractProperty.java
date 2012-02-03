package velir.intellij.cq5.jcr.model;

/**
 * @author krasmussen
 */
public abstract class AbstractProperty implements VProperty {

    public static final String JCR_PRIMARYTYPE = "jcr:primaryType";
    public static final String BOOLEAN_PREFIX = "{Boolean}";
    public static final String DATE_PREFIX = "{Date}";
    public static final String DOUBLE_PREFIX = "{Double}";
    public static final String LONG_PREFIX = "{Long}";
    public static final String NAME_PREFIX = "{Name}";
    public static final String PATH_PREFIX = "{Path}";
    public static final String BINARY_PREFIX = "{Binary}";
    public static final String[] TYPESTRINGS = {
            "{String}",
            BOOLEAN_PREFIX,
            DATE_PREFIX,
            DOUBLE_PREFIX,
            //NAME_PREFIX,
            //PATH_PREFIX,
            //BINARY_PREFIX,
            LONG_PREFIX,
            LONG_PREFIX + "[]",
            DOUBLE_PREFIX + "[]",
            BOOLEAN_PREFIX + "[]",
            "{String}[]"
    };

    private Object value;
    private String name;

    public AbstractProperty(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public Object getValue(){
        return value;
    }

    public String getType(){
        return "String";
    }

    public Class getValueClass(){
        return value.getClass();
    }

    public boolean isAutoCreated(){
        return false;
    }

    public boolean isMandatory(){
        return false;
    }

    public boolean isProtected(){
        return false;
    }

    public boolean isMultiple(){
        return false;
    }
}
