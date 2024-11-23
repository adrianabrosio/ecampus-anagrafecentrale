package anagraficaCentrale.client.gui.resource;

import java.io.FileNotFoundException;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;

public class ReportElement extends AbstractResourceElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id, fileTitle, fileContent, filePath;
	
	public ReportElement(OperationPanel op, String id, String fileDisplayName, String fileTitle, String fileContent, String filePath) {
		super(op, fileDisplayName);
		this.id = id;
		this.fileTitle = fileTitle;
		this.fileContent = fileContent;
		this.filePath = filePath;
	}

	@Override
	protected void executeAction() {
		//se il fileName a db e' vuoto, allora il file e' da generare
		if(this.fileTitle != null && fileContent != null)
			this.operationPanel.generateAndDownloadFile(this.fileTitle, this.fileContent);
		else if(filePath != null)
			this.operationPanel.downloadFile(this.filePath);
		else //file not found
			this.operationPanel.popupError(new FileNotFoundException(GUIConstants.LANG.errReportFileNotFound));
	}

	@Override
	protected void executePostAction() {
		/*NO-OP*/
	}

	@Override
	protected String getButtonIconName() {
		return "downloadButtonIcon.png";
	}

	public String getReportId() {
		return this.id;
	}

}
