package CMMGUI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * MainFrame.java
 *
 * Created on 2015.12
 */

public class CMMmainFrame extends JFrame {
	static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	final static int Defualt_width = screenSize.width;
	final static int Defualt_height = screenSize.height;
	static int inputTabCounts;
	Font sans14 = new Font("SansSerif", Font.CENTER_BASELINE,14);
	Font sans16 = new Font("SansSerif", Font.PLAIN,15);
	
	private String cpRight="CopyRigth@ 朱超 徐航 方巧璐 刘梦舟";
	private String cpTime="in 2015-2016上学年";
	String[] outputString=new String[4];
	
	ByteArrayOutputStream baoStream;
	ByteArrayOutputStream errStream;
	
	/** Creates new form MainFrame */
    public CMMmainFrame() {
    	//set the title of the frame 
    	super.setTitle("IDE For CMM");
    	//set the outlook and file 
        String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
			UIManager.setLookAndFeel(plaf);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("fail to change the look and feel");
		}
        SwingUtilities.updateComponentTreeUI(this);
        inputTabCounts = 0;
    	initComponents();  
    	//close the frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //创建工程目录
        try{
        	File file = new File("D:\\CmmWorkspace");
        	if (!file.exists()  && !file.isDirectory()) {
				file.mkdir();
				System.out.println("新建根目录成功");
			}
        }catch(Exception e){
        	System.out.println("新建根目录出错");
        }
        
