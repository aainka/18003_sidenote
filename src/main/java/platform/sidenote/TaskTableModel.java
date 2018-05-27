package platform.sidenote;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TaskTableModel implements TableModel {
	
	List<OV_Task> list = new LinkedList<OV_Task>();

	public TaskTableModel(TaskTreeController taskTreeController) {
		// TODO Auto-generated constructor stub
	}

	public void buildData(List<OV_Task> treeList) {
		list.clear();
		 for ( OV_Task t : treeList) {
			 if ( t.priority > 0 && t.priority < 6) {
				 list.add(t);
			 }
		 }
		
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getColumnName(int arg0) {
		// TODO Auto-generated method stub
		return "Today's Todo";
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getValueAt(int row, int arg1) {
		// TODO Auto-generated method stub
		return list.get(row);
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
