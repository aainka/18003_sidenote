package com.elg.uis;

import java.util.Vector;

/**
 * 
* <pre>
* <p> Title: OpenWindowManager.java </p>
* <p> Description: 창을 관리하는 Manager 클래스</p>
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
     * WindowInterface를 등록한다. 
     * @param view WindowInterface
     * @return 등록여부 (true : 성공, false : 창 존재하여 등록 실패)
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
     * WinodwInterface를 제거한다.
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
     * 모든 창을 닫는다.
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
     * key에 해당하는 WinodwInterface를 반환한다.
     * @param key WinodwInterface 구분자
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
