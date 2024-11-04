package anagraficaCentrale.client.gui.resource;

import anagraficaCentrale.client.gui.OperationPanel;

public class ReportElement extends AbstractResourceElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id, fileName, fileDisplayName, fileTitle, fileContent;
	
	public ReportElement(OperationPanel op, String id, String fileName, String fileDisplayName, String fileTitle, String fileContent) {
		super(op, fileDisplayName);
		this.id = id;
		this.fileName = fileName;
		this.fileDisplayName = fileDisplayName;
		this.fileTitle = fileTitle;
		this.fileContent = fileContent;
	}

	@Override
	protected void executeAction() {
		//se il fileName a db e' vuoto, allora il file e' da generare
		if(this.fileName == null || this.fileName.trim().equals(""))
			this.operationPanel.generateAndDownloadFile(this.fileTitle, this.fileContent);
		else
			this.operationPanel.downloadFile(this.fileName);
	}

	@Override
	protected void executePostAction() {
		/*NO-OP*/
	}

	@Override
	protected String getButtonIconName() {
		return "downloadButtonIcon.png";
	}



}
