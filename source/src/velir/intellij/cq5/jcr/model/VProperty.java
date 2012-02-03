package velir.intellij.cq5.jcr.model;

/**
 * @author krasmussen
 */
public interface VProperty {
    public String getName();

    public Object getValue();

    public String getType();

    public Class getValueClass();

    public boolean isAutoCreated();

    public boolean isMandatory();

    public boolean isProtected();

    public boolean isMultiple();

}
