package com.yec.ContactSensor_Demo_NBIoTDevice;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class App {

	/**{
     * 创建并显示GUI。出于线程安全的考虑，  
     * 这个方法在事件调用线程中调用。  Unit Test
     */
    private static void createAndShowGUI() {
        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // 添加 "Hello World" 标签
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);
        // 显示窗口
        //frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//byte[] lst = Utilty.getInstance().float2byte(-1.654f);
		System.out.println("Hello World!");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

}
