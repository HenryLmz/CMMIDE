package CMMGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class InputPanel extends JPanel{ 
	private JTabbedPane inputTabPanel;
    JTextPane inputTextPanel;
    String code;
	Font sans14 = new Font("SansSerif", Font.CENTER_BASELINE,14);
	
	public void setCode(String code){
		this.code = code;
	}
	public String getCode(){
		return this.code;
	}
	
	public InputPanel(JTabbedPane inputTabPanel){
		super(new BorderLayout());
		this.inputTabPanel = inputTabPanel;	
		code = "";
	}
	
	//初始化带有关闭按钮的标签头部
	private void initTabComponent() {
		//用这个函数可以初始化标签的头部
		for (int i = 0; i < CMMmainFrame.inputTabCounts; i++) {
			inputTabPanel.setTabComponentAt(i, new InputTabComponent(inputTabPanel));
		}
	}                                
	
	void addTabPanel(String filename){
		CMMmainFrame.inputTabCounts++;
		JTextPane rowNumPanel = new JTextPane();
		JTextPane inputTextPanel = new JTextPane();
		rowNumPanel.setEditable(false);
		//high lighter the key words and show the line number for the inputTextPanel
		inputTextPanel.getDocument().addDocumentListener(new SyntaxHighlighter(this,inputTextPanel, rowNumPanel));
		this.add(rowNumPanel,BorderLayout.WEST);
		this.add(inputTextPanel,BorderLayout.CENTER);
		
		//put inpuyPanel into the hashMap
		CMMmainFrame.inputFilePath.put(this, filename);
		String name = (new File(filename)).getName();
        
		//change the color and font of the inputText
        rowNumPanel.setBackground(Color.lightGray);
        rowNumPanel.setFont(sans14);
		rowNumPanel.setPreferredSize(new Dimension(25, CMMmainFrame.screenSize.height/3-50));
        rowNumPanel.setAlignmentX(SwingConstants.CENTER);
        inputTextPanel.setFont(sans14);
		//创建标签
        inputTabPanel.addTab(name,this);
        inputTabPanel.setSelectedComponent(this);
        //初始化标签上的文字和Button
        initTabComponent();
	} 
	
	public JTextPane getJTextPane(){
		inputTextPanel = (JTextPane) this.getComponent(1);
		return inputTextPanel;
	}
}
