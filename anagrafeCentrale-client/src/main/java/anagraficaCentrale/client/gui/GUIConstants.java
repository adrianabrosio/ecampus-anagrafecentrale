package anagraficaCentrale.client.gui;

import java.awt.Color;

/**
 * class that contains all the constants used in the GUI
 * @author Adriana Brosio
 */
public class GUIConstants {
	
	public static final Color BACKGROUND_COLOR_1 = Color.decode("#f7f7f7");
	public static final Color BACKGROUND_COLOR_2 = Color.decode("#EEEEEE");
	public static final Color COMUNE_COLOR = Color.decode("#8CD0E5");
	public static final Color OSPEDALE_COLOR = Color.decode("#826B88");
	public static final Color SCUOLA_COLOR = Color.decode("#DE786A");
	public static final Color OPERATION_PANEL_BACKGROUND = Color.WHITE;
	public static final Color RESOURCE_CARD_BACKGROUND = Color.WHITE;
	
	public static class LANG {
		public static final String lblCerca = "Cerca ";
		public static final String lblServizi = "Servizi";
		public static final String lblOperationPanelTitle = "Anagrafica Centrale";
		public static final String lblComuneTitle = "Anagrafe";
		public static final String lblOspedaleTitle = "Ospedale";
		public static final String lblScuolaTitle = "Scuola";
		public static final String lblAccountBtnSide = "Profilo";
		public static final String lblServiceBtnSide = "Servizi";
		public static final String lblNotificationBtnSide = "Notifiche";
		public static final String lblNotication = "Notifiche";
		public static final String lblLoginUsername = "Username";
		public static final String lblLoginPassword = "Password";
		public static final String lblLoadingMessage = "Caricamento in corso...";
		public static final String lblLoadingTitle = "  Anagrafica Centrale";
		public static final String popupClosureDesc = "Sicuro di voler uscire?";
		public static final String popupClosureTitle = "Esci dal programma";
		public static final String lblProfile = "Il mio Profilo";
		public static final String lblReportBtnSide = "Documenti";
		public static final String lblService = "Servizi";
		public static final String lblReport = "Documenti";
		public static final String lblCollapseBtnSide = "Chiudi";
		public static final String lblUserCreationSrvTitle = "Creazione Utente";
		public static final String lblCloseBtn = "Chiudi";
		public static final String userCreateConfirmMsg = "Per favore controlla i dati. Confermi di voler procedere alla creazione dell'utente?";
		public static final String userCreateConfirmTitle = "Creazione utente";
		public static final String lbluserCreationUsername = "Username";
		public static final String lbluserCreationFirstName = "Nome";
		public static final String lbluserCreationCreateUserBtn = "Salva";
		public static final String lbluserCreationTaxIDCode = "Codice Fiscale";
		public static final String lbluserCreationGender = "Sesso";
		public static final String lbluserCreationBirthDate = "Data di Nascita";
		public static final String lbluserCreationSurname = "Cognome";
		public static final String lbluserCreationAuthorization = "Autorizzazioni";
		public static final String lblErrorMandatoryField = "Campo obbligatorio";
		public static final String lblTaxIdWrongSize = "Formato errato";
		public static final String lblApplyFilterNoResults = "Nessun risultato";
		public static final String tooltipDeleteReadNotifBtn = "Cancella tutte le notifiche lette";
		public static final String lblExpandBtnSide = "Espandi/collassa menu";
		public static final String lblDeleteReadNotifBtn = "Cancella notifiche lette";
		public static final String msgUserCreateSuccess = "Utente creato con successo. Ricorda che la password iniziale � uguale allo user id!";
		public static final String lblAppointmentDate = "Data appuntamento";
		public static final String lblAppointmentCreateBtn = "Prenota";
		public static final String msgAppointmentSuccess = "Appuntamento richiesto con successo. Una volta confermata la data, riceverai una notifica.";
		public static final String lbl_APP_CI_SrvTitle = "Appuntamento Carta d'identit�";
		public static final String lblAppointmentListUser = "Intestatario richiesta";
		public static final String lbl_APP_CI_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Gentile cittadino/a,<br/><br/>Per richiedere il rilascio della carta d'identit� elettronica � necessario prenotare un appuntamento.<br/><br/>Documenti da presentare:<br/><ul><li> Documento di identit� valido (se disponibile)</li><li> Tessera sanitaria</li><li> Codice fiscale</li><li> Fototessera recente (formato 3x4 cm)</li></ul><br/>Dati anagrafici:</p></body></html>";
		public static final String lbluserCreationBirthTown = "Citt� di nascita";
		public static final String lbluserCreationBirthState = "Stato di nascita";
		public static final String lbluserCreationAddress = "Indirizzo";
		public static final String lbluserCreationTown = "Citt� di residenza";
		public static final String lbluserCreationProvince = "Provincia";
		public static final String lbluserCreationState = "Stato";
		public static final String lbluserCreationZipCode = "CAP";
		public static final String lblErrorOnlyDigit = "Ammessi solo caratteri numerici";
		public static final String lblServiceCreateUser = "Crea utente";
		public static final String lblServiceAPP_CI = "Appuntamento Carta d'identit�";
		public static final String lblServiceCI_TEMP = "Carta d'identit� temporanea";
		public static final String lblServiceCAM_RES = "Cambio residenza";
		public static final String lblServiceCOLL_INS = "Richiesta colloquio insegnanti";
		public static final String lblServiceISCRIZ = "Domanda d'iscrizione";
		public static final String lblServicePAG_MEN = "Pagamento mensa";
		public static final String lblServicePAG_RET = "Pagamento retta";
		public static final String lblServicePAG_TICK = "Pagamento ticket";
		public static final String lblServicePREN_VIS = "Prenotazione visita";
		public static final String lblServiceCAM_MED = "Cambio medico";
		public static final String lblServiceSTAT_FAM = "Stato di famiglia";
		public static final String lblServiceCERT_MATR = "Certificato di matrimonio";
		public static final String lblServiceCERT_NASC = "Certificato di nascita";
		public static final String lbl_COLL_INS_SrvText = "Si richiede un colloquio con gli insegnanti per l'alunno:";
		public static final String lbl_COLL_INS_SrvTitle = "Colloquio insegnanti";
		public static final String lblErrorUnableToCreateConnection = "Impossibile stabilire una connessione con il server, riprovare?";
		public static final String msgSimpleRequestSuccess = "Richiesta sottomessa con successo";
		public static final String lblSimpleRequestCreateBtn = "Richiedi";
		public static final String lbl_CERT_NASC_SrvTitle = "Richiesta certificato di nascita";
		public static final String lbl_CERT_NASC_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>l'emissione del certificato di Nascita</p></body></html>";
		public static final String lbl_CERT_MATR_SrvTitle = "Richiesta certificato di matrimonio";
		public static final String lbl_CERT_MATR_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>l'emissione del certificato di Matrimonio</p></body></html>";
		public static final String lbl_STAT_FAM_SrvTitle = "Richiesta stato di famiglia";
		public static final String lbl_STAT_FAM_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>l'emissione del certificato Stato di Famiglia</p></body></html>";
		public static final String lbl_CAM_RES_SrvTitle = "Richiesta di cambio residenza";
		public static final String lbl_CAM_RES_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>che l'attuale indirizzo di residenza venga modificato con il seguente</p></body></html>";
		public static final String msgResidenceChangeSuccess = "Richiesta di cambio residenza inoltrata. Riceverai una notifica con il riscontro";
		public static final String lbluserCreationBirthProvince = "Provincia di nascita";
		public static final String msgCITempSuccess = "Carta d'identit� temporanea richiesta con successo";
		public static final String lblCI_TEMP_reason = "Motivo della richiesta";
		public static final String lbl_CI_TEMP_SrvTitle = "Carta d'identit� temporanea";
		public static final String lbl_CI_TEMP_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>l'emissione della carta d'identit� temporanea con validit� 5 giorni</p></body></html>";
		public static final String lblPREN_VIS_visit = "Visita";
		public static final String lbl_PREN_VIS_SrvText = "Compila i seguenti dati per procedere alla prenotazione:";
		public static final String lbl_PREN_VIS_SrvTitle = "Prenotazione visita";
		public static final String msgCAM_MEDSuccess = "Richiesta di cambio medico inoltrata. Riceverai una notifica con il riscontro";
		public static final String lbl_CAM_MED_SrvTitle = "Richiesta cambio medico";
		public static final String lbl_CAM_MED_SrvText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>che l'attuale medico venga modificato con il seguente</p></body></html>";
		public static final String lblTicketPaymentNumber = "#Ticket";
		public static final String lblTicketPaymentCard = "Numero carta";
		public static final String lblPayBtn = "Paga";
		public static final String lbl_PAG_TICK_SrvText = "Compila il modulo inserendo il numero ticket e il numero di carta";
		public static final String msgTicketPaymentSuccess = "Pagamento completato con successo";
		public static final String errInvalidCardNumber = "Il numero di carta deve contenere almeno 15 cifre";
		public static final String lblSchoolFee = "Quota (senza decimali)";
		public static final String lbl_PAG_RET_SrvTitle = "Pagamento Retta";
		public static final String[] monthValues = new String[]{"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
		public static final String lbl_PAG_RET_SrvText = "Compila il modulo inserendo il mese di competenza e la quota della retta da pagare";
		public static final String lblTicketPaymentUser = "Codice Fiscale";
		public static final String lbl_PAG_TICK_SrvTitle = "Pagamento Ticket Ospedaliero";
		public static final String lbl_PAG_MEN_SrvTitle = "Pagamento Mensa Scolastica";
		public static final String lbl_PAG_MEN_SrvText = "Compila il modulo inserendo il mese di competenza e la quota relativa al servizio mensa da pagare";
		public static final String lblMonth = "Mese competenza";
		public static final String lbl_ISCRIZ_SrvTitle = "Iscrizione";
		public static final String lbl_ISCRIZ_SrvText = "<html><head><style>p { word-wrap: normal;}</style></head><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\">Il/la sottoscritto/a <b>!first_name !surname</b>, nato/a a <b>!birth_town</b> (<b>!birth_province</b>) il <b>!birthdate</b>,<br/>CF <b>!tax_id_code</b> residente in via <b>!address, !town (!province), !zip_code</b></p><p align=\"center\"><b>RICHIEDE</b></p><p>l'iscrizione del/la proprio/a figlio/a &first_name &surname, nato/a a &birth_town, &birth_province il &birthdate,<br> CF &tax_id_code per l'anno scolastico 2024/2025</p></body></html>";
		public static final String errUserInvalid = "Impossibile trovare l'utente richiesto";
		public static final String lblServiceEditUser = "Modifica utente";
		public static final String lbluserCreationSearchUserBtn = "Cerca Utente";
		public static final String msgUserEditSuccess = "Utente modificato con successo";
		public static final String userEditConfirmMsg = "Per favore controlla i dati. Confermi di voler procedere alla modifica dell'utente?";
		public static final String userEditConfirmTitle = "Modifica utente";
		public static final String lbluserCreationSearchUserPlaceholder = "Scrivi UserID o CF dell'utente da ricercare e premi invio";
		public static final String lblAdminSupportButtonBtnSide = "Gestione richieste";
		public static final String lblAdminSupport = "Gestione richieste";
		public static final String lblAcceptRequestBtn = "Accetta";
		public static final String lblRejectRequestBtn = "Rifiuta";
		public static final String lbl_APP_CI_SrvAdminRequestText ="<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Codice Fiscale</td><td>&tax_id_code</td></tr><tr><td>Data appuntamento</td><td>&appointment_date</td></tr></table></p></body></html>";
		public static final String lbl_CAM_MED_SrvAdminRequestText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Nome nuovo medico</td><td>&medicFirstName</td></tr><tr><td>Cognome nuovo medico</td><td>&medicSurname</td></tr></table></p></body></html>";
		public static final String lbl_CAM_RES_SrvAdminRequestText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Indirizzo</td><td>&newAddress</td></tr><tr><td>Citt�</td><td>&newTown</td></tr><tr><td>Provincia</td><td>&newProvince</td></tr><tr><td>CAP</td><td>&newZipCode</td></tr></table></p></body></html>";
		public static final String lbl_CERT_MATR_SrvAdminRequestText = null;
		public static final String lbl_CERT_NASC_SrvAdminRequestText = null;
		public static final String lbl_CI_TEMP_SrvAdminRequestText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Motivo richiesta</td><td>&reason</td></tr></table></p></body></html>";
		public static final String lbl_COLL_INS_SrvAdminRequestText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Codice Fiscale</td><td>&tax_id_code</td></tr><tr><td>Data appuntamento</td><td>&appointment_date</td></tr></table></p></body></html>";
		public static final String lbl_ISCRIZ_SrvAdminRequestText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Codice Fiscale alunno</td><td>&tax_id_code</td></tr></table></p></body></html>";
		public static final String lbl_PAG_MEN_SrvAdminRequestText = null;
		public static final String lbl_PAG_RET_SrvAdminRequestText = null;
		public static final String lbl_PAG_TICK_SrvAdminRequestText = null;
		public static final String lbl_PREN_VIS_SrvAdminRequestText = "<html><body style=\"font-size: 16px;\"><p align=\"justify\" style=\"word-break:normal;white-space:normal\"><p>Richiedente:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>User ID</td><td>!id</td></tr><tr><td>Nome</td><td>!first_name</td></tr><tr><td>Cognome</td><td>!surname</td></tr><tr><td>Codice fiscale</td><td>!tax_id_code</td></tr></table><br/><p>Dettagli richiesta:</p><br/><table border=1 bgcolor=#eeeeee FRAME=VOID CELLSPACING=0><tr><td>Stato richiesta</td><td>&status</td></tr><tr><td>Codice fiscale paziente</td><td>&tax_id_code</td></tr><tr><td>Tipologia visita</td><td>&medical_examination</td></tr><tr><td>Data visita</td><td>&appointment_date</td></tr></table></p></body></html>";
		public static final String lbl_STAT_FAM_SrvAdminRequestText = null;
		public static final String lblAdminRequestManagementSrvTitle = "Richiesta";
		public static final String lbl_APP_CI_SrvNotificationText = "Appuntamento Carta D'identit�";
		public static final String lbl_CAM_MED_SrvNotificationText = "Cambio medico";
		public static final String lbl_CAM_RES_SrvNotificationText = "Cambio di residenza";
		public static final String lbl_CERT_MATR_SrvNotificationText = "Certificato di matrimonio";
		public static final String lbl_CERT_NASC_SrvNotificationText = "Certificato di nascita";
		public static final String lbl_CI_TEMP_SrvNotificationText = "Carta D'identit� temporanea";
		public static final String lbl_COLL_INS_SrvNotificationText = "Colloquio insegnanti";
		public static final String lbl_ISCRIZ_SrvNotificationText = "Domanda d'iscrizione";
		public static final String lbl_PAG_MEN_SrvNotificationText = "Pagamento mensa";
		public static final String lbl_PAG_RET_SrvNotificationText = "Pagamento retta scolastica";
		public static final String lbl_PAG_TICK_SrvNotificationText = "Pagamento ticket";
		public static final String lbl_PREN_VIS_SrvNotificationText = "Prenotazione visita";
		public static final String lbl_STAT_FAM_SrvNotificationText = "Certificato di stato di famiglia";
		public static final String lblRequestAccepted = "Accettato";
		public static final String lblRequestDeclined = "Rifiutato";
		public static final String errRequestIdInvalid = "Impossibile aprire la richiesta relativa a questa notifica";
		public static final String errorNoRelationFound = "Nessuna relazione di parentela trovata";
		public static final String lblOldPassword = "Password attuale";
		public static final String lblNewPassword = "Nuova password";
		public static final String lblConfirmPassword = "Conferma nuova password";
		public static final String lblPasswordChanged = "Password cambiata con successo";
		public static final String lblSaveBtn = "Salva";
		public static final String errPasswordResetSameAsUsername = "La nuova password non pu� essere uguale all'username";
		public static final String errPasswordResetDifferentPassword = "Le password non corrispondono";
		public static final String msgPasswordResetSuccess = "Password resettata con successo. Per favore effettua un nuovo accesso";
		public static final String msgPasswordResetMandatory = "La password deve essere modificata per poter accedere";
		public static final String errFillMandatoryFields = "Compilare tutti i campi obbligatori";
		public static final String msgUsernameEmpty = "Inserire username";
		public static final String lblLoginPwReset = "Reset password?";
		public static final String lblRequestStatusAccepted = "Accettata";
		public static final String lblRequestStatusRejected = "Rifiutata";
		public static final String lblRequestStatusToBeManaged = "Da gestire";
		public static final String msgCertificateRequestSuccess = "Il certificato � stato prodotto. Accedi alla sezione allegati per scaricarlo";
		public static final String lbl_CERT_NASC_ReportDisplayName = "Certificato di nascita";
		public static final String lbl_CERT_NASC_ReportTitle = "Certificato di Nascita";
		public static final String lbl_CERT_NASC_ReportContent = "Il Comune di !birth_town dichiara valido il presente certificato rilasciato a\\n\\nNome: !first_name \\nCognome: !surname\\nCF: !tax_id_code\\nGenere: !gender\\nData di nascita: !birthdate\\nLuogo di nascita: !birth_town (!birth_province) - !birth_state\\n";
		public static final String errReportFileNotFound = "Impossibile recuperare il file relativo al documento selezionato";
		public static final String lbl_STAT_FAM_ReportDisplayName = "Stato di famiglia";
		public static final String lbl_STAT_FAM_ReportTitle = "Stato di Famiglia";
		public static final String lbl_STAT_FAM_ReportContent = "Il Comune di !town dichiara valido il presente certificato rilasciato a\\n\\nNome: !first_name \\nCognome: !surname\\nCF: !tax_id_code\\nGenere: !gender\\nData di nascita: !birthdate\\nLuogo di nascita: !birth_town (!birth_province) - !birth_state\\n\\n\\nDichiara altres� che il suo nucleo familiare � anche composto da:\\n\\n";
		public static final String lbl_STAT_FAM_ReportSingleComponentString = " - !surname !first_name nato/a a !birth_town in data !birthdate con codice fiscale !tax_id_code\\n";
		public static final String lbl_CERT_MATR_ReportDisplayName = "Certificato di matrimonio";
		public static final String lbl_CERT_MATR_ReportTitle = "Certificato di matrimonio";
		public static final String lbl_CERT_MATR_ReportContent = "Il Comune di !town dichiara valido il presente certificato rilasciato a\\n\\nNome: !first_name \\nCognome: !surname\\nCF: !tax_id_code\\nGenere: !gender\\nData di nascita: !birthdate\\nLuogo di nascita: !birth_town (!birth_province) - !birth_state\\n";
		public static final String msgPDFFileGenerated = "Il file PDF � stato salvato nel path: ";
		public static final String lbl_CI_TEMP_ReportDisplayName = "CI Temporanea";
		public static final String lbl_CI_TEMP_ReportTitle = "Carta d''identit� temporanea";
		public static final String lbl_CI_TEMP_ReportContent = "Il Comune di !town rilascia il presente documento con il valore di carta d''identit� provvisoria a:\\n\\nNome: !first_name \\nCognome: !surname\\nCF: !tax_id_code\\nGenere: !gender\\nData di nascita: !birthdate\\nLuogo di nascita: !birth_town (!birth_province) - !birth_state\\n\\nIl presente documento ha validit� 5 giorni dalla data di emissione.\\nQuesto documento non � valido per l''espatrio.\\nSi ricorda di richiedere un appuntamento per il rilascio della carta d''identit� definitiva.";
		
	}
}
