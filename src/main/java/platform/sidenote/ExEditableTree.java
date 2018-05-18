package platform.sidenote;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class ExEditableTree extends JFrame {

    public static void main(String... s) {
        new ExEditableTree();
    }

    private JTree t;

    public ExEditableTree() {
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void init() {
        t = new JTree();
        t.setEditable(true);
        t.setCellEditor(getEditor());
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("edit");
        item.addActionListener(getEditActionListener());
        menu.add(item);
        t.setComponentPopupMenu(menu);
        t.addMouseListener(getMouseListener());
        add(new JScrollPane(t));
    }

    private TreeCellEditor getEditor() {
        return new DefaultTreeCellEditor(t, (DefaultTreeCellRenderer) t.getCellRenderer()){
            @Override
            public Component getTreeCellEditorComponent(JTree tree,
                    Object value, boolean isSelected, boolean expanded,
                    boolean leaf, int row) {
                System.out.println("editing ");
                return super.getTreeCellEditorComponent(tree, value, isSelected, expanded,leaf, row);
            }
        };
    }


    private MouseListener getMouseListener() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3){
                    int rowForLocation = t.getRowForLocation(e.getPoint().x, e.getPoint().y);
                    if(rowForLocation != -1){
                        t.setSelectionRow(rowForLocation);
                    }
                }
            }
        };
    }


    private ActionListener getEditActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath selectionPath = t.getSelectionPath();
                t.startEditingAtPath(selectionPath);
            }
        };
    }

}