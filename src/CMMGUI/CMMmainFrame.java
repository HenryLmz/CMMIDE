package CMMGUI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

/*
 * MainFrame.java
 *
 * Created on 2015.12
 */

public class CMMmainFrame extends JFrame {
	static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	final static int Defualt_width = screenSize.width;
	final static int Defualt_height = screenSize.height;
	
	Font sans16 = new Font("SansSerif", Font.BOLD,16);
	
	private String cpRight="CopyRigth@ 朱超 徐航 方巧璐 刘梦舟";
	private String cpTime="in 2015-2016上学年";
	String[] outputString=new String[4];
	/** Creates new form MainFrame */
    public CMMmainFrame() {
    	//set the title of the frame 
    	super.setTitle("IDE For CMM");
        initComponents();  
    }

    /** 
     * This method is called frame within the constructor 
     * to initialize the frame
     */
    private void initComponents() {
    	
    	//initialize Components
        editPan = new JPanel();
        editor = new JTextPane();
        editor.setText("");
        toolBar = new JToolBar();
        runButton = new JButton();
        saveButton = new JButton();
        newButton = new JButton();
        MainMenuBar = new JMenuBar();
        fileMenu = new JMenu();
        openFileMenuItem = new JMenuItem();  
        saveMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        editMenu = new JMenu();
        copyMenuItem = new JMenuItem();
        cutMenuItem = new JMenuItem();
        pasteMenuItem = new JMenuItem();
        copyRightMenuItem =new JMenuItem();
        copyRightMenu = new JMenu();
        viewMenu = new JMenu();
        toolbarViewMenuItem = new JCheckBoxMenuItem();
        toolbarOutputViewMenuItem = new JCheckBoxMenuItem();
        rowMarkedViewMenuItem = new JCheckBoxMenuItem();
        outTextArea = new TextArea();
        outTextArea.setEditable(false);
        midTextArea = new TextArea();
        midTextArea.setEditable(false);
        cifaTextArea = new TextArea();
        cifaTextArea.setEditable(false);
        yufaTextArea = new TextArea();
        yufaTextArea.setEditable(false);
        stopButton = new JButton();
        divPan1 = new JScrollPane();
        divPan2 = new JScrollPane();
        cpDialog = new Dialog(this,"Copy Right");
        cpLabel = new Label(cpRight);
        tmLabel = new Label(cpTime);
        okButton = new JButton("OK");
        dPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        runPanel = new JPanel(new BorderLayout());
        midPanel = new JPanel(new BorderLayout());
        cifaPanel = new JPanel(new BorderLayout());
        yufaPanel = new JPanel(new BorderLayout());
        tabPanel = new JTabbedPane();
        undoButton = new JButton();
        redoButton = new JButton();
        undoMenuItem = new JMenuItem();
        redoMenuItem = new JMenuItem(); 
        udm = new UndoManager();
        rowNumPan = new JPanel(new BorderLayout());
        inPan = new JPanel(new BorderLayout());
        inTextPan = new JPanel(new BorderLayout());
        slctAllMenuItem = new JMenuItem();
        mainPan = new JPanel(new BorderLayout());
        lookAndFeelMenu = new JMenu();
        metalMenuItem = new JMenuItem();
        nimbusMenuItem = new JMenuItem();
        windowsMenuItem = new JMenuItem();
        
        
        tabInputPanel = new JTabbedPane();
        //inPanels = new ArrayList<JPanel>();
        //inTextPanels = new ArrayList<JPanel>();
        //rowNumPanels = new ArrayList<JPanel>();
        //show the rows of code
        Integer[] in;
        in=new Integer[100];
        for(int i=0;i<100;i++){
        	in[i]=i+1;
        }
        numList = new JList<Integer>(in);
        numList.setForeground(Color.BLUE);
        numList.setFont(sans16);
        
        //close the frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //set the size and location of the frame 
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width/5,screenSize.height/5,screenSize.width/3,screenSize.height/3);
        