        baoStream = new ByteArrayOutputStream(1024);
   	    errStream = new ByteArrayOutputStream(1024);
   	    //重定向控制台输出
   	    PrintStream errStream1 = new PrintStream(errStream);
   	    PrintStream cacheStream = new PrintStream(baoStream);
   	    System.setOut(cacheStream);
   	    System.setErr(errStream1);
   	    
    }

    // Variables declaration - do not modify
    private JPanel mainPan;
    //MenuBar Menu MenuItem
    private JMenuBar MainMenuBar;
    private JMenu fileMenu;		//file Menu include open, save file and exit Menu 
    	private JMenuItem newProjectMenuItem;
    	private JMenuItem newFileMenuItem;
    	private JMenuItem openFileMenuItem;
    	private JMenuItem saveMenuItem;
    	private JMenuItem exitMenuItem;
    	private JMenuItem aboutMenuItem;
    private JMenu editMenu;		//edit Menu include the fundamental operation with the file
    	private JMenuItem slctAllMenuItem;
    	private JMenuItem copyMenuItem;
    	private JMenuItem cutMenuItem;
    	private JMenuItem pasteMenuItem;
    private JMenu viewMenu;		//view Menu include checkBox Menu of the edit and run panel
    	private JCheckBoxMenuItem toolbarOutputViewMenuItem;
    	private JCheckBoxMenuItem toolbarViewMenuItem;
    
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
    
    private FilePanel filePanel;
    
    //edit Panel
    private JPanel editPan;
    static JScrollPane divPan1;//the edit panel
    	static JTabbedPane inputTabPanel;
    	//save the text of inputTextPanel
    	static HashMap<InputPanel, String> inputText = new HashMap<>();
    	//save the file location of the inputTextPanel
    	static HashMap<InputPanel, String> inputFilePath = new HashMap<>();
    private JScrollPane divPan2;//the output panel
    	private JTabbedPane tabPanel;
    	private JPanel runPanel;
    	private JPanel midPanel;
    	private JPanel cifaPanel;
    	private JPanel yufaPanel;
    	private JTextPane outTextArea;
    	private TextArea midTextArea;
    	private TextArea cifaTextArea;
    	private TextArea yufaTextArea;
    private	JSplitPane splitPane;
    // End of variables declaration
    
	
    /** 
     * This method is called frame within the constructor 
     * to initialize the frame
     */
    private void initComponents() {
    	
    	//initialize Components
    	mainPan = new JPanel(new BorderLayout()); 
    	MainMenuBar = new JMenuBar();
    	fileMenu = new JMenu();	//file Menu include open, save file, about us and exit Menu 
    		newProjectMenuItem = new JMenuItem();
    		newFileMenuItem = new JMenuItem();
    		openFileMenuItem = new JMenuItem();
    		saveMenuItem = new JMenuItem();
    		aboutMenuItem = new JMenuItem();
    		exitMenuItem = new JMenuItem();
    	editMenu = new JMenu();	//edit Menu include the fundamental operation with the file
    		slctAllMenuItem = new JMenuItem();
    		copyMenuItem = new JMenuItem();
    		cutMenuItem = new JMenuItem();
            pasteMenuItem = new JMenuItem();
        viewMenu = new JMenu(); //view Menu include checkBox Menu of the edit and run panel
        	toolbarOutputViewMenuItem = new JCheckBoxMenuItem();
        	toolbarViewMenuItem = new JCheckBoxMenuItem();
       
        	cpDialog = new Dialog(this,"Copy Right");
            cpLabel = new Label(cpRight);
            tmLabel = new Label(cpTime);
            okButton = new JButton("OK");
            dPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            dPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            dPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolBar = new JToolBar(); //toolBar include run, stop, create and save file, undo and redo operation
            runButton = new JButton();runButton.setFocusPainted(false);
            stopButton = new JButton();stopButton.setFocusPainted(false);
            saveButton = new JButton();saveButton.setFocusPainted(false);
            newButton = new JButton();newButton.setFocusPainted(false);
            undoButton = new JButton();undoButton.setFocusPainted(false);
            redoButton = new JButton();redoButton.setFocusPainted(false);
        filePanel = new FilePanel();
        editPan = new JPanel();
        	editMenu = new JMenu();
        	divPan1 = new JScrollPane();
        		inputTabPanel = new JTabbedPane();
        		inputTabPanel.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        	divPan2 = new JScrollPane();
        		tabPanel = new JTabbedPane();
        		runPanel = new JPanel(new BorderLayout());
        	    midPanel = new JPanel(new BorderLayout());
        	    cifaPanel = new JPanel(new BorderLayout());
                yufaPanel = new JPanel(new BorderLayout());
                outTextArea = new JTextPane();
                //outTextArea.setText(baoStream.toString());
                outTextArea.setEditable(true);
                midTextArea = new TextArea();
                midTextArea.setEditable(false);
                cifaTextArea = new TextArea();
                cifaTextArea.setEditable(false);
                yufaTextArea = new TextArea();
                yufaTextArea.setEditable(false);
            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);  
        
        //set the size and location of the frame 
        setBounds(screenSize.width/6,screenSize.height/20,screenSize.width/3,screenSize.height/3);
        
        /**
         * set MenuBar
         */
        //fileMenu
        fileMenu.setText("文件");
        newFileMenuItem.setText("新建项目");
        newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
        newFileMenuItem.addActionListener(new MyMonitorListener());
        newFileMenuItem.setText("新建文件");
        newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
        newFileMenuItem.addActionListener(new MyMonitorListener());
        fileMenu.add(newFileMenuItem);
        openFileMenuItem.setText("打开");
        openFileMenuItem.addActionListener(new MyMonitorListener());
        fileMenu.add(openFileMenuItem);
        saveMenuItem.setText("保存");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
        saveMenuItem.addActionListener(new MyMonitorListener());
        fileMenu.add(saveMenuItem);
        aboutMenuItem.setText("关于");
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
        aboutMenuItem.addActionListener(new MyMonitorListener());
        fileMenu.add(aboutMenuItem);
        exitMenuItem.setText("退出");
        exitMenuItem.addActionListener(new MyMonitorListener());
        fileMenu.add(exitMenuItem);
        
        //editMenu
        editMenu.setText("编辑");
        slctAllMenuItem.setText("全选");
        //add keyStike event
        slctAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
        slctAllMenuItem.addActionListener(new MyMonitorListener());
        editMenu.add(slctAllMenuItem);
        copyMenuItem.setText("拷贝");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
        copyMenuItem.addActionListener(new MyMonitorListener());
        editMenu.add(copyMenuItem);
        cutMenuItem.setText("剪切");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
        cutMenuItem.addActionListener(new MyMonitorListener());
        editMenu.add(cutMenuItem);
        pasteMenuItem.setText("粘贴");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK));
        pasteMenuItem.addActionListener(new MyMonitorListener());
        editMenu.add(pasteMenuItem);
        //viewMenu
        viewMenu.setText("视图");
        toolbarViewMenuItem.setSelected(true);
        toolbarViewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_MASK));
        toolbarViewMenuItem.setText("工具栏视图");
        toolbarViewMenuItem.addActionListener(new MyMonitorListener());
        viewMenu.add(toolbarViewMenuItem);
        toolbarOutputViewMenuItem.setSelected(false);
        toolbarOutputViewMenuItem.setText("输出视图");
        toolbarOutputViewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
        toolbarOutputViewMenuItem.addActionListener(new MyMonitorListener());
        viewMenu.add(toolbarOutputViewMenuItem);

        MainMenuBar.add(fileMenu);
        MainMenuBar.add(editMenu);
        MainMenuBar.add(viewMenu);
        setJMenuBar(MainMenuBar);
        
        //copyDialog design for aboutMenuItem
        cpDialog.setLayout(new GridLayout(3,1));
        dPanel1.add(cpLabel);
        cpDialog.add(dPanel1);
        dPanel2.add(tmLabel);
        cpDialog.add(dPanel2);
        dPanel3.add(okButton);
        cpDialog.add(dPanel3);
        okButton.addActionListener(new MyMonitorListener());
        cpDialog.addWindowListener(new MyWdListener());
        cpDialog.pack();
        cpDialog.setLocation((screenSize.width-cpDialog.getWidth())/2,(screenSize.height-cpDialog.getHeight())/2);
        
        //mouse state on the toolBar panel
        toolBar.setRollover(true);
        toolBar.setOrientation(SwingConstants.HORIZONTAL);;
        runButton.setText("运行");// run button
        runButton.setIcon(new ImageIcon("iconLib/start.png"));
        runButton.addActionListener(new MyMonitorListener());
        toolBar.add(runButton);
        stopButton.setText("停止");//stop button
        stopButton.setIcon(new ImageIcon("iconLib/stop.png"));
        stopButton.setFocusable(false);
        stopButton.addActionListener(new MyMonitorListener());
        toolBar.add(stopButton);
        newButton.setText("新建");//new button
        newButton.setIcon(new ImageIcon("iconLib/new.png"));
        newButton.addActionListener(new MyMonitorListener());
        toolBar.add(newButton);
        saveButton.setText("保存");//save button
        saveButton.setIcon(new ImageIcon("iconLib/save.png"));
        saveButton.setFocusable(false);
        saveButton.addActionListener(new MyMonitorListener());
        toolBar.add(saveButton);     
        //unable to float
        toolBar.setFloatable(false);
        
        //editPan design
        editPan.setLayout(new BorderLayout());
        //input panel
        divPan1.setBorder(BorderFactory.createTitledBorder(null, "input", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        Dimension myDimension = new Dimension(screenSize.width/2-25, screenSize.height/3-50);
        divPan1.setPreferredSize(myDimension);
        //output panel
        divPan2.setBorder(BorderFactory.createTitledBorder(null,  "output", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        
        //set input panel
        divPan1.setViewportView(inputTabPanel);

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
        splitPane.setRequestFocusEnabled(false);
        splitPane.setTopComponent(divPan1);  
        editPan.add(splitPane);
        //manager the panels
        //use the border layout
        getContentPane().add(BorderLayout.CENTER, mainPan);
        mainPan.add(BorderLayout.NORTH, toolBar);
        mainPan.add(BorderLayout.WEST,filePanel);
        mainPan.add(BorderLayout.CENTER, editPan);

        pack();
    }
    
    private void runBtnDiv(){
    	//add output panel
		splitPane.setBottomComponent(divPan2);
		pack();
    }
    
    private void stopBtnDiv(){
    	if (divPan2.isShowing()) {
    		splitPane.remove(divPan2);
    		toolbarOutputViewMenuItem.setState(false);
    		pack();
		}
    }
    
    private void statusItem(){
    	if(toolbarViewMenuItem.getState()){
    		if(!toolBar.isShowing()){
    			mainPan.add(BorderLayout.NORTH, toolBar);
    			pack();
    		}
    	}
    	if(!toolbarViewMenuItem.getState()){
    		mainPan.remove(toolBar);
    		pack();
    	}
    }
    
    private void newProject(){
    	//创建工程目录
    	
        try{
        	File file = new File("D:\\CmmWorkspace");
        	if (!file.exists()  && !file.isDirectory()) {
				file.mkdir();
				System.out.println("新建根目录成功");
			}
        }catch(Exception e){
        	System.out.println("新建根目录出错");
        }
    }
    
    
    //new or open file 
    private void newAndOpenfile(){
    	try{
    		JFileChooser jFileChooser = new JFileChooser("D:\\CmmWorkspace");
    		FileNameExtensionFilter filter = new FileNameExtensionFilter("CMM文件", "cmm");
 	        jFileChooser.setFileFilter(filter);
    		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    		jFileChooser.showOpenDialog(editPan);
    		String filePath=jFileChooser.getSelectedFile().getAbsolutePath();
    		File file = new File(filePath);
    		if (!file.exists()) {
				file.createNewFile();
    		}
				mainPan.remove(filePanel);
				filePanel = new FilePanel();
				mainPan.add(BorderLayout.WEST,filePanel);
				pack();
    		
    		int fLength;
	        fLength = (int) file.length();
	        int num = 0;
	        FileReader fReader = new FileReader(file);
	        char[] date = new char[fLength];
	        while(fReader.ready()){
	             num += fReader.read(date, num, fLength - num);
	          }
	        fReader.close();
	 	    //show multiple documents
	        if (CMMmainFrame.inputTabCounts >= 0) {
        		int j = CMMmainFrame.inputTabCounts;
        		if (j!=0) {
        			for (int i = 0; i < CMMmainFrame.inputTabCounts; i++) {
        				//judge the filePath
	        			if ((CMMmainFrame.inputFilePath.get(CMMmainFrame.inputTabPanel.getComponentAt(i))).equals(filePath)) {
	        				CMMmainFrame.inputTabPanel.setSelectedIndex(i);
	        				j--;
						}
	        		}
				}
        		if (j==CMMmainFrame.inputTabCounts) {
        			InputPanel myInputPanel = new InputPanel(CMMmainFrame.inputTabPanel);
				    myInputPanel.addTabPanel(filePath);
				    String content = new String(date, 0, num);
					myInputPanel.getJTextPane().setText(content);
					CMMmainFrame.inputText.put(myInputPanel, content);
					CMMmainFrame.divPan1.setViewportView(CMMmainFrame.inputTabPanel);
				}
				
        }
    	}catch(Exception e){
    		System.err.println("create file error!");
    	}
    }
    
    //save the file
    private void saveFile()
    {
    	try{
    		int i = CMMmainFrame.inputTabPanel.getSelectedIndex();
    		if (i<0) {
    			return;
    		}
    		String filePath = inputFilePath.get(CMMmainFrame.inputTabPanel.getComponentAt(i));
    		if (filePath!=null) {
    			FileWriter fw=new FileWriter(filePath);
        		BufferedWriter bw=new BufferedWriter(fw);
        		PrintWriter pw=new PrintWriter(bw);
        		String content = ((InputPanel)CMMmainFrame.inputTabPanel.getComponentAt(i)).getCode();
        		inputText.put((InputPanel)CMMmainFrame.inputTabPanel.getComponentAt(i), content);
        		pw.print(content);
        		bw.close();
        		fw.close();
        		JOptionPane.showMessageDialog(null, "save success！");
			}
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
    	 int i = CMMmainFrame.inputTabPanel.getSelectedIndex();
    	 if (i<0) {
    		outTextArea.setText("you haven't choose a project");
    		return;
		 }
     	 String inText=((InputPanel)CMMmainFrame.inputTabPanel.getComponentAt(i)).getCode();
     	 outTextArea.setText(inText);
     	 try{
     	 	/**
     		 * 调用 CMM 解释器解释执行
     		 */
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
 		 }
    }
    
   	//the action listener of event  
    class MyMonitorListener implements ActionListener {

    	 public void actionPerformed(ActionEvent e){
    		 Object object = e.getSource();
    		 /**
    		  * judge the source of the actionEvent
    		  */
    		 //source from fileMenu
    		 if(object==openFileMenuItem){
    			 //open the file 
    			 newAndOpenfile();
    		 }
    		 if(object==newFileMenuItem){
    			 //open the file 
    			 newAndOpenfile();
    		 }
    		 else if(object==saveMenuItem||object==saveButton){
    			 //save the file 
    			 saveFile();
    		 }
    		 else if(object==exitMenuItem){
    			 //exit IDE for CMM
    			 System.exit(0);
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
    		 //source from copyRight
    		 else if(object==aboutMenuItem){
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
    			 outTextArea.setText("");
    		 }
    		 else if(object==newButton){
    			 newAndOpenfile();
    		 }
    		 else {
    			 return;
    		 }
    	}
    }
    
    class MyWdListener extends WindowAdapter{
    	public void windowClosing(WindowEvent e) {
    		if (e.getSource() == cpDialog)
    			cpDialog.setVisible(false);
    	}
    	public void windowStateChanged(WindowEvent e) {
			System.out.println("windows state change");
		}
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CMMmainFrame().setVisible(true);
                Console cons = System.console();  
           	 	if (cons != null) {  
                	// -------------------------  
           	 		PrintWriter printWriter = cons.writer();  
            	    printWriter.write("input:");  
            	    cons.flush();  
            	    // -------------------------  
            	    String str1 = cons.readLine();  
            	    // -------------------------  
            	    cons.format("%s", str1);  
            	    System.out.println(str1);
                   }  
            }
        });
        
    }

}

