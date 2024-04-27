/**
 * 
 */
package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.resource.FilterableResourcePanel;
import anagraficaCentrale.client.gui.resource.ServiceElement;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * 
 *
 */
public class ServicePanel extends FilterableResourcePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ServicePanel(OperationPanel op,PortalType portalType, boolean isAdmin) {
		super();
		//addResource(new ServiceElement(op, "Service 1"));

		switch(portalType){
		case COMUNE:
			if(!isAdmin){
				//Appuntamento carta d’identità
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceAPP_CI, ServiceType.APP_CI));
				//Carta d’identità temporanea
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCI_TEMP, ServiceType.CI_TEMP));
				//Cambio residenza 
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCAM_RES, ServiceType.CAM_RES));
				//Certificato di nascita 
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCERT_NASC, ServiceType.CERT_NASC));
				//Certificato di matrimonio
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCERT_MATR, ServiceType.CERT_MATR));
				//Stato di famiglia
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceSTAT_FAM, ServiceType.STAT_FAM));
			} else {
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCreateUser, ServiceType.ADM_CREAZ_USR));
			}
			break;
		case OSPEDALE:
			if(!isAdmin){
				//Cambio medico
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCAM_MED, ServiceType.CAM_MED));
				//Prenotazione visita
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServicePREN_VIS, ServiceType.PREN_VIS));
				//Pagamento ticket
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServicePAG_TICK, ServiceType.PAG_TICK));
			}else{

			}
			break;
		case SCUOLA:
			if(!isAdmin){
				//Pagamento retta
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServicePAG_RET, ServiceType.PAG_RET));
				//Pagamento servizio mensa
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServicePAG_MEN, ServiceType.PAG_MEN));
				//Iscrizione
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceISCRIZ, ServiceType.ISCRIZ));
				//Richiesta colloquio insegnanti
				addResource(new ServiceElement(op, GUIConstants.LANG.lblServiceCOLL_INS, ServiceType.COLL_INS));
			}else{

			}
			break;
		}

	}


}
