package CMMGUI;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/** 
 *	high lighter the keywords
 */
public class SyntaxHighlighter implements DocumentListener {
	private Set<String> keywords;
	private Style keywordStyle;
	private Style normalStyle;
	private Style digitStyle;

	public SyntaxHighlighter(JTextPane editor) {
		keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		digitStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(keywordStyle, Color.ORANGE.darker());
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		StyleConstants.setForeground(digitStyle,Color.GREEN.darker());
		
		keywords = new HashSet<String>();
		keywords.add("if");
		keywords.add("else");
		keywords.add("while");
		keywords.add("write");
		keywords.add("read");
		keywords.add("int");
		keywords.add("real");

	}


	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);


	char ch;
	
	if(Character.isDigit(getCharAt(doc, start))){
		for(int i=0;i<len;i++)	
		SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, digitStyle));
		start=start+len;
	}

	while (start < end) {
		ch = getCharAt(doc, start);
		if (Character.isLetter(ch)) {
			start = colouringWord(doc, start);
		} else {
			SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
			++start;
		}
	}

}




	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);
		if (keywords.contains(word)) {
			
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
		} else {
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
		}
		return wordEnd;

	}
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
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
		if (Character.isLetter(ch)) { return true; }
		return false;
	}




	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}




	@Override
	public void insertUpdate(DocumentEvent e) {
		try {
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
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