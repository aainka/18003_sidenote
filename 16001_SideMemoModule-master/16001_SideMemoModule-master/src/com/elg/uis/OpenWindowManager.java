package com.elg.uis;

import java.util.Vector;

/**
 * 
* <pre>
* <p> Title: OpenWindowManager.java </p>
* <p> Description: â�� �����ϴ� Manager Ŭ����</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class OpenWindowManager
{
    private static volatile OpenWindowManager openWindowManager = new OpenWindowManager();

    private Vector<WindowInterface> openWindowVector = new Vector<WindowInterface>();
    
    public static OpenWindowManager getInstance() {
    	if (null == openWindowManager) {
			synchronized (OpenWindowManager.class) {
				
				if(null == openWindowManager) {
					openWindowManager = new OpenWindowManager();
				}	
			}
		}
    	
    	return openWindowManager;
    }

    /**
     * WindowInterface�� ����Ѵ�. 
     * @param view WindowInterface
     * @return ��Ͽ��� (true : ����, false : â �����Ͽ� ��� ����)
     */
    public boolean add(WindowInterface view) {
        WindowInterface windowInterface = getWindowInterface(view.getKey());
        if (null != windowInterface) {
            return false;
        }

        openWindowVector.add(view);
        
        return true;
    }

    /**
     * WinodwInterface�� �����Ѵ�.
     * @param view WinodwInterface
     */
    public void remove(WindowInterface view) {
        try {
            openWindowVector.remove(view);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��� â�� �ݴ´�.
     */
    public void closeAllWindow() {
        try {
            for (int i = openWindowVector.size() - 1; i >= 0; i--) {
                WindowInterface emsWindowInterface = (WindowInterface) openWindowVector.get(i);

                emsWindowInterface.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * key�� �ش��ϴ� WinodwInterface�� ��ȯ�Ѵ�.
     * @param key WinodwInterface ������
     * @return WinodwInterface
     */
    public WindowInterface getWindowInterface(int key) {
    	
        for (int i = 0; i < openWindowVector.size(); i++) {
            WindowInterface emsWindowInterface = (WindowInterface) openWindowVector.get(i);

            if (emsWindowInterface.getKey() == key) {
                return emsWindowInterface;
            }
        }
        return null;
    }
}
