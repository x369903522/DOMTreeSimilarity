/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package treesim;

import treesim.ElementNode;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.io.SAXReader;

/**
 *
 * @author teoman
 */
public class TreeSim extends JFrame implements ActionListener
{
	private static SAXReader xmlReader = new SAXReader();
	private static List<String> elementNodes = new ArrayList<String>();
	private static List<String> elementNodes2 = new ArrayList<String>();
	private static List<String> textNodeConnections = new ArrayList<String>();
	private static List<String> textNodeConnections2 = new ArrayList<String>();
	private static List<String> attributeParentNodes = new ArrayList<String>();
	private static List<String> attributeParentNodes2 = new ArrayList<String>();
	private static List<String> attributeNodes = new ArrayList<String>();
	private static List<String> attributeNodes2 = new ArrayList<String>();
	private static double elementSimilarityRatio;
	private static double textSimilarityRatio;
	private static double attributeSimilarityRatio;
	private static double overallSimilarityRatio;
	private static List<Integer> elementFrequenciesList = new ArrayList<Integer>();
	private static List<Integer> textOwnersFrequenciesList = new ArrayList<Integer>();
	private static List<Integer> attributeFrequenciesList = new ArrayList<Integer>();
	private static List<Integer> attributeParentFrequenciesList = new ArrayList<Integer>();
	private static Document doc;
	private static Document doc2;
	private static JLabel pathLabel1;
	private static JLabel pathLabel2;
	private static JLabel elementSimResultLabel;
	private static JLabel textSimResultLabel;
	private static JLabel attributeSimResultLabel;
	private static JLabel overallSimResultLabel;
	private static JFileChooser chooseFirstFile = new JFileChooser();
	private static File firstFile;
	private static JFileChooser chooseSecondFile = new JFileChooser();
	private static File secondFile;
	private static FileFilter filter = new FileNameExtensionFilter("HTML Files", new String[] {"html", "htm"});
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DocumentException 
    {    
    	new TreeSim().setVisible(true);
    }
    