        //copyDialog design
        cpDialog.setLayout(new GridLayout(3,1));
        dPanel1.add(cpLabel);
        cpDialog.add(dPanel1);
        dPanel2.add(tmLabel);
        cpDialog.add(dPanel2);
        dPanel3.add(okButton);
        cpDialog.add(dPanel3);
        okButton.addActionListener(new MyMonitor());
        cpDialog.addWindowListener(new MyWdListener());
        cpDialog.pack();
        cpDialog.setLocation((screenSize.width-cpDialog.getWidth())/2,(screenSize.height-cpDialog.getHeight())/2);
        
        //editPan design
        editPan.setLayout(new BorderLayout());
        //show the
        //divPan1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //input panel
        divPan1.setBorder(BorderFactory.createTitledBorder(null, "input", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        Dimension myDimension = new Dimension(screenSize.width/2-25, screenSize.height/2-50);
        divPan1.setPreferredSize(myDimension);
        //output panel
        divPan2.setBorder(BorderFactory.createTitledBorder(null,  "output", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        
        //set the input panel
        inTextPan.add(inPan);
        tabInputPanel.addTab("input", inTextPan);
        rowNumPan.add(numList);
        inPan.add(BorderLayout.WEST,rowNumPan);
        //rowNumPan.setBackground(Color.blue);
    	inPan.add(BorderLayout.CENTER,editor);
    	//editor.setBackground(Color.red);
    	divPan1.setViewportView(tabInputPanel);
    	
        //high lighter the key words
        editor.setFont(sans16);
        editor.getDocument().addDocumentListener(new SyntaxHighlighter(editor));
        
    
        //undo operation
        editor.getDocument().addUndoableEditListener(new UndoableEditListener() {
        	public void undoableEditHappened(UndoableEditEvent e) {
        		udm.addEdit(e.getEdit());
        	}
        });
       
        //runPanel
        runPanel.add(outTextArea);
        tabPanel.addTab("运行结果",runPanel);
        cifaPanel.add(cifaTextArea);
        tabPanel.add("词法分析",cifaPanel);
        yufaPanel.add(yufaTextArea);
        tabPanel.add("语法分析",yufaPanel);
        midPanel.add(midTextArea);
        tabPanel.add("中间代码",midPanel);
        divPan2.setViewportView(tabPanel);
        
        //only add divPan1 at the beginning
        editPan.add(divPan1,BorderLayout.CENTER);
        
        //mouse state on the toolBar panel
        toolBar.setRollover(true);
       
        runButton.setText("运行");
        runButton.setIcon(new ImageIcon("iconLib/start.png"));
        runButton.addActionListener(new MyMonitor());
        toolBar.add(runButton);
        
        stopButton.setText("停止");
        stopButton.setIcon(new ImageIcon("iconLib/stop.png"));
        stopButton.setFocusable(false);
        stopButton.addActionListener(new MyMonitor());
        toolBar.add(stopButton);
        
       
        
        newButton.setText("新建");
        newButton.setIcon(new ImageIcon("iconLib/new.png"));
        newButton.addActionListener(new MyMonitor());
        toolBar.add(newButton);

        saveButton.setText("保存");
        saveButton.setIcon(new ImageIcon("iconLib/save.png"));
        saveButton.setFocusable(false);
        saveButton.addActionListener(new MyMonitor());
        toolBar.add(saveButton);
        
        undoButton.setText("撤销");
        undoButton.setIcon(new ImageIcon("iconLib/undo.png"));
        undoButton.setFocusable(false);
        undoButton.addActionListener(new MyMonitor());
        toolBar.add(undoButton);
        
        redoButton.setText("重做");
        redoButton.setIcon(new ImageIcon("iconLib/redo.png"));
        redoButton.setFocusable(false);
        redoButton.addActionListener(new MyMonitor());
        toolBar.add(redoButton);
        //unable to float
        toolBar.setFloatable(true);
        /**
         * set MenuBar
         */
        //fileMenu
        fileMenu.setText("文件");

        openFileMenuItem.setText("打开");
        openFileMenuItem.addActionListener(new MyMonitor());
        fileMenu.add(openFileMenuItem);
        saveMenuItem.setText("保存");
        saveMenuItem.addActionListener(new MyMonitor());
        fileMenu.add(saveMenuItem);
        exitMenuItem.setText("退出");
        exitMenuItem.addActionListener(new MyMonitor());
        fileMenu.add(exitMenuItem);
        
        //editMenu
        editMenu.setText("编辑");
        
        slctAllMenuItem.setText("全选          Ctrl+A");
        slctAllMenuItem.addActionListener(new MyMonitor());
        editMenu.add(slctAllMenuItem);
        copyMenuItem.setText("拷贝          Ctrl+C");
        copyMenuItem.addActionListener(new MyMonitor());
        editMenu.add(copyMenuItem);
        cutMenuItem.setText("剪切          Ctrl+X");
        cutMenuItem.addActionListener(new MyMonitor());
        editMenu.add(cutMenuItem);        
        pasteMenuItem.setText("粘贴          Ctrl+V");
        pasteMenuItem.addActionListener(new MyMonitor());
        editMenu.add(pasteMenuItem);
        undoMenuItem.setText("撤销          Ctrl+Z");
        undoMenuItem.addActionListener(new MyMonitor());
        editMenu.add(undoMenuItem);
        redoMenuItem.setText("重做          Ctrl+Y");
        redoMenuItem.addActionListener(new MyMonitor());
        editMenu.add(redoMenuItem);

        //viewMenu
        viewMenu.setText("视图");

        toolbarViewMenuItem.setSelected(true);
        toolbarViewMenuItem.setText("工具栏视图");
        toolbarViewMenuItem.addActionListener(new MyMonitor());
        viewMenu.add(toolbarViewMenuItem);
        toolbarOutputViewMenuItem.setSelected(false);
        toolbarOutputViewMenuItem.setText("输出视图");
        toolbarOutputViewMenuItem.addActionListener(new MyMonitor());
        viewMenu.add(toolbarOutputViewMenuItem);
        rowMarkedViewMenuItem.setSelected(true);
        rowMarkedViewMenuItem.setText("显示行号");
        rowMarkedViewMenuItem.addActionListener(new MyMonitor());
        viewMenu.add(rowMarkedViewMenuItem);
        
        //lookAndFeelMenu
        lookAndFeelMenu.setText("外观");
        metalMenuItem.setText("Metal");
        metalMenuItem.addActionListener(new MyMonitor());
        lookAndFeelMenu.add(metalMenuItem);
        nimbusMenuItem.setText("Nimbus");
        nimbusMenuItem.addActionListener(new MyMonitor());
        lookAndFeelMenu.add(nimbusMenuItem);
        windowsMenuItem.setText("Windows");
        windowsMenuItem.addActionListener(new MyMonitor());
        lookAndFeelMenu.add(windowsMenuItem);
        
        //copyRightMenu
        copyRightMenu.setText("版权");
        copyRightMenuItem.setText("CopyRight");
        copyRightMenuItem.addActionListener(new MyMonitor());
        copyRightMenu.add(copyRightMenuItem);
        
        MainMenuBar.add(fileMenu);
        MainMenuBar.add(editMenu);
        MainMenuBar.add(viewMenu);
        MainMenuBar.add(lookAndFeelMenu);
        MainMenuBar.add(copyRightMenu);
        setJMenuBar(MainMenuBar);
        
        //manager the panels
        //use the border layout
        getContentPane().add(BorderLayout.CENTER, mainPan);
        mainPan.add(BorderLayout.NORTH, toolBar);
        mainPan.add(BorderLayout.CENTER, editPan);
        pack();
      
    }
    
    private void runBtnDiv(){
    	editPan.add(divPan2,BorderLayout.SOUTH);
    	pack();
    }
    
    private void stopBtnDiv(){
    	if (divPan2.isShowing()) {
    		editPan.remove(divPan2);
    		toolbarOutputViewMenuItem.setState(false);
    		pack();
		}
    }
    
    private void numViewOut(){
    	if (!rowNumPan.isShowing()) {
    		inPan.add(BorderLayout.WEST,rowNumPan);
        	pack();
		}
    }
   
    private void numViewHide(){
    	if (rowNumPan.isShowing()) {
    		inPan.remove(rowNumPan);
        	pack();
		}
    }
    
    private void statusItem(){
    	if(toolbarViewMenuItem.getState()){
    		//initialGroupLayout();
    		if(!toolBar.isShowing()){
    			mainPan.add(BorderLayout.NORTH, toolBar);
    			pack();
    			repaint();
    		}
    	}
    	if(!toolbarViewMenuItem.getState()){
    		mainPan.remove(toolBar);
    		pack();
    		repaint();
    	}
    }
    
    //open the file 
    private void openFile()
	  {
    	try{
    		JFileChooser jFileChooser = new JFileChooser();
    		//filter the document of cmm
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("CMM文件", "cmm");
	        jFileChooser.setFileFilter(filter);
	        jFileChooser.showOpenDialog(editPan);
	        String fileName =null; 
	        fileName =jFileChooser.getSelectedFile().getPath();
	        File file = new File(fileName);
	        String shortName = file.getName();
	        int fLength;
	        fLength = (int) file.length();
	        int num = 0;
	        FileReader fReader = new FileReader(file);
	        char[] date = new char[fLength];
	        while(fReader.ready()){
	             num += fReader.read(date, num, fLength - num);
	          }
	        fReader.close();
	        if (tabInputPanel.getTitleAt(0).equals("input")) {
	        	//change the title of input panel
	        	tabInputPanel.setTitleAt(0, shortName);
	        	editor.setText(new String(date, 0, num));
	        	repaint();
			}
	        
	        /**
	        * show multiple documents
	        */
	        /*  else {
	        	System.out.println("文件存在");
	        	try {
	        		JPanel newInPan = new JPanel();
	        		JPanel newRowPan = new JPanel();
	        		newRowPan.add(numList);
		            newInPan.add(BorderLayout.WEST,newRowPan);
		            newInPan.add(BorderLayout.CENTER,editor);
		        	//inTextPanels.add(1,newInPan);
		            tabInputPanel.addTab(shortName, newInPan);
		        	divPan1.setViewportView(tabInputPanel);
		        	repaint();
		        	System.out.println("成功");
				} catch (Exception e) {
					System.out.println("出错");
				}
	        	
			}	         */   
	      }catch(Exception e){
	    	  System.err.println("open file error");
	    	  return;
	      }
	  }
    
    //save the file
    private void saveFile()
    {
    	try{
    		JFileChooser jFileChooser = new JFileChooser();
    		FileNameExtensionFilter filter = new FileNameExtensionFilter("CMM文件", "cmm");
 	        jFileChooser.setFileFilter(filter);
 	        jFileChooser.showSaveDialog(editPan);
    		String filePath=jFileChooser.getSelectedFile().getAbsolutePath();
    		FileWriter fw=new FileWriter(filePath);
    		BufferedWriter bw=new BufferedWriter(fw);
    		PrintWriter pw=new PrintWriter(bw); 
    		pw.print(editor.getText());
    		bw.close();
    		fw.close();
    	}catch(Exception e){
    		System.err.println("save file error");
    		return;
    	}
     }
    
    void output(){
    	outTextArea.setForeground(Color.BLACK);
    	midTextArea.setForeground(Color.BLACK);
    	cifaTextArea.setForeground(Color.BLACK);
    	yufaTextArea.setForeground(Color.BLACK);
    	String inText=editor.getText();
    	System.out.println(inText);
    	/*try{
    		*//**
    		 * 调用 CMM 解释器解释执行
    		 *//*
    		//outputString=InterpretFourFounction.CMMAllAnalyse(inText,null);
    		//outTextArea.setText(outputString[2]);
    		//cifaTextArea.setText(outputString[0]);
    		//yufaTextArea.setText(outputString[1]);
    		//midTextArea.setText(outputString[3]);
    	}catch(Exception exce){
    		outTextArea.setForeground(Color.RED);
    		outTextArea.setText(exce.toString());
    		midTextArea.setForeground(Color.RED);
    		midTextArea.setText(exce.toString());
    		cifaTextArea.setForeground(Color.RED);
    		cifaTextArea.setText(exce.toString());
    		yufaTextArea.setForeground(Color.RED);
    		yufaTextArea.setText(exce.toString());
		 }*/
    }
  
    /**
     * change the model of frame
     * @param plaf
     */
    private void changeLookAndFeel(String plaf)
    {
    	UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
    	for (int i = 0; i<infos.length; i++){
    		String plafName = infos[i].getName();
    		if (plaf.equals(plafName)) {
    			try
    			{
    				UIManager.setLookAndFeel(infos[i].getClassName());
    				SwingUtilities.updateComponentTreeUI(CMMmainFrame.this);
    				//pack();
    				repaint();
    			}catch (Exception e){
    				e.printStackTrace();
    				System.out.println(plaf);
    			}
    			return;
			}
    	}
    }  
  
   
   	//the action listener of event  
    class MyMonitor implements ActionListener {

    	 public void actionPerformed(ActionEvent e){
    		 Object object = e.getSource();
    		 /**
    		  * judge the source of the actionEvent
    		  */
    		 //source from fileMenu
    		 if(object==openFileMenuItem){
    			 //open the file 
    			 openFile();
    		 }
    		 else if(object==saveMenuItem||object==saveButton){
    			 //save the file 
    			 saveFile();
    		 }
    		 else if(object==exitMenuItem){
    			 //exit IDE for CMM
    			 System.exit(0);
    		 }
    		 //source from editMenu
    		 else if(object==slctAllMenuItem){
    			 editor.selectAll();
    		 }
    		 else if(object==copyMenuItem){
    			 editor.copy();
    		 }
    		 else if(object==cutMenuItem){
    			 editor.cut();
    		 }
    		 else if(object==pasteMenuItem){
    			 editor.paste();
    		 }
    		 else if(object==redoButton||object==redoMenuItem){
    			 RedoAction.redo(udm);
    		 }
    		 else if(object==undoButton||object==undoMenuItem){
    			 UndoAction.undo(udm);
    		 }
    		 //source from viewMenu
    		 else if(object==toolbarViewMenuItem){
    			 statusItem();
    		 }
    		 else if(object==toolbarOutputViewMenuItem){
    			 if(toolbarOutputViewMenuItem.getState())
    				 runBtnDiv();
    			 else
    				 stopBtnDiv();
    		 }
    		 else if(object==rowMarkedViewMenuItem){
    			 if(rowMarkedViewMenuItem.getState())
    				 numViewOut();
    			 else
    				 numViewHide();
    		 }
    		 //source from look and feel
    		 else if(object==metalMenuItem){
    			 
    			 String plaf = "Metal";
    			 changeLookAndFeel(plaf);
    		 }
    		 else if(object==nimbusMenuItem){
    			 String plaf = "Nimbus";
    			 changeLookAndFeel(plaf);
    		 }
    		 else if(object == windowsMenuItem){
    			 String plaf = "Windows";
    			 changeLookAndFeel(plaf);
    		 }
    		 //source from copyRight
    		 else if(object==copyRightMenuItem){
    			 cpDialog.setVisible(true);
    		 }
    		 else if(object==okButton){
    			 cpDialog.setVisible(false);
    		 }
    		 //source from run button
    		 else if(object==runButton){
    			 runBtnDiv();
    			 output();
    		 }
    		 //source from stop button
    		 else if(object==stopButton){
    			 stopBtnDiv();
    			 //how to play
    		 }
    		 else if(object==newButton){
    			 //stopBtnDiv();
    			 //editor.setText("");
    			 openFile();
    		 }
    		 else {
    			 return;
    		 }
    	}
    }
    
    class MyWdListener extends WindowAdapter{
    	public void windowClosing(WindowEvent e) {
    		cpDialog.setVisible(false);
    	}

    }
    
    class MyKeyListener extends KeyAdapter{
    	public void keyTyped(KeyEvent e){
    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
    			
    		}

    	}
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CMMmainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    //MenuBar Menu MenuItem
    private JMenuBar MainMenuBar;
    private JMenu fileMenu;		//file Menu include open, save file and exit Menu 
    	private JMenuItem openFileMenuItem;
    	private JMenuItem saveMenuItem;
    	private JMenuItem exitMenuItem;
    private JMenu editMenu;		//edit Menu include the fundamental operation with the file
    	private JMenuItem slctAllMenuItem;
    	private JMenuItem copyMenuItem;
    	private JMenuItem cutMenuItem;
    	private JMenuItem pasteMenuItem;
    	private JMenuItem undoMenuItem;
    	private JMenuItem redoMenuItem;
    private JMenu viewMenu;		//view Menu include checkBox Menu of the edit and run panel
    	private JCheckBoxMenuItem toolbarOutputViewMenuItem;
    	private JCheckBoxMenuItem toolbarViewMenuItem;
    	private JCheckBoxMenuItem rowMarkedViewMenuItem;
    private JMenu lookAndFeelMenu;//lookAndFeel Menu include three types of outlook Menu
    	private JMenuItem metalMenuItem;
    	private JMenuItem nimbusMenuItem;
    	private JMenuItem windowsMenuItem;
    private JMenu copyRightMenu;//copyRight Menu include the information of our team
    	private JMenuItem copyRightMenuItem;
    		//copyRightframe
    		private Dialog cpDialog;
    		private Label cpLabel;
    		private Label tmLabel;
    	    private JButton okButton;
    	    private JPanel dPanel1;
    	    private JPanel dPanel2;
    	    private JPanel dPanel3;
    	
    //toolBar
    private JToolBar toolBar;	//toolBar include run, stop, create and save file, undo and redo operation
    	private JButton runButton;
    	private JButton stopButton;
    	private JButton newButton;
    	private JButton saveButton;
    	private JButton undoButton;
    	private JButton redoButton;
    
    //edit Panel
    private JPanel editPan;
    private JScrollPane divPan1;//the edit panel
    	private JPanel inPan;
    	//private ArrayList<JPanel> inPanels ;
    	private JTabbedPane tabInputPanel;
    	//show the row number
    	private JPanel rowNumPan;
    	//private ArrayList<JPanel> rowNumPanels ;
    	private JList<Integer> numList;
    	//the input panel
    	private JPanel inTextPan;
    	//private ArrayList<JPanel> inTextPanels ;
    	private JTextPane editor;
    private JScrollPane divPan2;//the output panel
    	private JTabbedPane tabPanel;
    	private JPanel runPanel;
    	private JPanel midPanel;
    	private JPanel cifaPanel;
    	private JPanel yufaPanel;
    	private TextArea outTextArea;
    	private TextArea midTextArea;
    	private TextArea cifaTextArea;
    	private TextArea yufaTextArea;

    private UndoManager udm;
    
    
    private JPanel mainPan;

   // End of variables declaration

}

class UndoAction {
	public static void undo(UndoManager undoManager) {
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
	}
}

class RedoAction {
	public static void redo(UndoManager undoManager) {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}
}


