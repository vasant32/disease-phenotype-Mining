/* **
 * This code has been designed/implemented and is maintained by:
 * 
 * Miguel Arregui (miguel.arregui@gmail.com)
 * 
 * Any comments and/or feedback are welcome and encouraged. 
 * 
 * Started on:     9 May 2006.
 * Last reviewed:  7 June 2006.
 */


package whatizitws.core;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.sun.xml.ws.developer.JAXWSProperties;

import whatizitws.client.Whatizit;
import whatizitws.client.WhatizitException_Exception;
import whatizitws.client.Whatizit_Service;


public class SoapClient {
	
	private Whatizit whatizit;
	private XMLParser par;
	
	
  //public static void main (String [] args) throws Exception {
  	
	public SoapClient(){
  	// Get the WHATIZIT service end point (always like this) 
    Whatizit_Service service = new Whatizit_Service();    
     whatizit = service.getPipeline();              
     par = new XMLParser();
    // Mtom and session maintain flag
    BindingProvider bindingProvider = (BindingProvider)whatizit;   
    bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "europepmc");
    bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "europepmc");
    ((SOAPBinding)bindingProvider.getBinding ()).setMTOMEnabled(true);
    Map <String,Object> requestContext = bindingProvider.getRequestContext();
    requestContext.put(JAXWSProperties.MTOM_THRESHOLOD_VALUE, new Integer(0));
	}
    
    // Check what pipelines are available (their *name* and what they do)
    /*List <SelectItem> pipesStatus = whatizit.getPipelinesStatus();
    Iterator<SelectItem> it = pipesStatus.iterator();
    while (it.hasNext()){
    	SelectItem i = it.next();    
    	System.out.println("PipelineName: " + i.getLabel());
    	System.out.println("Description: " + i.getDescription());    	
    	System.out.println("Available: " + !i.isDisabled());
    	System.out.println("");
    }*/
        
    public void TextMine( String tex, String id, int count,String filename) throws WhatizitException_Exception, IOException {
    // Those pipelines which are not disabled can be accessed by *name*
    String pipelineName = "whatizitHPO_MP_MPATH_Chemicals_Disease";
    String text = tex;
    boolean convertToHtml = false; // vs. leave it as plain XML
   // System.out.println(" contacting whatizitpipeline with text" + text);
    String result = whatizit.contact(pipelineName,text, convertToHtml);
    //System.out.println(result);
   // System.out.println(" contacted whatizitpipeline");
    File folder = new File(filename+"Mined");
    if(folder.mkdir()){
    	System.out.println(filename+"Mined folder created!");
    }
    String file = folder.getAbsolutePath() +"/"+id+"_"+count+".xml";
    PrintWriter out = new PrintWriter(new FileWriter(file)); 
    out.print(result); 
   // out.println("world"); 
    out.close();
    par.parseDocument(file,id,count);
    par.CalculateTf();
        
    // Query by pmid
   /* String pmid = "121212"; // The number is a Pubmed accession key
    String xml = whatizit.queryPmid(pipelineName, pmid);
    System.out.println(xml);*/
    
    
    // Query using the whole syntax power of lucene
   /*String pipelineName = "whatizitDisease";
    Search params = new Search();
    params.setPipelineName(pipelineName);
    params.setQuery("Marfans Syndrome");
    params.setLimit(5);
    try {
    	// using text/xml mime type
    	StreamSource resp = (StreamSource)whatizit.search(params);
    	InputStream is = resp.getInputStream();    	
    	byte [] bytes = new byte [1024];
    	int size = 0;
    	while ((size=is.read(bytes, 0, bytes.length)) != -1){ 
    		System.out.write(bytes, 0, size); 
    	}
    	is.close();
    }
    catch (WhatizitException_Exception e){
      System.out.println(e.getFaultInfo().getMessage());
    }*/
  }
    
    public void setVariables(String name){
    	par.setCount(name);
    	 par.CreateXL();
    	
    	
    }
    
    public void gettotalNoOfAbstracts(int totalNoOfAbstracts){
    	  par.setTotalDocuments(totalNoOfAbstracts);
    	
    }
    
    
    public void savefile(String name) throws IOException, InvalidFormatException{
    	par.saveToFile(name);
    }
}

// Eof
