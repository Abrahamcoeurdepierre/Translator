package translator;

import java.io.IOException;

import com.darkprograms.speech.translator.GoogleTranslate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class Translator {
	
     private TranslatorInterface translatorInterface; 
     private TextEditor view;
     private List<Integer> list = new ArrayList<>();
     String text = "0";

    public Translator(TextEditor view) {
        this.view = view;
        this.view.getBtnSave().addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 save();
             }
             
         });
         this.view.getBtnLoad().addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 proses();
             }
         });
          this.view.getBtnTranslator().addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 translateOn();
                 
             }
         });
          
    }
    
     private void proses() {
         JFileChooser loadFile = view.getLoadFile();
             StyledDocument doc = view.getTxtPane().getStyledDocument();
             if (JFileChooser.APPROVE_OPTION == loadFile.showOpenDialog(view)) {
                 BufferedInputStream reader = null;
                 try {
                     reader = new BufferedInputStream(new FileInputStream(loadFile.getSelectedFile()));
                     doc.insertString(0, "", null);
                     int temp = 0;
                     List<Integer> list = new ArrayList<>();
                     while ((temp=reader.read()) != -1) {                    
                         list.add(temp);
                     }
                     
                     if (!list.isEmpty()) {
                         byte[] dt = new byte[list.size()];
                         int i = 0;
                         for (Integer integer : list) {
                             dt[i]=integer.byteValue();
                             i++;
                         }
                         doc.insertString(doc.getLength(), new String(dt), null);
                         JOptionPane.showMessageDialog(view, "File Loaded Successfuly", "Information", JOptionPane.INFORMATION_MESSAGE);
                     }
                 } catch (FileNotFoundException ex) {
                     Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
                 } catch (IOException | BadLocationException ex) {
                     Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
                 } finally {
                     if (reader != null) {
                         try {
                             reader.close();
                         } catch (IOException ex) {
                             Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                 }
             }
     }
     
      private void save() {
          JFileChooser loadFile = view.getLoadFile();
         if (JFileChooser.APPROVE_OPTION == loadFile.showSaveDialog(view)) {
             BufferedOutputStream writer = null;
             try {
                 String contents = view.getTxtPane().getText();
                 if (contents != null && !contents.isEmpty()) {
                     writer = new BufferedOutputStream(new FileOutputStream(loadFile.getSelectedFile()));
                     writer.write(contents.getBytes());
                     JOptionPane.showMessageDialog(view, "File saved Successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                 }

             } catch (FileNotFoundException ex) {
                 Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
             } finally {
                 if (writer != null) {
                     try {
                         writer.flush();
                         writer.close();
                         view.getTxtPane().setText("");
                     } catch (IOException ex) {
                         Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
             }
         }
     }
     private void translateOn(){
         translatorInterface = new TranslatorInterface();
         translatorInterface.setVisible(true);  
         
         this.translatorInterface.getBtnTranslate().addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 translateText();
             }
         });
         
          this.translatorInterface.getBtnAdd().addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 add();
             }
         });
         
     }
     
     private void translateOff(){
         translatorInterface.dispose();
     }
     
     private void translateText(){
         try {
              String textToTranslate  = translatorInterface.getTextPane().getText();
              text = GoogleTranslate.translate("fr", textToTranslate);
             translatorInterface.getTextPane().setText(text);
         } catch (IOException ex) {
             Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
     
     private void add(){
         try {
             StyledDocument doc = view.getTxtPane().getStyledDocument();
             if (text.equals("0")) {
                 JOptionPane.showMessageDialog(view, "Please insert a text to tranlsate", "Information", JOptionPane.INFORMATION_MESSAGE);
             }
             else{
                 doc.insertString(doc.getLength(), " " + text, null );
             }
             
         } catch (BadLocationException ex) {
             Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
         }
     }

}
