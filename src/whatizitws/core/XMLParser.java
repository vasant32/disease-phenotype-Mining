
package whatizitws.core;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


/**
 * @author drashtti
 * this class will take the 
 * text mined xml output and 
 * parse it to a readable spreadsheet.
 *
 */
public class XMLParser extends DefaultHandler {
	
	private String ontId;
	private String tempVal;
	private String pmid;
	//private int diseaseCount = 0;
	private int citeCount;
	private double cite;
	private int count = 1;
	private int counter = 1;
	private int counts = 1;
	private int counting = 1;
	private Workbook wb;
	private Workbook wb1;
	private Sheet s;
	private Sheet s1;
	private Sheet s2;	
	private Sheet s3;
	private String name;
	//Map for OntId and corresponding pubmed id
	private Map <String,String> terms;

	//total no of abstracts
	private float totalDocuments;
	//local term count to calculate TF
	private float HPtermCount = 0;
	private float MPtermCount = 0;
	//map of ontID and count - to count no of times the word appeared in a document to calculate TF
	private Map <String,Float> localCount;
	//float termCountPerDoc;
	//list of Ontid and tf (temp storage per abstract)
	//List OntTf;
	//private Sheet sx;
	//private Row r1;
	//private Workbook[] wbs;
	
	
	public void setCount(String disease){
		this.name = disease;
		this.wb = new HSSFWorkbook();
		this.wb1 = new HSSFWorkbook();
		this.s = wb.createSheet();
		this.s1 = wb.createSheet();
		this.s2 = wb.createSheet();	
		this.s3 = wb1.createSheet();
		this.terms = new HashMap<String, String>();
		
	//	this.sx = wb.createSheet();
	}
	 public void setTotalDocuments( int totalDocuments){
		 this.totalDocuments = totalDocuments;
	 }
	
