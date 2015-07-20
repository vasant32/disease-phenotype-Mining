package whatizitws.core;

public class TermFrequencyData {
	
	String pubmedId;
	String ontoID;
	float termFreq;

	public TermFrequencyData(String pmid, String tempOID, float tf) {
		this.pubmedId = pmid;
		this.ontoID = tempOID;
		this.termFreq = tf;
	}

	/**
	 * @return the pubmedId
	 */
	public String getPubmedId() {
		return pubmedId;
	}

	/**
	 * @param pubmedId the pubmedId to set
	 */
	public void setPubmedId(String pubmedId) {
		this.pubmedId = pubmedId;
	}

	/**
	 * @return the ontoID
	 */
	public String getOntoID() {
		return ontoID;
	}

	/**
	 * @param ontoID the ontoID to set
	 */
	public void setOntoID(String ontoID) {
		this.ontoID = ontoID;
	}

	/**
	 * @return the termFreq
	 */
	public float getTermFreq() {
		return termFreq;
	}

	/**
	 * @param termFreq the termFreq to set
	 */
	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
	}

	
}
