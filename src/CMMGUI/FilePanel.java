package CMMGUI;

import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import com.sun.prism.Texture;

import sun.java2d.cmm.kcms.CMM;


public class FilePanel extends JPanel implements TreeSelectionListener{
	private JTree jTree = null;
	private JScrollPane jScrollPane = null;
	DefaultMutableTreeNode top = null; // decl-index=0

	public FilePanel(){
		super();
		jScrollPane = new JScrollPane();
		jScrollPane.setPreferredSize(new Dimension(150, CMMmainFrame.Defualt_height/2+CMMmainFrame.Defualt_height/4));
		jScrollPane.setViewportView(getJTree());
		this.add(jScrollPane);
	}
	
	/*
	final static void findDirectory(File dir) throws Exception{
		File[] fs = dir.listFiles();
		int projectCount=0;
		for(int i=0; i<fs.length; i++){
			if(fs[i].isDirectory()){
				//add parent node
				projectNode.add(new DefaultMutableTreeNode(fs[i].getName()));
				projectCount++;
				File[] projectFs = fs[i].listFiles();
				for(int j =0;j<projectFs.length;j++)
				{
					if (projectFs[j].getName().endsWith("cmm")) {
						//add leaf node
						new DefaultMutableTreeNode();
						projectNode.get(projectCount-1).add(new DefaultMutableTreeNode(projectFs[j].getName()));
						
					}
				}
			}
		}
	}
	*/
	
	final static void findDirectory(File dir,DefaultMutableTreeNode top) throws Exception{
		File[] fs = dir.listFiles();
		for(int i=0; i<fs.length; i++){
			if (fs[i].isFile()) {
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(fs[i].getName());
				top.add(fileNode);
			}
			if (fs[i].isDirectory()) {
				DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(fs[i].getName());
				top.add(projectNode);
				//递归调用获取文件夹下的文件
				DefaultMutableTreeNode projectTop = projectNode;
				findDirectory(fs[i],projectTop);
			}
		}
	}
	
	private JTree getJTree() {
		if (jTree == null) {
			top = new DefaultMutableTreeNode("CmmWorkspace");
			File path = new File("D:\\CmmWorkspace");
			try {
				findDirectory(path,top);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jTree = new JTree(top);
			jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTree.addTreeSelectionListener(this);
	    }
		return jTree;
	}

	
	//open the file 
	void openfile(String parentName,String leafName){
	
		String filePath = "D:\\"+parentName+leafName;
		File file = new File(filePath);
		if (!leafName.endsWith(".cmm")||!file.exists()) {
			JOptionPane.showMessageDialog(null, "该文件不能被打开");
			return;
		}
		try {
			int fLength;
	        fLength = (int) file.length();
	        int num = 0;
	        FileReader fReader = new FileReader(file);
	        char[] date = new char[fLength];
	        while(fReader.ready()){
	             num += fReader.read(date, num, fLength - num);
	          }
	        fReader.close();
	        if (CMMmainFrame.inputTabCounts>=0) {
	        		int j = CMMmainFrame.inputTabCounts;
	        		if (j!=0) {
	        			for (int i = 0; i < CMMmainFrame.inputTabCounts; i++) {
		        			if (CMMmainFrame.inputFilePath.get(CMMmainFrame.inputTabPanel.getComponentAt(i)).equals(filePath)) {
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
		} catch (Exception e) {
			System.err.println("open file err in the filePanel");
		}
	}
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree) e.getSource();
		tree.getLastSelectedPathComponent();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
		if (node == null)
			// Nothing is selected.
			return;
		if(node.isRoot()){
			System.out.println(node.toString());
			System.err.println("root chenggong");
		}
		if (node.isLeaf()) {
			String leafName = node.toString();
			String[] parentName = new String[5];
			for (int j = 0; j < parentName.length; j++) {
				parentName[j]="";
			}
			int i=0;
			while (node.getParent()!=null) {
				node = (DefaultMutableTreeNode) node.getParent();
				parentName[i]= node.toString()+"\\";
				i++;
			}
			String parentNames="";
			for (int j = 0; j < parentName.length; j++) {
				parentNames =parentName[j]+parentNames;
			}
			System.out.println(parentNames);
			System.err.println(leafName);
			openfile(parentNames, leafName);
		}
	}
}
