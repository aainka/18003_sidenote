package com.elg.uis;

/**
 * 
* <pre>
* <p> Title: WindowInterface.java </p>
* <p> Description: Winodw(â) Inteface</p>
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
     * Key�� �����ϴ� �������̽�
     * @return key
     */
	public int getKey();

    /**
     * â�� ������ ȣ��Ǵ� �������̽�
     */
	public void close();

	/**
	 * Dialog�� toFront�ϴ� �������̽�
	 */
	public void toFrontDialog();
}
