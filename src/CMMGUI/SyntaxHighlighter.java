package CMMGUI;
import java.awt.Color;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.print.DocFlavor.STRING;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/** 
 *	high lighter the keywords
 */
public class SyntaxHighlighter implements DocumentListener {
	private Set<String> keywords;
	private Hashtable<String,String> comments;
	private Style keywordStyle;
	private Style normalStyle;
	private Style digitStyle;
	private InputPanel inputPanel;
	private JTextPane inputTextPanel;
	private JTextPane rowNumPanel;
	public SyntaxHighlighter(InputPanel inputPanel,JTextPane inputTextPanel,JTextPane rowNumPanel) {
		keywordStyle = ((StyledDocument) inputTextPanel.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) inputTextPanel.getDocument()).addStyle("Keyword_Style", null);
		digitStyle = ((StyledDocument) inputTextPanel.getDocument()).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(keywordStyle, Color.ORANGE.darker());
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		StyleConstants.setForeground(digitStyle,Color.GREEN.darker());
		keywords = new HashSet<String>();
		comments = new Hashtable<>();
		comments.put("//", "\n");
		comments.put("/*", "*/");
		keywords.add("if");
		keywords.add("else");
		keywords.add("while");
		keywords.add("write");
		keywords.add("read");
		keywords.add("int");
		keywords.add("real");
		keywords.add("void");
		
		this.inputTextPanel = inputTextPanel;
		this.rowNumPanel = rowNumPanel;
		this.inputPanel = inputPanel;
	}

	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);
		char ch;
		try {
			while (start < end) {
				ch = getCharAt(doc, start);
				if (Character.isLetter(ch)||Character.isDigit(ch)||ch == '_'||ch=='/'||ch=='*'||ch=='\t'||ch=='\n') {
						start = colouringWord(doc, start);
				}
				else {
					SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
					++start;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("错了");
		}
		
	}
	
	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);
		System.out.println(word);
		if (keywords.contains(word)) {
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
		} else {
			if (word.startsWith("/*")&&(word.endsWith("*/"))) {
				System.out.println("多行注释");
			}
			else if(word.startsWith("//")&&(word.endsWith("\n"))){
				System.out.println("单行注释");
			}
			else{
			//if the word is consist of digits, we should paint it as digitStyle
			char[] digitchars = word.toCharArray();
			int i=0;
			for (int j = 0; j < digitchars.length; j++) {
				if (Character.isDigit(digitchars[i])) {
					i++;
				}
			}
			if (i==digitchars.length){
				SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, digitStyle));
			}else{
				SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
			}
		}
			}
		return wordEnd;
	}
	
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		char inputchar = doc.getText(pos, 1).charAt(0);
		return inputchar;
	}
	
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);
		return pos;
	}
	
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		for (; isWordCharacter(doc, pos); ++pos);
		return pos;
	}
	
	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		if (Character.isLetter(ch)||Character.isDigit(ch)||ch == '_'||ch=='/'||ch=='*'||ch=='\t'||ch=='\n') { return true; }
		return false;
	}
	public boolean isDigitCharacter(Document doc, int pos) throws BadLocationException{
		char ch = getCharAt(doc, pos);
		if (Character.isDigit(ch)) return true;
		return false;
	}
	
	public int enterCharacterNum(Document doc,int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		int i =0;
		if (ch == 10) {
			i++;
		}
		return i;
	}

	public void showRowNum(int count){
		String rowNum = "";
		for (int i = 1; i <= count; i++) {
			rowNum+=i+"\n";
		}
		rowNumPanel.setText(rowNum);
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		try {
			//e.getOffset() return the position of cursor 
			//e.getLenght() return the insert words each time
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
			//show the row number
			Element root = inputTextPanel.getDocument().getDefaultRootElement();
			int count = root.getElementCount();
			showRowNum(count);
			inputPanel.setCode(inputTextPanel.getText());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
			Element root = inputTextPanel.getDocument().getDefaultRootElement();
			int count = root.getElementCount();
			showRowNum(count);
			inputPanel.setCode(inputTextPanel.getText());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
	
	private class ColouringTask implements Runnable {
		private StyledDocument doc;
		private Style style;
		private int pos;
		private int len;
		public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
			this.doc = doc;
			this.pos = pos;
			this.len = len;
			this.style = style;
		}
		public void run() {
			try {
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
}