    private TreeSim()
    {
    	super("DOM Similarity Evaluator");
    	setSize(600,200);
    	setResizable(false);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setLayout(new FlowLayout());
    	
    	JButton loadButton1 = new JButton("Load the First HTML File");
    	loadButton1.setActionCommand("load_first_html");
    	loadButton1.addActionListener(this);
    	add(loadButton1);
    	
    	JLabel path1Is = new JLabel(": ");
    	pathLabel1 = new JLabel();
    	add(path1Is);
    	add(pathLabel1);
    	
    	JButton loadButton2 = new JButton("Load the Second HTML File");
    	loadButton2.setActionCommand("load_second_html");
    	loadButton2.addActionListener(this);
    	add(loadButton2);
    	
    	JLabel path2Is = new JLabel(": ");
    	pathLabel2 = new JLabel();
    	add(path2Is);
    	add(pathLabel2);
    	
    	JButton simButton = new JButton("Calculate the Similarity");
    	simButton.setActionCommand("calculate_similarity");
    	simButton.addActionListener(this);
    	add(simButton);
    	
    	JLabel elementSimilarity = new JLabel("Element Similarity (%): ");
    	elementSimResultLabel = new JLabel();
    	add(elementSimilarity);
    	add(elementSimResultLabel);
    	
    	JLabel textSimilarity = new JLabel("Text Similarity (%): ");
    	textSimResultLabel = new JLabel();
    	add(textSimilarity);
    	add(textSimResultLabel);
    	
    	JLabel attributeSimilarity = new JLabel("Attribute Similarity (%): ");
    	attributeSimResultLabel = new JLabel();
    	add(attributeSimilarity);
    	add(attributeSimResultLabel);
    	
    	JLabel overallSimilarity = new JLabel("Overall Similarity (%): ");
    	overallSimResultLabel = new JLabel();
    	add(overallSimilarity);
    	add(overallSimResultLabel);
    	
    	JButton exitButton = new JButton("Quit the Application");
    	exitButton.setActionCommand("exit");
    	exitButton.addActionListener(this);
    	add(exitButton);
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	String actionCommand = e.getActionCommand();
    	
    	if(actionCommand.equals("exit"))
    	{
    		int reply = JOptionPane.showConfirmDialog(null, "Are you sure that you want to quit the application?", "Exit?",  JOptionPane.YES_NO_OPTION);
    		
    		if (reply == JOptionPane.YES_OPTION)
    		{
    		   System.exit(0);
    		}
    	}
    	
    	if(actionCommand.equals("load_first_html"))
    	{
    		chooseFirstFile.setFileFilter(filter);
    		
    		if(chooseFirstFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
    		{
    			firstFile = chooseFirstFile.getSelectedFile();
    			
    			try {
    				doc = xmlReader.read(new File(firstFile.getPath()));
    			} catch (DocumentException e3) {
    				e3.printStackTrace();
    			}
    			
    			pathLabel1.setText(String.valueOf(firstFile.getPath()));
    		}
    	}
    	
    	if(actionCommand.equals("load_second_html"))
    	{
    		chooseSecondFile.setFileFilter(filter);
    		
    		if(chooseSecondFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
    		{
    			secondFile = chooseSecondFile.getSelectedFile();
    			
    			try {
    				doc2 = xmlReader.read(new File(secondFile.getPath()));
    			} catch (DocumentException e4) {
    				e4.printStackTrace();
    			}
    			
    			pathLabel2.setText(String.valueOf(secondFile.getPath()));
    		}
    	}
    	
    	if(actionCommand.equals("calculate_similarity"))
    	{
    		System.out.println("The DOM Tree of the First HTML File:");
            System.out.println("\n\ngraph G {");
            
            @SuppressWarnings("rawtypes")
    		Iterator it = doc.nodeIterator();
            
            while(it.hasNext())
            {
            	preParentOrderFirstDOM((Node)it.next());
            }
                
            System.out.println("}");
            
            System.out.println("\n\nThe DOM Tree of the Second HTML File:");
            System.out.println("\n\ngraph G {");
            
            @SuppressWarnings("rawtypes")
            Iterator it2 = doc2.nodeIterator();
            
            while(it2.hasNext())
            {
            	preParentOrderSecondDOM((Node)it2.next());	
            }
            
            System.out.println("}");
            
            ElementNode[] elements_freq_FirstDOM = extractUniqueElements(elementNodes);
            ElementNode[] elements_freq_SecondDOM = extractUniqueElements(elementNodes2);
            
            System.out.println("\n\n");
            if(elements_freq_FirstDOM.length < elements_freq_SecondDOM.length)
            {
            	for(int i=0; i<elements_freq_SecondDOM.length; i++)
    	        {
    	        	if(commonAvailabilityCheckerforElements(elements_freq_SecondDOM[i], elements_freq_FirstDOM, elements_freq_SecondDOM) == true)
    	        	{
    	        		for(ElementNode _e : elements_freq_FirstDOM)
    	        		{
    	        			if(_e.getElementName() == elements_freq_SecondDOM[i].getElementName())
    	        			{
    	        				if(_e.getElementFrequency() < elements_freq_SecondDOM[i].getElementFrequency())
    	        				{
    	        					elementFrequenciesList.add(_e.getElementFrequency());
    	        				}
    	        				else
    	        				{
    	        					elementFrequenciesList.add(elements_freq_SecondDOM[i].getElementFrequency());
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            else
            {
            	for(int i=0; i<elements_freq_FirstDOM.length; i++)
    	        {
    	        	if(commonAvailabilityCheckerforElements(elements_freq_FirstDOM[i], elements_freq_FirstDOM, elements_freq_SecondDOM) == true)
    	        	{
    	        		for(ElementNode _e : elements_freq_SecondDOM)
    	        		{
    	        			if(_e.getElementName() == elements_freq_FirstDOM[i].getElementName())
    	        			{
    	        				if(_e.getElementFrequency() < elements_freq_FirstDOM[i].getElementFrequency())
    	        				{
    	        					elementFrequenciesList.add(_e.getElementFrequency());
    	        				}
    	        				else
    	        				{
    	        					elementFrequenciesList.add(elements_freq_FirstDOM[i].getElementFrequency());
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            
            int freqSum = 0;
            
            if(elementNodes.size() < elementNodes2.size())
            {
            	for(int i : elementFrequenciesList)
            	{
            		freqSum += i;
            	}
            	
            	elementSimilarityRatio = (double)(100*(freqSum) / elementNodes2.size());
            }
            else
            {
            	for(int i : elementFrequenciesList)
            	{
            		freqSum += i;
            	}
            	
            	elementSimilarityRatio = (double)(100*(freqSum) / elementNodes.size());
            }
            
            elementSimResultLabel.setText(String.valueOf(elementSimilarityRatio));
            
            TextNode[] textOwners_freq_FirstDOM = extractUniqueTextOwners(textNodeConnections);
            TextNode[] textOwners_freq_SecondDOM = extractUniqueTextOwners(textNodeConnections2);
            
            if(textOwners_freq_FirstDOM.length < textOwners_freq_SecondDOM.length)
            {
            	for(int i=0; i<textOwners_freq_SecondDOM.length; i++)
    	        {
    	        	if(commonAvailabilityCheckerforTextOwners(textOwners_freq_SecondDOM[i], textOwners_freq_FirstDOM, textOwners_freq_SecondDOM) == true)
    	        	{
    	        		for(TextNode _t : textOwners_freq_FirstDOM)
    	        		{
    	        			if(_t.getParentElementNodeHavingText() == textOwners_freq_SecondDOM[i].getParentElementNodeHavingText())
    	        			{
    	        				if(_t.getParentElementFrequency() < textOwners_freq_SecondDOM[i].getParentElementFrequency())
    	        				{
    	        					textOwnersFrequenciesList.add(_t.getParentElementFrequency());
    	        				}
    	        				else
    	        				{
    	        					textOwnersFrequenciesList.add(textOwners_freq_SecondDOM[i].getParentElementFrequency());
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            else
            {
            	for(int i=0; i<textOwners_freq_FirstDOM.length; i++)
    	        {
    	        	if(commonAvailabilityCheckerforTextOwners(textOwners_freq_FirstDOM[i], textOwners_freq_FirstDOM, textOwners_freq_SecondDOM) == true)
    	        	{
    	        		for(TextNode _t : textOwners_freq_SecondDOM)
    	        		{
    	        			if(_t.getParentElementNodeHavingText() == textOwners_freq_FirstDOM[i].getParentElementNodeHavingText())
    	        			{
    	        				if(_t.getParentElementFrequency() < textOwners_freq_FirstDOM[i].getParentElementFrequency())
    	        				{
    	        					textOwnersFrequenciesList.add(_t.getParentElementFrequency());
    	        				}
    	        				else
    	        				{
    	        					textOwnersFrequenciesList.add(textOwners_freq_FirstDOM[i].getParentElementFrequency());
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            
            int freqSum2 = 0;
            
            if(textNodeConnections.size() < textNodeConnections2.size())
            {
            	for(int i : textOwnersFrequenciesList)
            	{
            		freqSum2 += i;
            	}
            	
            	textSimilarityRatio = (double)(100*(freqSum2) / textNodeConnections2.size());
            }
            else
            {
            	for(int i : textOwnersFrequenciesList)
            	{
            		freqSum2 += i;
            	}
            	
            	textSimilarityRatio = (double)(100*(freqSum2) / textNodeConnections.size());
            }
            
            textSimResultLabel.setText(String.valueOf(textSimilarityRatio));
            
            AttributeNode[] attributes_freq_FirstDOM = extractUniqueAttributes(attributeNodes);
            AttributeNode[] attributes_freq_SecondDOM = extractUniqueAttributes(attributeNodes2);
            
            if(attributes_freq_FirstDOM.length < attributes_freq_SecondDOM.length)
            {
            	for(int i=0; i<attributes_freq_SecondDOM.length; i++)
    	        {
    	        	if(commonAvailabilityCheckerforAttributes(attributes_freq_SecondDOM[i], attributes_freq_FirstDOM, attributes_freq_SecondDOM) == true)
    	        	{
    	        		for(AttributeNode _a : attributes_freq_FirstDOM)
    	        		{
    	        			if(_a.getAttributeName() == attributes_freq_SecondDOM[i].getAttributeName())
    	        			{
    	        				if(_a.getAttributeFrequency() < attributes_freq_SecondDOM[i].getAttributeFrequency())
    	        				{
    	        					attributeFrequenciesList.add(_a.getAttributeFrequency());
    	        				}
    	        				else
    	        				{
    	        					attributeFrequenciesList.add(attributes_freq_SecondDOM[i].getAttributeFrequency());
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            else
            {
            	for(int i=0; i<attributes_freq_FirstDOM.length; i++)
    	        {
    	        	if(commonAvailabilityCheckerforAttributes(attributes_freq_FirstDOM[i], attributes_freq_FirstDOM, attributes_freq_SecondDOM) == true)
    	        	{
    	        		//System.out.printf(textOwners_freq_FirstDOM[i].elementName + "-" + textOwners_freq_FirstDOM[i].elementFrequency + " ");
    	        		
    	        		for(AttributeNode _a : attributes_freq_SecondDOM)
    	        		{
    	        			if(_a.getAttributeName() == attributes_freq_FirstDOM[i].getAttributeName())
    	        			{
    	        				if(_a.getAttributeFrequency() < attributes_freq_FirstDOM[i].getAttributeFrequency())
    	        				{
    	        					attributeFrequenciesList.add(_a.getAttributeFrequency());
    	        				}
    	        				else
    	        				{
    	        					attributeFrequenciesList.add(attributes_freq_FirstDOM[i].getAttributeFrequency());
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            
            int freqSum3 = 0;
            double subRatio1;
            
            if(attributeNodes.size() < attributeNodes2.size())
            {
            	for(int i : attributeFrequenciesList)
            	{
            		freqSum3 += i;
            	}
            	
            	subRatio1 = (double)(100*(freqSum3) / attributeNodes2.size());
            }
            else
            {
            	for(int i : attributeFrequenciesList)
            	{
            		freqSum3 += i;
            	}
            	
            	subRatio1 = (double)(100*(freqSum3) / attributeNodes.size());
            }
            
            Set<String> _unique_attributeParentNames = new HashSet<String>(attributeParentNodes);
            Set<String> _unique_attributeParentNames2 = new HashSet<String>(attributeParentNodes2);
            
            List<String> unique_attributeParentNames = new ArrayList<String>();
            List<String> unique_attributeParentNames2 = new ArrayList<String>();
            
            for(String s : _unique_attributeParentNames)
            {
            	unique_attributeParentNames.add(s);
            }
            
            for(String s2 : _unique_attributeParentNames2)
            {
            	unique_attributeParentNames2.add(s2);
            }
            
            if(unique_attributeParentNames.size() < unique_attributeParentNames2.size())
            {
            	for(int i=0; i<unique_attributeParentNames2.size(); i++)
            	{
            		if(commonAvailabilityCheckerforAttributeParents(unique_attributeParentNames2.get(i), unique_attributeParentNames, unique_attributeParentNames2) == true)
            		{
            			for(String s : unique_attributeParentNames)
            			{
            				if(s.equals(unique_attributeParentNames2.get(i)))
            				{
            					if((Collections.frequency(unique_attributeParentNames2, s) < Collections.frequency(unique_attributeParentNames, s)))
            					{
            						attributeParentFrequenciesList.add(Collections.frequency(unique_attributeParentNames2, s));
            					}
            					else
            					{
            						attributeParentFrequenciesList.add(Collections.frequency(unique_attributeParentNames, s));
            					}
            				}
            			}
            		}
            	}
            }
            else
            {
            	for(int i=0; i<unique_attributeParentNames.size(); i++)
            	{
            		if(commonAvailabilityCheckerforAttributeParents(unique_attributeParentNames.get(i), unique_attributeParentNames, unique_attributeParentNames2) == true)
            		{
            			for(String s : unique_attributeParentNames2)
            			{
            				if(s.equals(unique_attributeParentNames.get(i)))
            				{
            					if((Collections.frequency(unique_attributeParentNames, s) < Collections.frequency(unique_attributeParentNames2, s)))
            					{
            						attributeParentFrequenciesList.add(Collections.frequency(unique_attributeParentNames, s));
            					}
            					else
            					{
            						attributeParentFrequenciesList.add(Collections.frequency(unique_attributeParentNames2, s));
            					}
            				}
            			}
            		}
            	}
            }
            
            int freqSum4 = 0;
            double subRatio2 = 0;
            
            if(attributeParentNodes.size() < attributeParentNodes2.size())
            {
            	for(int i : attributeParentFrequenciesList)
            	{
            		freqSum4 += i;
            	}
            	
            	subRatio2 = (double)(100*freqSum4 / attributeParentNodes.size());
            }
            else
            {
            	for(int i : attributeParentFrequenciesList)
            	{
            		freqSum4 += i;
            	}
            	
            	subRatio2 = (double)(100*freqSum4 / attributeParentNodes2.size());
            }
            
            attributeSimilarityRatio = (subRatio1 + subRatio2) / 2;
            
            attributeSimResultLabel.setText(String.valueOf(attributeSimilarityRatio));
            
            overallSimilarityRatio = (elementSimilarityRatio*0.6) + (textSimilarityRatio*0.3) + (attributeSimilarityRatio*0.1);
            
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            
            overallSimResultLabel.setText(String.valueOf(numberFormat.format(overallSimilarityRatio)));
    	}
    }
    
    private static boolean commonAvailabilityCheckerforElements(ElementNode e, ElementNode[] dom_elementList1, ElementNode[] dom_elementList2)
    {
    	boolean isAvailableFirst = false;
    	boolean isAvailableSecond = false;
    	
    	for(int i=0; i<dom_elementList1.length; i++)
    	{
    		if(e.getElementName().equals(dom_elementList1[i].getElementName()))
    		{
    			isAvailableFirst = true;
    		}
    	}
    	
    	for(int i=0; i<dom_elementList2.length; i++)
    	{
    		if(e.getElementName().equals(dom_elementList2[i].getElementName()))
    		{
    			isAvailableSecond = true;
    		}
    	}
    	
    	if((isAvailableFirst==true) && (isAvailableSecond==true))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private static boolean commonAvailabilityCheckerforTextOwners(TextNode t, TextNode[] dom_elementList1, TextNode[] dom_elementList2)
    {
    	boolean isAvailableFirst = false;
    	boolean isAvailableSecond = false;
    	
    	for(int i=0; i<dom_elementList1.length; i++)
    	{
    		if(t.getParentElementNodeHavingText().equals(dom_elementList1[i].getParentElementNodeHavingText()))
    		{
    			isAvailableFirst = true;
    		}
    	}
    	
    	for(int i=0; i<dom_elementList2.length; i++)
    	{
    		if(t.getParentElementNodeHavingText().equals(dom_elementList2[i].getParentElementNodeHavingText()))
    		{
    			isAvailableSecond = true;
    		}
    	}
    	
    	if((isAvailableFirst==true) && (isAvailableSecond==true))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private static boolean commonAvailabilityCheckerforAttributeParents(String s, List<String> dom_elementList1, List<String> dom_elementList2)
    {
    	boolean isAvailableFirst = false;
    	boolean isAvailableSecond = false;
    	
    	for(int i=0; i<dom_elementList1.size(); i++)
    	{
    		if(s.equals(dom_elementList1.get(i)))
    		{
    			isAvailableFirst = true;
    		}
    	}
    	
    	for(int i=0; i<dom_elementList2.size(); i++)
    	{
    		if(s.equals(dom_elementList2.get(i)))
    		{
    			isAvailableSecond = true;
    		}
    	}
    	
    	if((isAvailableFirst==true) && (isAvailableSecond==true))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private static boolean commonAvailabilityCheckerforAttributes(AttributeNode a, AttributeNode[] dom_elementList1, AttributeNode[] dom_elementList2)
    {
    	boolean isAvailableFirst = false;
    	boolean isAvailableSecond = false;
    	
    	for(int i=0; i<dom_elementList1.length; i++)
    	{
    		if(a.getAttributeName().equals(dom_elementList1[i].getAttributeName()))
    		{
    			isAvailableFirst = true;
    		}
    	}
    	
    	for(int i=0; i<dom_elementList2.length; i++)
    	{
    		if(a.getAttributeName().equals(dom_elementList2[i].getAttributeName()))
    		{
    			isAvailableSecond = true;
    		}
    	}
    	
    	if((isAvailableFirst==true) && (isAvailableSecond==true))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private static ElementNode[] extractUniqueElements(List<String> element_list)
    {
    	Set<String> unique_elementNames = new HashSet<String>(element_list);
    	
    	ElementNode[] uniqueElements = new ElementNode[unique_elementNames.size()];
    	
    	for(int i=0; i<unique_elementNames.size(); i++)
        {
        	uniqueElements[i] = new ElementNode();
        }
    	
    	int j = 0;
    	
    	for(String s : unique_elementNames)
        {
        	uniqueElements[j].setElementName(s);
        	
        	j++;
        }
    	
    	for(ElementNode e : uniqueElements)
        {
        	e.setElementFrequency(Collections.frequency(element_list, e.getElementName()));
        }
    	
    	return uniqueElements;
    }
    
    private static TextNode[] extractUniqueTextOwners(List<String> textOwner_list)
    {
    	Set<String> unique_textOwnerNames = new HashSet<String>(textOwner_list);
    	
    	TextNode[] uniqueTextOwners = new TextNode[unique_textOwnerNames.size()];
    	
    	for(int i=0; i<unique_textOwnerNames.size(); i++)
        {
        	uniqueTextOwners[i] = new TextNode();
        }
    	
    	int j = 0;
    	
    	for(String s : unique_textOwnerNames)
        {
        	uniqueTextOwners[j].setParentElementNodeHavingText(s);
        	
        	j++;
        }
    	
    	for(TextNode t : uniqueTextOwners)
        {
        	t.setParentElementFrequency(Collections.frequency(textOwner_list, t.getParentElementNodeHavingText()));
        }
    	
    	return uniqueTextOwners;
    }
    
    private static AttributeNode[] extractUniqueAttributes(List<String> attribute_list)
    {
    	Set<String> unique_attributeNames = new HashSet<String>(attribute_list);
    	
    	AttributeNode[] uniqueAttributes = new AttributeNode[unique_attributeNames.size()];
    	
    	for(int i=0; i<unique_attributeNames.size(); i++)
        {
        	uniqueAttributes[i] = new AttributeNode();
        }
    	
    	int j = 0;
    	
    	for(String s : unique_attributeNames)
        {
        	uniqueAttributes[j].setAttributeName(s);
        	
        	j++;
        }
    	
    	for(AttributeNode a : uniqueAttributes)
        {
        	a.setAttributeFrequency(Collections.frequency(attribute_list, a.getAttributeName()));
        }
    	
    	return uniqueAttributes;
    }
    
    private static void preParentOrderFirstDOM(Node n)
    {
    	if(n!=null)
    	{       
            if(n.getParent()==null)
            {
            	System.out.println("ROOT -- \""+n.getName()+"\"");
            	
            	elementNodes.add(n.getName());
            }
            else
            {
            	System.out.println("\""+n.getParent().getName()+"\" -- \""+n.getName()+"\"");
            	
            	elementNodes.add(n.getName());
            }
            
            for(Object a:((Element) n).attributes())
            {
            	System.out.println("\""+n.getName()+"\" -- \""+((Attribute)a).getName()+"  "+((Attribute)a).getValue()+"\"");
            	
            	attributeParentNodes.add(n.getName());
            	attributeNodes.add(((Attribute)a).getName());
            }
                
            if(!n.getText().trim().equals(""))
            {
            	System.out.println("\""+n.getName()+"\" -- \""+n.getText().trim()+"\"");
            	
            	textNodeConnections.add(n.getName());
            }
            
            for(Object o:n.selectNodes("child::*"))
            {
            	if(n.getNodeType()==Node.ELEMENT_NODE)
            	{
            		preParentOrderFirstDOM((Node) o);
            	}
            }
        }
    }
    
    private static void preParentOrderSecondDOM(Node n)
    {
    	if(n!=null)
    	{       
            if(n.getParent()==null)
            {
            	System.out.println("ROOT -- \""+n.getName()+"\"");
            	
            	elementNodes2.add(n.getName());
            }
            else
            {
            	System.out.println("\""+n.getParent().getName()+"\" -- \""+n.getName()+"\"");
            	
            	elementNodes2.add(n.getName());
            }
            
            for(Object a:((Element) n).attributes())
            {
            	System.out.println("\""+n.getName()+"\" -- \""+((Attribute)a).getName()+"  "+((Attribute)a).getValue()+"\"");
            
            	attributeParentNodes2.add(n.getName());
            	attributeNodes2.add(((Attribute)a).getName());
            }
                
            if(!n.getText().trim().equals(""))
            {
            	System.out.println("\""+n.getName()+"\" -- \""+n.getText().trim()+"\"");
            	
            	textNodeConnections2.add(n.getName());
            }
            
            for(Object o:n.selectNodes("child::*"))
            {
            	if(n.getNodeType()==Node.ELEMENT_NODE)
            	{
            		preParentOrderSecondDOM((Node) o);
            	}
            }
        }
    }
}