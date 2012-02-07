package velir.intellij.cq5.jcr.model;

/**
 * @author krasmussen
 */
public abstract class AbstractProperty implements VProperty {

    public static final String JCR_PRIMARYTYPE = "jcr:primaryType";
    public static final String BOOLEAN_PREFIX = "{Boolean}";
    public static final String STRING_PREFIX = "{String}";
    public static final String DATE_PREFIX = "{Date}";
    public static final String DOUBLE_PREFIX = "{Double}";
    public static final String LONG_PREFIX = "{Long}";
    public static final String NAME_PREFIX = "{Name}";
    public static final String PATH_PREFIX = "{Path}";
    public static final String BINARY_PREFIX = "{Binary}";
    public static final String STRING_ARRAY_PREFIX = STRING_PREFIX + "[]";
    public static final String BOOLEAN_ARRAY_PREFIX = BOOLEAN_PREFIX + "[]";
    public static final String LONG_ARRAY_PREFIX = LONG_PREFIX + "[]";
    public static final String DOUBLE_ARRAY_PREFIX = DOUBLE_PREFIX + "[]";


    public static final String[] TYPESTRINGS = {
            STRING_PREFIX,
            BOOLEAN_PREFIX,
            DATE_PREFIX,
            DOUBLE_PREFIX,
            //NAME_PREFIX,
            //PATH_PREFIX,
            //BINARY_PREFIX,
            LONG_PREFIX,
            LONG_ARRAY_PREFIX,
            DOUBLE_ARRAY_PREFIX,
            BOOLEAN_ARRAY_PREFIX,
            STRING_ARRAY_PREFIX
    };

    private Object value;
    private String name;
    private String type;

    public AbstractProperty(String name, Object value, String type){
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public Object getValue(){
        return value;
    }

    public String getType(){
        return type;
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
