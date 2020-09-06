package com.elg.uis;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * 
* <pre>
* <p> Title: AbstractTreeTransferHandler.java </p>
* <p> Description: Drag/Drap Transfer Handler를 추상 클래스</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public abstract class AbstractTreeTransferHandler implements DragGestureListener, DragSourceListener, DropTargetListener {

     protected DNDTree tree;
     private DragSource dragSource; // dragsource
     private DropTarget dropTarget; //droptarget
     private static DefaultMutableTreeNode draggedNode; 
     private DefaultMutableTreeNode draggedNodeParent; 
     private static BufferedImage image = null; //buff image
     private Rectangle rect2D = new Rectangle();
     private boolean drawImage;

     /**
      * 생성자
      * @param tree JTreer 객체
      * @param action action(DnDConstants.ACTION_COPY_OR_MOVE)
      * @param drawIcon 아이콘 표시여부 
      */
     protected AbstractTreeTransferHandler(DNDTree tree, int action, boolean drawIcon) {
          this.tree = tree;
          drawImage = drawIcon;
          dragSource = new DragSource();
          dragSource.createDefaultDragGestureRecognizer(tree, action, this);
          dropTarget = new DropTarget(tree, action, this);
     }

     /* Methods for DragGestureListener */
     @Override
     public final void dragGestureRecognized(DragGestureEvent dge) {
    	 TreePath path = tree.getSelectionPath(); 
          if (path != null) { 
               draggedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
               draggedNodeParent = (DefaultMutableTreeNode)draggedNode.getParent();
               if (drawImage) {
                    Rectangle pathBounds = tree.getPathBounds(path); //getpathbounds of selectionpath
                   
                    JComponent lbl = (JComponent)tree.getCellRenderer().getTreeCellRendererComponent(tree, draggedNode, false , tree.isExpanded(path),((DefaultTreeModel)tree.getModel()).isLeaf(path.getLastPathComponent()), 0,false);//returning the label
                    lbl.setBounds(pathBounds);//setting bounds to lbl
                    image = new BufferedImage(lbl.getWidth(), lbl.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);//buffered image reference passing the label's ht and width
                    Graphics2D graphics = image.createGraphics();//creating the graphics for buffered image
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));     //Sets the Composite for the Graphics2D context
                    lbl.paint(graphics); //painting the graphics to label
                   
                    graphics.dispose();                    
               }
               dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop , image, new Point(0,0), new TransferableNode(draggedNode), this);               
          }      
     }
     
     
     /* Methods for DragSourceListener */
     @Override
     public void dragDropEnd(DragSourceDropEvent dsde) {
    	 if (dsde.getDropSuccess() && dsde.getDropAction()==DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
    		 int value = tree.getVerticalScrollBarValue();	
    		 ArrayList<TreePath> extendList = tree.getExtandRows();
    		 ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(draggedNodeParent);                    
    		 tree.setExtandRows(extendList, value); 
    	 }
     }

     @Override
     public final void dragEnter(DragSourceDragEvent dsde)  {
    	 int action = dsde.getDropAction();
          if (action == DnDConstants.ACTION_COPY)  {
               dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
          } 
          else {
               if (action == DnDConstants.ACTION_MOVE) {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
               } 
               else {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
               }
          }
     }
     
     @Override
     public final void dragOver(DragSourceDragEvent dsde) {
    	 int action = dsde.getDropAction();
          if (action == DnDConstants.ACTION_COPY) {
               dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
          } 
          else  {
               if (action == DnDConstants.ACTION_MOVE) {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
               } 
               else  {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
               }
          }
     }
    
     @Override
     public final void dropActionChanged(DragSourceDragEvent dsde)  {
    	 int action = dsde.getDropAction();
          if (action == DnDConstants.ACTION_COPY) {
               dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
          }
          else  {
               if (action == DnDConstants.ACTION_MOVE) {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
               } 
               else {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
               }
          }
     }
   
     @Override
     public final void dragExit(DragSourceEvent dse) {
    	 dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
     }     
          
   

     /* Methods for DropTargetListener */
     @Override
     public final void dragEnter(DropTargetDragEvent dtde) {
    	 Point pt = dtde.getLocation();
          int action = dtde.getDropAction();
          if (drawImage) {
               paintImage(pt);
          }
          if (canPerformAction(tree, draggedNode, action, pt)) {
               dtde.acceptDrag(action);               
          }
          else {
               dtde.rejectDrag();
          }
     }

     @Override
     public final void dragExit(DropTargetEvent dte) {
          
    	 if (drawImage) {
               clearImage();
          }
     }

     @Override
     public final void dragOver(DropTargetDragEvent dtde) {
         
    	 Point pt = dtde.getLocation();
          int action = dtde.getDropAction();
          tree.autoscroll(pt);
          if (drawImage) {
               paintImage(pt);
          }
          if (canPerformAction(tree, draggedNode, action, pt)) {
               dtde.acceptDrag(action);               
          }
          else {
               dtde.rejectDrag();
          }
     }

     @Override
     public final void dropActionChanged(DropTargetDragEvent dtde) {
    	 Point pt = dtde.getLocation();
          int action = dtde.getDropAction();
          if (drawImage) {
               paintImage(pt);
          }
          if (canPerformAction(tree, draggedNode, action, pt)) {
               dtde.acceptDrag(action);               
          }
          else {
               dtde.rejectDrag();
          }
     }

     @Override
     public final void drop(DropTargetDropEvent dtde) {
          
    	 try {
               if (drawImage) {
                    clearImage();
               }
               int action = dtde.getDropAction();
               Transferable transferable = dtde.getTransferable();
               Point pt = dtde.getLocation();
               if (transferable.isDataFlavorSupported(TransferableNode.NODE_FLAVOR) && canPerformAction(tree, draggedNode, action, pt)) {
                    TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) transferable.getTransferData(TransferableNode.NODE_FLAVOR);
                    DefaultMutableTreeNode newParentNode =(DefaultMutableTreeNode)pathTarget.getLastPathComponent();
                    if (executeDrop(tree, node, newParentNode, action)) {
                         dtde.acceptDrop(action);                    
                         dtde.dropComplete(true);
                       //File에 write한다.
             			String filePath = MemoFrame.FILE_PATH;
             			String fileName = tree.getFileName();
             			
             			FileManager.getInstance().write(filePath, fileName, tree.getModel());
                       
             			return;                         
                    }
               }
               dtde.rejectDrop();
               dtde.dropComplete(false);
          }          
          catch (Exception e) {     
               e.printStackTrace();	
               dtde.rejectDrop();
               dtde.dropComplete(false);
          }     
     }
     
     private final void paintImage(Point pt) {
          tree.paintImmediately(rect2D.getBounds());
          rect2D.setRect((int) pt.getX(),(int) pt.getY(),image.getWidth(),image.getHeight());
          tree.getGraphics().drawImage(image,(int) pt.getX(),(int) pt.getY(),tree);
     }

     private final void clearImage() {
          tree.paintImmediately(rect2D.getBounds());
     }

     public abstract boolean canPerformAction(DNDTree target, DefaultMutableTreeNode draggedNode, int action, Point location);

     public abstract boolean executeDrop(DNDTree tree, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode newParentNode, int action);
}