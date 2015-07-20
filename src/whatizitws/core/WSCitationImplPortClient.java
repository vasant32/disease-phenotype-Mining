package whatizitws.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.*;

import java.util.List;

import javax.activation.DataHandler;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import ebi.ws.client.*;

public class WSCitationImplPortClient {

	private SoapClient soapclient;

	public WSCitationImplPortClient(String name) {
		// System.out.println("you have reached here");
		soapclient = new SoapClient();
		soapclient.setVariables(name);
	}

	public static void main(String[] args) throws InterruptedException,
			QueryException_Exception, IOException, InvalidFormatException {
		if (args.length < 2){
			System.out.println("Please provide the name of the disease you want to mine and the corpus set:\n" +
					"-all : for entire literature \n" +
					"-metabolism : for metabolic specific journals\n" +
					"-immune : for immune specific journals\n" +
					"-skeletal: for skeletal specific journals \n");
			System.exit(0);
			}
		String name = args[0];
		String flag = args[1];
		//System.out.println("flag is " + flag + "\n");
		String query = null;
		if(flag.contentEquals("-all")){
		 query = "src:med \""+name+"\" NOT PUB_TYPE:review HAS_ABSTRACT:y";}
		else if (flag.contentEquals("-metabolism")){
			query = "(ISSN:\"0353-8109\" OR ISSN:\"1179-318X\" OR ISSN:\"2277-9175\" OR ISSN:\"0002-9165\" " +
				"OR ISSN:\"0193-1849\" OR ISSN:\"2141-9248\" OR ISSN:\"0250-6807\" OR ISSN:\"1836-1935\" OR ISSN:\"1741-7015\" " +
				"OR ISSN:\"2049-3002\" OR ISSN:\"0012-1797\" OR ISSN:\"0149-5992\" OR ISSN:\"1178-7007\" OR ISSN:\"1758-5996\" " +
				"OR ISSN:\"0973-3930\" OR ISSN:\"2090-9446\" OR ISSN:\"2251-6581\" OR ISSN:\"2090-0708\" " +
				"OR ISSN:\"2090-0724\" OR ISSN:\"1540-4196\" OR ISSN:\"1743-7075\" OR ISSN:\"1549-1277\" OR ISSN:\"1948-9358\") " +
				"AND "+ name +"  AND PUB_TYPE:\"Journal Article\" AND has_abstract:y";}
		else if (flag.contentEquals("-immune")){
		query = "(ISSN:\"0353-8109\" OR ISSN:\"1179-318X\" OR ISSN:\"2277-9175\" OR ISSN:\"1710-1484\" " +
				"OR ISSN:\"2092-7355\" OR ISSN:\"2164-7712\" OR ISSN:\"1945-8924\" OR ISSN:\"2141-9248\" OR ISSN:\"2090-0422\" " +
				"OR ISSN:\"1836-1935\" OR ISSN:\"1471-2172\" OR ISSN:\"1664-3224\" OR ISSN:\"1598-2629\" OR ISSN:\"1018-2438\" " +
				"OR ISSN:\"0953-8178\" OR ISSN:\"1740-2557\" OR ISSN:\"2314-8861\" OR ISSN:\"1549-1277\") " +
				"AND "+ name +"  AND PUB_TYPE:\"Journal Article\" AND has_abstract:y";}
		else if (flag.contentEquals("-skeletal")){
			query = "(ISSN:\"0353-8109\" OR ISSN:\"1745-3674\" OR ISSN:\"1179-318X\" OR ISSN:\"2277-9175\" " +
				"OR ISSN:\"2090-3464\" OR ISSN:\"2141-9248\" OR ISSN:\"1836-1935\" OR ISSN:\"1471-2474\" OR ISSN:\"2046-3758\" " +
				"OR ISSN:\"0976-5662\" OR ISSN:\"0972-978X\" OR ISSN:\"2240-4554\" OR ISSN:\"1549-1277\" OR ISSN:\"1759-720X\" " +
				"OR ISSN:\"0019-5413\" OR ISSN:\"2090-6161\" OR ISSN:\"2287-6375\" OR ISSN:\"1863-2521\" OR ISSN:\"2218-5836\") " +
				"AND "+ name +"  AND PUB_TYPE:\"Journal Article\" AND has_abstract:y";}
		else{
			System.out.println("please check the arguments you have passed.");
			System.exit(0);
			}
		
		
		
		WSCitationImplService wSCitationImplService = new WSCitationImplService();
		WSCitationImpl wSCitationImpl = wSCitationImplService
				.getWSCitationImplPort();
		// Add your code to call the desired methods.
		// new WSCitationImplPortClient().saveIdList(wSCitationImpl,
		// "src:med marfan's syndrom HAS_ABSTRACT:y");
		// new WSCitationImplPortClient().fetchFullText(wSCitationImpl,
		// "test.csv");
		/**query = 
				"(ISSN:\"0193-1857\" OR ISSN:\"1108-7471\" OR ISSN:\"1471-230X\" OR ISSN:\"1662-0631\" " +
				"OR ISSN:\"11178-7023\" OR ISSN:\"1179-5522\" OR ISSN:\"1554-7914\" OR ISSN:\"1687-6121\" OR ISSN:\"2052-0034\" " +
				"OR ISSN:\"0017-5749\" OR ISSN:\"2090-8040\" OR ISSN:\"02090-4398\" OR ISSN:\"2090-8695\" OR ISSN:\"1476-9255\" " +
				"OR ISSN:\"1178-7031\" OR ISSN:\"2154-1280\" OR ISSN:\"1756-283X\" OR ISSN:\"1007-9327\" " +
				"OR ISSN:\"1948-5190\" OR ISSN:\"2150-5330\" OR ISSN:\"1948-9366\") " +
				"AND (\"oral crohn's\")  AND PUB_TYPE:\"Journal Article\" AND has_abstract:y";*/
		// String query =
		// "(ISSN:\"1939-327x\" OR ISSN:\"1935-5548\" OR ISSN:\"1463-1326\" OR ISSN:\"1432-0428\" OR ISSN:\"1872-8227\" OR ISSN:\"1178-7007\" OR ISSN:\"1758-5996\") AND (\"type 2 diabetes\") AND has_abstract:y";
		System.out.println("your query is - " + query);
		new WSCitationImplPortClient(name).getAbstractsFT(wSCitationImpl,
				query, name);
		// new WSCitationImplPortClient().runAllMethods(wSCitationImpl);

	}