	public void parseDocument(String XMLFilePath, String id, int count){
		//local term count to calculate TF
		this.HPtermCount = 0;
		this.MPtermCount = 0;
		this.localCount = new HashMap<String, Float>();
		//termCountPerDoc = 1;
		
		
	    System.out.println("Parsing: " + XMLFilePath);
	    this.pmid = id;
	    this.citeCount = count;
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    boolean success = false;
	    int tries = 0;
	    Exception lastException = null;
//	    try {
	    	while (!success & tries < 5) {
	    		try {
	    			tries++;
		            SAXParser sp = spf.newSAXParser();
		            sp.parse(XMLFilePath, this);
		            success = true;
	    		}
	    		catch (Exception e) {
	    			System.err.println("Problem parsing " + XMLFilePath + " (" + e.getMessage() + ").  Retries remaining = " + (5-tries));
	    			lastException = e;
	    		}
	    	}
	    	if (!success) {
	    		lastException.printStackTrace();
	    		throw new RuntimeException("Failed to parse " + XMLFilePath + " after 5 retries", lastException);
	    	}
	    	
		//System.out.println("you are here");
//	    }catch(SAXException se) {
//		se.printStackTrace();
//	    }catch(ParserConfigurationException pce) {
//		pce.printStackTrace();
//	    }catch (IOException ie) {
//		ie.printStackTrace();
//	    }
//	     catch (Exception el){
//	    	 el.printStackTrace();
//	     }
	}

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	//reset
	tempVal = "";
	if (qName.equalsIgnoreCase("z:e")){
		ontId = attributes.getValue("ids");
	}
	if (qName.equalsIgnoreCase("z:HPO")){
		ontId = attributes.getValue("ids");
	}
	if (qName.equalsIgnoreCase("z:MP")){
		ontId = attributes.getValue("ids");
	}
	if (qName.equalsIgnoreCase("z:MPATH")){
		ontId = attributes.getValue("ids");
	}
	if (qName.equalsIgnoreCase("z:chebi")){
		ontId = attributes.getValue("ids");
	}
	
	
    }
    
    
    public void characters(char[] ch, int start, int length) throws SAXException {
	tempVal = new String(ch,start,length);
	//System.out.println(ch);
    }
    
    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     *
     */
    
    public void endElement(String uri, String localName,
			   String qName) throws SAXException {

		 
	if(qName.equalsIgnoreCase("z:e")) {
		//System.out.println(tempVal);
		if(terms.containsKey(ontId) && (!pmid.equalsIgnoreCase(terms.get(ontId)))){
			//diseaseCount = diseaseCount++;
			terms.put(ontId, pmid);
			if (! tempVal.equalsIgnoreCase(name)){
			addCandidateData(tempVal,"Disease");}
		}
		else {
		terms.put(ontId, pmid);
				}
	   }
	if(qName.equalsIgnoreCase("z:HPO")) {
		
		if ((terms.containsKey(ontId) && (!pmid.equalsIgnoreCase(terms.get(ontId)))) || !terms.containsKey(ontId)){
			terms.put(ontId, pmid);
			addData(tempVal, "HPO");
			this.HPtermCount = HPtermCount + 1;
			//System.out.println("Hp term count is : " + HPtermCount);
			localCount.put(ontId, 1f);
		}
		else{
		terms.put(ontId, pmid);
		this.HPtermCount = HPtermCount + 1;
		//System.out.println("Hp term count is : " + HPtermCount);
		float temp = localCount.get(ontId)+1 ;
		localCount.put(ontId, temp);
		}
	}
	if(qName.equalsIgnoreCase("z:MP")){
		if ((terms.containsKey(ontId) && (!pmid.equalsIgnoreCase(terms.get(ontId)))) || !terms.containsKey(ontId) ){
			terms.put(ontId, pmid);
			addData(tempVal, "MP");
			this.MPtermCount = MPtermCount + 1;
			localCount.put(ontId, 1f);
		}
		else{
			terms.put(ontId, pmid);
			this.MPtermCount = MPtermCount + 1;
			float temp = localCount.get(ontId)+1 ;
			localCount.put(ontId, temp);
		}
	}
	if(qName.equalsIgnoreCase("z:MPATH")) {
		if (terms.containsKey(ontId) && (!pmid.equalsIgnoreCase(terms.get(ontId)))){
			terms.put(ontId, pmid);
			addCandidateData(tempVal, "MPATH");
		}
		else{
			terms.put(ontId, pmid);
		}
		
	}
	if(qName.equalsIgnoreCase("z:Chebi")) { 
		if (terms.containsKey(ontId) && (!pmid.equalsIgnoreCase(terms.get(ontId)))){
			terms.put(ontId, pmid);
			addChebiData(tempVal, "ChEBI");
		}
		else{
			terms.put(ontId, pmid);
		}
					
	}

		}
    
    
    
    public void CalculateTf(){
    	System.out.println("Calculating Tf");
    	for (String key : localCount.keySet() ) {
    		//System.out.println("In the for loop.");
    		String tempOID = key;
    		float numOfOccurrences = localCount.get(key);
    		if ( tempOID.startsWith("HP")){
    			//System.out.println("Found an HP term. HP term count is " + HPtermCount);
    		float totalTermsInDocument = HPtermCount;
    		//System.out.println("totalTermsInDocument is " + totalTermsInDocument + "And the number of occurences is " + numOfOccurrences );
    		float tf = numOfOccurrences / totalTermsInDocument;
    		//System.out.println("Tf score for " + tempOID + " and pubmed id " + pmid + "is" + tf);
    		TermFrequencyData tdf = new TermFrequencyData(pmid,tempOID,tf);
//    		addTfData(pmid,tempOID,tf);
    		addTfData(tdf);
    		
    		}
    		else {
    			//System.out.println("Found an MP term. MP term count is " + MPtermCount);
    			
    			float	totalTermsInDocument = MPtermCount;
    			//System.out.println("totalTermsInDocument is " + totalTermsInDocument + "And the number of occurences is " + numOfOccurrences );
    			float tf = numOfOccurrences / totalTermsInDocument;
    			TermFrequencyData tdf = new TermFrequencyData(pmid,tempOID,tf);
//        		addTfData(pmid,tempOID,tf);
        		addTfData(tdf);
        		
    		}
    	}
 }
    

    
