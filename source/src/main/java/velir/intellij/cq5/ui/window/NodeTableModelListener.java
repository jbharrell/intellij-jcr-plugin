package velir.intellij.cq5.ui.window;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * @author krasmussen
 */
public class NodeTableModelListener implements TableModelListener {

    public NodeTableModelListener(){

    }

    public void tableChanged(TableModelEvent e){
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
    }

}
