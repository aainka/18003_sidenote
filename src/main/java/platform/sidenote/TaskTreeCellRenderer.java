package platform.sidenote;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import platform.sidenote.util.Debug;

public class TaskTreeCellRenderer extends DefaultTreeCellRenderer {

	Debug logger = Debug.getLogger(this.getClass());
	OT_Popup popup = new OT_Popup();
	private Color colorSelected = new Color(184, 207, 229);
	private Color colorTitle = new Color(128, 255, 128);
	private Color colorPriority = new Color(253, 240, 225);

	public TaskTreeCellRenderer() {
		// this.setOpaque(true);
		this.setBackgroundSelectionColor(colorSelected);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		JComponent comp = (JComponent) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row,
				hasFocus);

		comp.setOpaque(false);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject().getClass() != String.class) {
			OV_Task task = (OV_Task) node.getUserObject();

			if (selected || hasFocus) {
				comp.setBackground(colorSelected);
			} else {
				if (task.priority == 0) {
					comp.setBackground(Color.WHITE);
				} else {
					if (task.priority > 5) {
						comp.setOpaque(true);
						comp.setBackground(colorTitle);
					} else {
						comp.setBackground(colorPriority);
					}
				}
			}
		}
		return comp;
	}

}
