package com.elg.uis;

/**
 * 
* <pre>
* <p> Title: WindowInterface.java </p>
* <p> Description: Winodw(창) Inteface</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public interface WindowInterface
{
    /**
     * Key를 리턴하는 인터페이스
     * @return key
     */
	public int getKey();

    /**
     * 창을 닫을때 호출되는 인퍼테이스
     */
	public void close();

	/**
	 * Dialog를 toFront하는 인터페이스
	 */
	public void toFrontDialog();
}
