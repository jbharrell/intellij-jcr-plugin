package velir.intellij.cq5.ui.window;

import velir.intellij.cq5.jcr.model.VNode;
import velir.intellij.cq5.jcr.model.VProperty;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author krasmussen
 */
public class NodeTableModel extends AbstractTableModel {
    private String[] columnNames = { "Name" , "Value" , "Type" }; // , "Auto Created" , "Mandatory" , "Protected" , "Multiple"};
    private VNode vNode;
    String[] props;
    public NodeTableModel(VNode vNode1){
        vNode = vNode1;
        props = vNode.getSortedPropertyNames();

        this.addTableModelListener( new NodeTableModelListener() );
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }
    public int getRowCount() {
        return props.length;
    }
    public int getColumnCount() {
        return columnNames.length;
    }
    public Object getValueAt(int row, int col) {
        String propName = props[row];
        VProperty vProp = vNode.getProperty(propName);
        if(col == 0){
            return vProp.getName();
        } else if(col == 1){
            Object value = vProp.getValue();
            String retValue = "";
            if(value instanceof Object[]){
                retValue = Arrays.toString((Object[])value);
            } else if(value instanceof Date){
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                retValue = df.format(value);
            } else {
                retValue = value.toString();
            }
            return retValue;
        } else {
            return vProp.getType();
        }
    }
    public boolean isCellEditable(int row, int col) {
        return col == 2;
    }
    public void setValueAt(Object value, int row, int col) {
        //rowData[row][col] = value;
        //fireTableCellUpdated(row, col);
    }
}