	@SuppressWarnings("unused")
	private void saveIdList(WSCitationImpl wSCitationImpl, String idQuery)
			throws QueryException_Exception, FileNotFoundException {
		String query = idQuery;
		String dataSet = "metadata";
		String resultType = "idlist";
		Integer offSet = 0;
		Boolean synonym = false;
		String email = "";
		File pmid;

		ResponseWrapper resultsBean = wSCitationImpl.searchPublications(query,
				dataSet, resultType, offSet, synonym, email);
		List<Result> beanCollection = resultsBean.getResultList().getResult();

		pmid = new File("data.txt");

		try {
			FileWriter outstream = new FileWriter(pmid);
			BufferedWriter out = new BufferedWriter(outstream);

			for (Result bean : beanCollection) {
				out.write(bean.getPmid() + "\n");

				System.out.println("pmid:" + bean.getPmid());
			}
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	@SuppressWarnings("unused")
	private void fetchFullText(WSCitationImpl wSCitationImpl, String pmid_file)
			throws QueryException_Exception, FileNotFoundException {

		Scanner sc = new Scanner(new File(pmid_file));

		/*
		 * List<Integer> pmid; pmid = new ArrayList<Integer>();
		 * 
		 * while (sc.hasNext()) { if (sc.hasNextInt()) { pmid.add(sc.nextInt());
		 * System.out.println(pmid.get(pmid.size()-1)); Result fulltextXML =
		 * wSCitationImpl
		 * .getFulltextXML(Integer.toString(pmid.get(pmid.size()-1)), "pmc",
		 * "");
		 * 
		 * if (fulltextXML.getFullText() != null) { saveFullText(fulltextXML,
		 * pmid.get(pmid.size()-1)); } } }
		 * 
		 * System.out.println("number of pmids: " + pmid.size());
		 */
		String[] ids;
		String line;

		while (sc.hasNext()) {

			line = sc.nextLine();
			ids = new String[3];
			ids = line.split(",");
			System.out.println("\nRetrieving Data:\nPMID:" + ids[0] + " PMCID:"
					+ ids[1]);

			// check open access availability and get ft if open access is given
			if (ids[0].matches("\\d+")) {
				// retrieve abstract

				String query = "ext_id:" + ids[0] + " src:med";
				String dataSet = "metadata";
				String resultType = "core";
				Integer offSet = 0;
				Boolean synonym = false;
				String email = "";
				File dir, abs, supp;

				ResponseWrapper resultsBean = wSCitationImpl
						.searchPublications(query, dataSet, resultType, offSet,
								synonym, email);

				if (resultsBean.getResultList().getResult().isEmpty() == false) {
					dir = new File("data");
					abs = new File(dir.getAbsolutePath(), ids[0] + ".txt");
					supp = new File("data/supp");
					if (supp.mkdir()) {
						System.out.println("dir created!");
					}
					if (dir.mkdir()) {
						System.out.println("dir created!");
					}

					// System.out.println("Is open access:" +
					// resultsBean.getResultList().getResult().get(0).getIsOpenAccess());
					// //TODO: find a fix/workaround for this (producing null
					// values only)
					// System.out.println(resultsBean.getResultList().getResult().get(0).getAbstractText());

					try {
						FileWriter outstream = new FileWriter(abs);
						BufferedWriter out = new BufferedWriter(outstream);
						out.write(resultsBean.getResultList().getResult()
								.get(0).getAbstractText());

						out.close();

						System.out.println("Abstract retrieved successfully!");
					} catch (Exception e) {
						System.err.println("Error: " + e.getMessage());
					}

				} else {
					System.out
							.println("Nothing found for pmid: "
									+ ids[0]
									+ "\nMaybe the citation given in ArrayExpress is incorrect.");
					// TODO: generate a counting var + some logging for such
					// incidents
				}
			}

			if (ids[1].matches("PMC\\d+")) {
				try {

					Result fulltextXML = wSCitationImpl.getFulltextXML(ids[0],
							"pmc", "");

					if (fulltextXML.getFullText() != null) {
						saveFullText(fulltextXML, Integer.parseInt(ids[0]));
					}
				} catch (QueryException_Exception e) { // here comes the
														// workaround
					System.out
							.println("PMCID present but the publication is not open access. Full Text not retrieved.");
				}

				try {
					Result suppFiles = wSCitationImpl.getSupplementaryFiles(
							ids[0], "pmc", "");
					if (suppFiles.getSupplementaryFiles() != null) {
						saveSupplementaryFiles(suppFiles,
								Integer.parseInt(ids[0]));
					}
				} catch (QueryException_Exception e) { // here comes the
														// workaround
					System.out.println("Supplemental Data not retrievable.");

				}

			}

		}
	}

	private void getAbstractsFT(WSCitationImpl wSCitationImpl,
			String searchQuery, String name) throws QueryException_Exception,
			IOException, InvalidFormatException {

		// TODO: load general metadata first and then if full text is available
		// load it

		// Scanner sc = new Scanner(new File(pmid_file));

		// List<Integer> pmid;
		// pmid = new ArrayList<Integer>();
		/*
		 * while (sc.hasNext()) { if (sc.hasNextInt()) { pmid.add(sc.nextInt());
		 * System.out.println(pmid.get(pmid.size()-1)); Result fulltextXML =
		 * wSCitationImpl
		 * .getFulltextXML(Integer.toString(pmid.get(pmid.size()-1)), "pmc",
		 * "");
		 * 
		 * if (fulltextXML.getFullText() != null) { saveFullText(fulltextXML,
		 * pmid.get(pmid.size()-1)); } } }
		 * 
		 * System.out.println("number of pmids: " + pmid.size());
		 */
		String query = searchQuery;
		String dataSet = "metadata";
		String resultType = "core";
		Integer offSet = 0;
		Boolean synonym = true; // was false
		String email = "";
		String filename = name.trim().replace(" ", "_");
		File dir, abs;

		dir = new File(filename + "Abstracts");
		if (dir.mkdir()) {
			System.out.println(filename + "dir created!");
		}
		System.out.println("fetching abstracts");
		ResponseWrapper resultsBean = wSCitationImpl.searchPublications(query,
				dataSet, resultType, offSet, synonym, email);
		List<Result> beanCollection = resultsBean.getResultList().getResult();

		@SuppressWarnings("unused")
		Result fulltextXML;

		int totalNoOfAbstracts = beanCollection.size();
		while (resultsBean.getResultList().getResult().isEmpty() == false) {
			// System.out.println("abstracts found!");
			for (Result bean : beanCollection) {
				if (bean.getPmid() != null){
				abs = new File(dir.getAbsolutePath(), bean.getPmid() + "_"
						+ bean.getCitedByCount() + ".txt");

				try {
					FileWriter outstream = new FileWriter(abs);
					BufferedWriter out = new BufferedWriter(outstream);
					// out.write(bean.getAbstractText());
					String text = bean.getAbstractText();
					out.write(text);
					out.close();
					// System.out.println("Abstract: " + text);
					String pid = bean.getPmid();
					int cite = bean.getCitedByCount();
					// SoapClient soapclient = new SoapClient();
					// System.out.println("sent to whatizit!");
					soapclient.TextMine(text, pid, cite, filename);
					// out.close();
				} catch (Exception e) {
					// System.err.println("Error: " + e.getMessage());
					throw new RuntimeException(e);
				}

				// try getting the fulltext if it is open access
				/**
				 * try {
				 * 
				 * fulltextXML = wSCitationImpl.getFulltextXML(bean.getPmid(),
				 * "pmc", "");
				 * 
				 * if (fulltextXML.getFullText() != null) {
				 * saveFullText(fulltextXML, Integer.parseInt(bean.getPmid()));
				 * } } catch (QueryException_Exception e) { // catching the the
				 * exception if non open access full text is attempted to be
				 * retrieved System.out.println(
				 * "PMCID present but the publication is not open access. Full Text not retrieved."
				 * ); }
				 */
				}
			}
			offSet++; // overcoming the 25 entries batch problem
			resultsBean = wSCitationImpl.searchPublications(query, dataSet,
					resultType, offSet, synonym, email);
			beanCollection = resultsBean.getResultList().getResult();
			totalNoOfAbstracts = totalNoOfAbstracts + beanCollection.size();
		}
		// System.out.println("total number of documents = " +
		// totalNoOfAbstracts);
		soapclient.gettotalNoOfAbstracts(totalNoOfAbstracts);
		soapclient.savefile(filename);

	}

	/*
	 * while (sc.hasNext()) {
	 * 
	 * line = sc.nextLine(); ids = new String[3]; ids = line.split(",");
	 * System.out.println("\nRetrieving Data:\nPMID:" + ids[0] + " PMCID:" +
	 * ids[1]);
	 * 
	 * // check open access availability and get ft if open access is given if
	 * (ids[0].matches("\\d+")){ // retrieve abstract
	 * 
	 * 
	 * 
	 * // System.out.println("Is open access:" +
	 * resultsBean.getResultList().getResult().get(0).getIsOpenAccess());
	 * //TODO: find a fix/workaround for this (producing null values only) //
	 * System
	 * .out.println(resultsBean.getResultList().getResult().get(0).getAbstractText
	 * ());
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } else { System.out.println("Nothing found for pmid: " + ids[0] +
	 * "\nMaybe the citation given in ArrayExpress is incorrect."); //TODO:
	 * generate a counting var + some logging for such incidents } }
	 * 
	 * if (ids[1].matches("PMC\\d+")) { try {
	 * 
	 * Result fulltextXML = wSCitationImpl.getFulltextXML(ids[0], "pmc", "");
	 * 
	 * if (fulltextXML.getFullText() != null) { saveFullText(fulltextXML,
	 * Integer.parseInt(ids[0])); } } catch (QueryException_Exception e) { //
	 * here comes the workaround System.out.println(
	 * "PMCID present but the publication is not open access. Full Text not retrieved."
	 * ); }
	 * 
	 * try { Result suppFiles = wSCitationImpl.getSupplementaryFiles(ids[0],
	 * "pmc", ""); if (suppFiles.getSupplementaryFiles() != null){
	 * saveSupplementaryFiles(suppFiles, Integer.parseInt(ids[0])); } } catch
	 * (QueryException_Exception e) { // here comes the workaround
	 * System.out.println("Supplemental Data not retrievable.");
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

	protected void runAllMethods(WSCitationImpl wSCitationImpl)
			throws QueryException_Exception {

		List<SearchTerm> searchTerms =

		wSCitationImpl.listSearchFields("testclient@ebi.ac.uk")
				.getSearchTermList().getSearchTerms();
		printSearchTerms(searchTerms);

		String query = "HAS_UNIPROT:y HAS_FREE_FULLTEXT:y HAS_REFLIST:y HAS_XREFS:y sort_cited:y";
		// String query = "TITLE:blood";
		String dataSet = "metadata";
		int offSet = 0;
		String resultType = "lite";
		boolean synonym = false;
		String email = "testclient@ebi.ac.uk";
		String database = "UNIPROT";
		ResponseWrapper searchResult;

		ResponseWrapper resultsBean = wSCitationImpl.searchPublications(query,
				dataSet, resultType, offSet, synonym, email);
		List<Result> beanCollection = resultsBean.getResultList().getResult();

		for (Result bean : beanCollection) {

			printResultBean(bean);

			searchResult = wSCitationImpl.getDatabaseLinks(bean.getPmid(),
					bean.getSource(), database, 0, email);
			printFirstDbCrossReference(searchResult);

			searchResult = wSCitationImpl.getCitations(bean.getPmid(),
					bean.getSource(), 0, email);
			printFirstCitedByData(searchResult);

			searchResult = wSCitationImpl.getReferences(bean.getPmid(),
					bean.getSource(), 0, email);
			printFirstReference(searchResult);

			searchResult = wSCitationImpl.getTextMinedTerms(bean.getPmid(),
					bean.getSource(), "organism", 0, email);
			printFirstMinedTerm(searchResult);

		}

		ResponseWrapper profileBean = wSCitationImpl.profilePublications(query,
				dataSet, "PUB_TYPE", synonym, email);
		printFirstProfile(profileBean);

		Result fulltextXML = wSCitationImpl.getFulltextXML("15314653", "pmc",
				"");
		Result files = wSCitationImpl.getSupplementaryFiles("PMC3280045",
				"pmc", "testclient@ebi.ac.uk");

		if (fulltextXML.getFullText() != null) {
			// saveFullText(fulltextXML);
		}
		if (files.getSupplementaryFiles() != null) {
			// saveSupplementaryFiles(files);
		}
	}

	private void printResultBean(Result citation) {
		// writeToOutput("score: " + resultBean.getScore());
		// Citation citation = resultBean.getCitation();
		writeToOutput(", title: " + citation.getTitle());
		writeToOutput(", is open access: " + citation.getIsOpenAccess());
		DataHandler dataHandler = citation.getFullText();
		try {
			if (dataHandler != null) {
				writeToOutputln("");
				// dataHandler.writeTo(System.out);
				dataHandler.getInputStream().close();
				writeToOutputln("");
				writeToOutputln("");
			}
		} catch (Exception e) {
		}
	}

	private void printSearchTerms(List<SearchTerm> searchTerms) {
		for (SearchTerm searchTerm : searchTerms) {
			writeToOutput(searchTerm.getTerm());
			List<String> dataSets = searchTerm.getDataSets();
			for (String dataSet : dataSets) {
				writeToOutput("\t" + dataSet);
			}
			writeToOutputln("");
		}
	}

	private void printFirstCitedByData(ResponseWrapper searchResult) {
		writeToOutput("");
		writeToOutputln("Printing Cited By Data");

		writeToOutput(searchResult.getCitationList().getCitation().get(0)
				.getTitle());
	}

	private void printFirstDbCrossReference(ResponseWrapper searchResult) {
		writeToOutput("");
		writeToOutputln("Printing DbCrossReference Data");
		DbCrossReference dbCrossReference = searchResult
				.getDbCrossReferenceList().getDbCrossReference().get(0);
		DbCrossReferenceInfo dbCrossReferenceInfo = dbCrossReference
				.getDbCrossReferenceInfo().get(0);
		writeToOutputln(dbCrossReference.getDbName() + ": "
				+ dbCrossReferenceInfo.getInfo1() + ", "
				+ dbCrossReferenceInfo.getInfo2() + ", "
				+ dbCrossReferenceInfo.getInfo3() + ", "
				+ dbCrossReferenceInfo.getInfo4() + ", ");
	}

	private void saveSupplementaryFiles(Result citation, Integer out_name) {
		DataHandler dataHandler = citation.getSupplementaryFiles();
		if (dataHandler != null) {
			// writeToOutputln("Saving zip file");
			File dir = new File("data/supp");
			File file = new File(dir.getAbsolutePath(), out_name + ".zip");
			try {
				FileOutputStream out = new FileOutputStream(file);
				dataHandler.writeTo(out);
				dataHandler.getInputStream().close();
				out.close();
				writeToOutputln("Supplemental Data retrieved successfully!");
			} catch (Exception e) {
			}
		}
	}

	private void printFirstReference(ResponseWrapper searchResult) {
		writeToOutput("");
		writeToOutputln("Printing Reference");
		try {

			writeToOutputln(searchResult.getReferenceList().getReference()
					.get(0).getTitle());
		} catch (Exception e) {
		}
	}

	private void printFirstMinedTerm(ResponseWrapper searchResult) {
		writeToOutput("");
		writeToOutputln("Printing Mined Terms");
		try {

			writeToOutputln(searchResult.getSemanticTypeList()
					.getSemanticType().get(0).getTmSummary().get(0).getTerm());
		} catch (Exception e) {
		}
	}

	private void printFirstProfile(ResponseWrapper profileBean) {
		writeToOutput("");
		writeToOutputln("Printing Profile");
		try {

			writeToOutputln(profileBean.getProfileList().getPubType().get(0)
					.getName());
		} catch (Exception e) {
		}
	}

	private void saveFullText(Result citation, Integer out_name) {
		DataHandler dataHandler = citation.getFullText();
		if (dataHandler != null) {
			// writeToOutputln("");
			// writeToOutputln("Saving Full text");
			try {
				File dir = new File("data");
				File fulltext = new File(dir.getAbsolutePath(), out_name
						+ ".xml");

				FileOutputStream out = new FileOutputStream(fulltext);
				dataHandler.writeTo(out);
				dataHandler.getInputStream().close();
				out.close();
				writeToOutputln("Full text retrieved successfully!");
			} catch (Exception e) {
			}
		}
	}

	void writeToOutputln(String param) {
		System.out.println(param);
	}

	void writeToOutput(String param) {
		System.out.print(param);
	}

}