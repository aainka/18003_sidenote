package com.elg.uis;

import java.awt.datatransfer.*;
import javax.swing.tree.*;
import java.util.*;

/**
 * 
* <pre>
* <p> Title: TransferableNode.java </p>
* <p> Description: Drag/Drap Transfer Wapper Å¬·¡½º</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class TransferableNode implements Transferable {
     public static final DataFlavor NODE_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Node");
     private DefaultMutableTreeNode node;
     private DataFlavor[] flavors = { NODE_FLAVOR };

     public TransferableNode(DefaultMutableTreeNode nd) {
          node = nd;
     }  

     public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
          if (flavor == NODE_FLAVOR) {
               return node;
          }
          else {
               throw new UnsupportedFlavorException(flavor);     
          }               
     }

     public DataFlavor[] getTransferDataFlavors() {
          return flavors;
     }

     public boolean isDataFlavorSupported(DataFlavor flavor) {
          return Arrays.asList(flavors).contains(flavor);
     }
}