//    public void addTfData(String pubmed, String oid , int score){
//	   	String[][] arr = new String [][];
//	   	
//    }
    
    private Set<TermFrequencyData> tdfs = new HashSet<TermFrequencyData>();
    
    public void addTfData(TermFrequencyData tdf) {
    	tdfs.add(tdf);
    }
    
    
 public void CreateXL(){
    	
    	   //Defining the headers for the spreadsheet.
	 	   Row rs3 = this.s3.createRow(0);
    	   Row r0 = this.s.createRow(0);
    	   Row sheet1 = this.s1.createRow(0);
    	   Row sheet2 = this.s2.createRow(0);  
    	   //sheet 0
    	   Cell h1 = r0.createCell(0);
    	   h1.setCellValue("Subject");
    	   Cell h2 = r0.createCell(1);
    	   h2.setCellValue("Predicate");
    	   Cell h3 = r0.createCell(2);
    	   h3.setCellValue("object");
    	   Cell h4 = r0.createCell(3);
    	   h4.setCellValue("Ontology");
    	   Cell h5 = r0.createCell(4);
    	   h5.setCellValue("Ontology-ID");
    	   Cell h6 = r0.createCell(5);
    	   h6.setCellValue("PMID");
    	   Cell h7 = r0.createCell(6);
    	   h7.setCellValue("Citation Count");
    	   Cell h8 = r0.createCell(7);
    	   h8.setCellValue("Term frequency");
    	  
    	   
    	   //sheet1 
    	   Cell sh1 = sheet1.createCell(0);
    	   sh1.setCellValue("Subject");
    	   Cell sh2 = sheet1.createCell(1);
    	   sh2.setCellValue("Predicate");
    	   Cell sh3 = sheet1.createCell(2);
    	   sh3.setCellValue("object");
    	   Cell sh4 = sheet1.createCell(3);
    	   sh4.setCellValue("Ontology");
    	   Cell sh5 = sheet1.createCell(4);
    	   sh5.setCellValue("Ontology-ID");
    	   Cell sh6 = sheet1.createCell(5);
    	   sh6.setCellValue("PMID");
    	   Cell sh7 = sheet1.createCell(6);
    	   sh7.setCellValue("Citation Count");
    	   //Row r1 = this.s.createRow(1);
    	   //System.out.println("you are here");
    	   
    	   //sheet2
    	   Cell ssh1 = sheet2.createCell(0);
    	   ssh1.setCellValue("Subject");
    	   Cell ssh2 = sheet2.createCell(1);
    	   ssh2.setCellValue("Predicate");
    	   Cell ssh3 = sheet2.createCell(2);
    	   ssh3.setCellValue("object");
    	   Cell ssh4 = sheet2.createCell(3);
    	   ssh4.setCellValue("Ontology");
    	   Cell ssh5 = sheet2.createCell(4);
    	   ssh5.setCellValue("Ontology-ID");
    	   Cell ssh6 = sheet2.createCell(5);
    	   ssh6.setCellValue("PMID");
    	   Cell ssh7 = sheet2.createCell(6);
    	   ssh7.setCellValue("Citation Count");
    	   
    	   //sheet 1 of second workbook.
    	   Cell sssh1 = rs3.createCell(0);
    	   sssh1.setCellValue("Disease");
    	   Cell sssh2 = rs3.createCell(1);
    	   sssh2.setCellValue("Ontology ID");
    	   Cell sssh3 = rs3.createCell(2);
    	   sssh3.setCellValue("Phenotype");
    	   Cell sssh4 = rs3.createCell(3);
    	   sssh4.setCellValue("Abstract count");
    	   Cell sssh5 = rs3.createCell(4);
    	  sssh5.setCellValue("Total citation count");
    	   Cell sssh6 = rs3.createCell(5);
    	  sssh6.setCellValue("Mean tf");
    	  	Cell sssh7 = rs3.createCell(6);
    	  sssh7.setCellValue("tf-idf");

    	}
    	
 
    	 
   public void addData(String Value, String ont){
    	   // Define a few rows
	      	   for(int rownum = count; rownum <= count; rownum++) {
	      		   Row r1 = this.s.createRow(rownum); 
	      		// System.out.println("in add data sheet s = " + s);   
    		   for(int cellnum = 0; cellnum < 1; cellnum ++) {
    			   Cell c1 = r1.createCell(cellnum);
    			   Cell c2 = r1.createCell(cellnum+1);
    			   Cell c3 = r1.createCell(cellnum+2);
    			   Cell c4 = r1.createCell(cellnum+3);
    			   Cell c5 = r1.createCell(cellnum+4);
    			   Cell c6 = r1.createCell(cellnum+5);
    			   Cell c7 = r1.createCell(cellnum+6);
    			  
    	   
    			   c1.setCellValue(name);
    			   c2.setCellValue("is_associated_with");
    			   c3.setCellValue(Value);
    			   c4.setCellValue(ont);
    			   c5.setCellValue(ontId);
    			   c6.setCellValue(this.pmid);
    			   c7.setCellValue(this.citeCount); 
    			   
    			
    		   		}   		   
    	   		}
    	  this.count = count+1;    
    	 
    	 //	wb.write(this.out);
    	 	  	 	//this.out.close();
   	}
   
   
   
   public void addCandidateData(String Value, String ont){
	// Define a few rows
  	   for(int rownum = counter; rownum <= counter; rownum++) {
  		   Row r1 = this.s1.createRow(rownum); 
  		// System.out.println("in add data sheet s = " + s);   
	   for(int cellnum = 0; cellnum < 1; cellnum ++) {
		   Cell c1 = r1.createCell(cellnum);
		   Cell c2 = r1.createCell(cellnum+1);
		   Cell c3 = r1.createCell(cellnum+2);
		   Cell c4 = r1.createCell(cellnum+3);
		   Cell c5 = r1.createCell(cellnum+4);
		   Cell c6 = r1.createCell(cellnum+5);
		   Cell c7 = r1.createCell(cellnum+6);
   
		   c1.setCellValue(name);
		   c2.setCellValue("is_associated_with");
		   c3.setCellValue(Value);
		   c4.setCellValue(ont);
		   c5.setCellValue(ontId);
		   c6.setCellValue(this.pmid);
		   c7.setCellValue(this.citeCount); 
		
	   		}   		   
   		}
  this.counter = counter+1; 
	   
	   
   }
   
   
   
   public void addChebiData(String Value, String ont){
	// Define a few rows
  	   for(int rownum = counting; rownum <= counting; rownum++) {
  		   Row r1 = this.s2.createRow(rownum); 
  		// System.out.println("in add data sheet s = " + s);   
	   for(int cellnum = 0; cellnum < 1; cellnum ++) {
		   Cell c1 = r1.createCell(cellnum);
		   Cell c2 = r1.createCell(cellnum+1);
		   Cell c3 = r1.createCell(cellnum+2);
		   Cell c4 = r1.createCell(cellnum+3);
		   Cell c5 = r1.createCell(cellnum+4);
		   Cell c6 = r1.createCell(cellnum+5);
		   Cell c7 = r1.createCell(cellnum+6);
   
		   c1.setCellValue(name);
		   c2.setCellValue("is_associated_with");
		   c3.setCellValue(Value);
		   c4.setCellValue(ont);
		   c5.setCellValue(ontId);
		   c6.setCellValue(this.pmid);
		   c7.setCellValue(this.citeCount); 
		
	   		}   		   
   		}
  this.counting = counting+1; 
	   
   }
   
   
    		
   public void saveToFile(String fname) throws IOException, InvalidFormatException{
	 String filename = fname +".xls"; // change the name as required
	 FileOutputStream out = new FileOutputStream(filename);
	 wb.write(out);
	 out.close();
	 System.out.println(filename + "File created");
	 addTfdata(filename);
	 postProcess(filename);
	 
   }
   
   public void addTfdata(String file) throws InvalidFormatException, FileNotFoundException, IOException{
	   System.out.println("Starting to add term frequency data. Size of Tdf set:" + tdfs.size());
	   Workbook work = WorkbookFactory.create(new FileInputStream(file));
	   Sheet ws = work.getSheetAt(0);
	   int rownum = ws.getLastRowNum();
	   for(int num = 1; num <= rownum; num++){
		   Row rr = ws.getRow(num);
		   Cell pmid = rr.getCell(5);
		   String pid = pmid.toString();
		   Cell ontid = rr.getCell(4);
		   String oid = ontid.toString();
		   for(TermFrequencyData temp :tdfs){
			   String tempPid = temp.getPubmedId();
			   String tempOid = temp.getOntoID();
			   float temptf = temp.getTermFreq();
			   if (pid.equalsIgnoreCase(tempPid) && oid.equalsIgnoreCase(tempOid)){
				   Cell x = rr.createCell(7);
				   x.setCellValue(temptf);
			   }
		   }
	   }
	   FileOutputStream fout = new FileOutputStream(file);
	   work.write(fout);
	   fout.close();
   }
   
   
   
   public void postProcess(String filen) throws InvalidFormatException, FileNotFoundException, IOException{
	   System.out.println("Start of post processing...");
	   //map for onto id and citation count
	   Map<String,Double> process = new HashMap<String, Double>();
	   //map for onto id and abstract count
	   Map<String,Integer> totalCount = new HashMap<String,Integer>();
	   //map of onto id and its corresponding phenotype name
	   Map<String,String> names = new HashMap<String, String>();
	   //map of onto id and adding up its tf
	   Map<String,Float> frequency = new HashMap<String, Float>();
	   Workbook work = WorkbookFactory.create(new FileInputStream(filen));
	   Sheet ws = work.getSheetAt(0);
	   int rownum = ws.getLastRowNum();
	   System.out.println("the number of rows in the sheet are :" + rownum);
	   Row r = ws.getRow(0);
	   int columnNum = r.getLastCellNum();
	   System.out.println(" the number of columns in the first row are :" + columnNum);
	   
	   for ( int num =1; num<= rownum; num++){
		   Row rr = ws.getRow(num);
		   Cell phenos = rr.getCell(2);
		   String pheno = phenos.toString();
		   Cell ids = rr.getCell(4);
		   String id = ids.toString();
		   Cell cites = rr.getCell(6);
		   String cited = cites.toString();
		   cite = Double.parseDouble(cited);	
		   Cell tf = rr.getCell(7);
		   String tfs = tf.toString();
		   float tfcount = Float.parseFloat(tfs);
		 // System.out.println("the citation count is" + cite );
		   if (!process.containsKey(id)){
		   process.put(id,  cite);
		   names.put(id, pheno);
		   totalCount.put(id, 1);
		   frequency.put(id, tfcount);
		 
		   }
		   else {
			   double citCount =  (process.get(id) + cite);
			   int abstractcount = totalCount.get(id) + 1;
			   process.put(id, citCount);
			   totalCount.put(id, abstractcount);
			   float tfcounter = frequency.get(id) + tfcount;
			   frequency.put(id, tfcounter);
			  }
		   
	   }//end of for loop
	   
	   for (String x : process.keySet()){
		  String pheno = names.get(x);
		  int abCount = totalCount.get(x);
		   double ciCount = process.get(x);
		   float meantf = frequency.get(x) / abCount;
		   float idf = (float) Math.log10(totalDocuments/abCount);
		   float tfidf = meantf * idf; 
		   addPostProcessData(x,pheno,abCount,ciCount,meantf,tfidf);
		   
	   }
	  createPostProcessFile(); 
	 }

   
   
   public void addPostProcessData(String id, String pheno, int abCount, double ciCount, float meantf, float tfidf){
	// Define a few rows
  	   for(int rownum = counts; rownum <= counts; rownum++) {
  		   Row r1 = this.s3.createRow(rownum); 
  		// System.out.println("in add data sheet s = " + s);   
	   for(int cellnum = 0; cellnum < 1; cellnum ++) {
		   Cell c1 = r1.createCell(cellnum);
		   Cell c2 = r1.createCell(cellnum+1);
		   Cell c3 = r1.createCell(cellnum+2);
		   Cell c4 = r1.createCell(cellnum+3);
		   Cell c5 = r1.createCell(cellnum+4);
		   Cell c6 = r1.createCell(cellnum+5);
		   Cell c7 = r1.createCell(cellnum+6);
		  
		   c1.setCellValue(name);
		   c2.setCellValue(id);
		   c3.setCellValue(pheno);
		   c4.setCellValue(abCount);
		   c5.setCellValue(ciCount);
		   c6.setCellValue(meantf);
		   c7.setCellValue(tfidf);
		   
		   
		
	   		}   		   
   		}
  this.counts = counts+1; 
	   
	   
   }
   
   
   public void createPostProcessFile() throws IOException{
	   String nameprocessed = name.trim().replace(" ", "_");
	   FileOutputStream fo = new FileOutputStream("processed"+ nameprocessed + ".xls");
	   wb1.write(fo);
	   fo.close();
	   System.out.println("processed" + nameprocessed + ".xls file created");
	   
   }
}
 

