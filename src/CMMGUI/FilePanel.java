package CMMGUI;

import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;


public class FilePanel extends JPanel implements TreeSelectionListener{
	private JTree jTree = null;
	private JScrollPane jScrollPane = null;
	DefaultMutableTreeNode top = null; // decl-index=0
	static ArrayList<DefaultMutableTreeNode> projectNode;
	static ArrayList<DefaultMutableTreeNode> fileNode;
	public FilePanel(){
		super();
		projectNode = new ArrayList<DefaultMutableTreeNode>();
		fileNode = new ArrayList<DefaultMutableTreeNode>();
		jScrollPane = new JScrollPane();
		jScrollPane.setPreferredSize(new Dimension(150, CMMmainFrame.Defualt_height/2+CMMmainFrame.Defualt_height/4));
		jScrollPane.setViewportView(getJTree());
		this.add(jScrollPane);
	}

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
						projectNode.get(projectCount-1).add(new DefaultMutableTreeNode(projectFs[j].getName()));
						
					}
				}
			}
		}
	}
	
	private JTree getJTree() {
		if (jTree == null) {
			top = new DefaultMutableTreeNode("CmmWorkspace");
			File path = new File("D:\\CmmWorkspace");
			try {
				findDirectory(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			createNodes(top);
			jTree = new JTree(top);
			jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			jTree.addTreeSelectionListener(this);
	    }
		return jTree;
	}

	private void createNodes(DefaultMutableTreeNode top) {
		for (int i = 0; i < projectNode.size(); i++) {
			top.add(projectNode.get(i));
		}
	}
	
	//open the file 
	void openfile(String parentName,String leafName){
		String filePath = "D:\\CmmWorkspace\\"+parentName+"\\"+leafName;
		File file = new File(filePath);
		if (!leafName.endsWith(".cmm")||!file.exists()) {
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
			System.err.println("root chenggong");
		}
		if (node.isLeaf()) {
			String parentName = node.getParent().toString();
			String leafName = node.toString();
			openfile(parentName, leafName);
		}
	}
}
