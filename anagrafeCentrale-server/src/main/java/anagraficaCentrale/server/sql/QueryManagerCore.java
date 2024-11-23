package anagraficaCentrale.server.sql;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.utils.ScriptUtils;


@SuppressWarnings("unused")
public class QueryManagerCore {
	final static Logger logger = LogManager.getRootLogger();
	public static final String USAGE = "not runnable from command prompt";
	private boolean verbose = false;
	private boolean enableUpdate = false;
	private boolean header = false;
	private boolean plain = false;
	private int fetchSize = 50;
	private long batchSize = 0;
	private String cfg;
	private int totalRecordToProcess = 0;
	private int totalRecordWithError = 0;
	private String connectionName;
	private Statement statement;

	private String fileSeparator = ",";

	private boolean fileOutputEnabled = false;
	private String inputFileName;
	private String outFileName;
	
	private boolean autocommit;
	
	private boolean isOpening;

	public QueryManagerCore(String[] args) throws FileNotFoundException{
		long time, startTime;
		startTime = getTime();


		String enableUpdateString = ScriptUtils.getParam(args, "-u");
		if(enableUpdateString != null)
			setUpdateEnabled(true);

		time = getTime();
//		System.out.println("****************End of procedure****************\n"
//				+ "#record processed:\t"+totalRecordToProcess+"\n"
//				+ "process ends in:\t"+(getTime()-startTime)+"s.\n"
//				+ "************************************************");
		isOpening = false;
	}

	private String replaceWithParam(String query, String attributeRow) {
		String[] args = attributeRow.split(fileSeparator);
		for (int i = 1; i <= args.length; i++) {
			while(query.contains("!f"+i)) query=query.replace("!f"+i, args[i-1]);
		}
		return query;
	}

	public long getTime() {
		return System.currentTimeMillis()/1000;
	}


	public String getDefaultFileSeparator() {
		return fileSeparator;
	}

	public void setUpdateEnabled(boolean b) {
		enableUpdate = b;
	}

	public boolean isUpdateEnabled() {
		return enableUpdate;
	}

	public String getDefaultOutFileName() {
		return "lista.txt.out";
	}

	public String getDefaultInputFileName() {
		return "lista.txt";
	}

	public void setOptions(boolean printInFile, String outFileName, String inputFileName, String fileSeparator, long fetchSize, long batchSize) {
		fileOutputEnabled = printInFile;
		this.outFileName = outFileName;
		this.inputFileName = inputFileName;
		this.fileSeparator = fileSeparator;
		this.fetchSize = (int)fetchSize;
		this.batchSize = batchSize;
	}

	public boolean isFileOutputEnabled() {
		return fileOutputEnabled;
	}

	public void setCfg(String s){
		this.cfg = s;
		statement = null;
		this.connectionName = s;
	}

	public String[][] runQuery(String query) throws SQLException, ClassNotFoundException, FileNotFoundException {
		if(statement == null)
			openConnection();
		statement.getConnection().setAutoCommit(this.autocommit);
		if(query.toLowerCase().startsWith("select") || query.toLowerCase().startsWith("desc")){
			System.out.println("running query:" + query);
			ResultSet rs = statement.executeQuery(query);
			if(!rs.first()) return new String[][]{{"no result"}};
			LinkedList<String[]> output = new LinkedList<String[]>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			String[] line = new String[columnsNumber];
			for (int i = 1; i <= columnsNumber; i++)
				line[i-1] = rsmd.getColumnName(i);
			output.add(line);
			
			StringBuilder sb = new StringBuilder();
			rs.beforeFirst();
			while (rs.next()) {
				line = new String[columnsNumber];
				for (int i = 1; i <= columnsNumber; i++)
					line[i-1] = rs.getString(i);
				output.add(line);
			}
			return output.toArray(new String[0][0]);
		}else{
			//TODO insert update
			throw new UnsupportedOperationException("Must be a SELECT operation");
		}
	}
	
	public String[][] runQueryList(String query, File inputFile) throws SQLException, ClassNotFoundException, FileNotFoundException {
		Scanner sc = new Scanner(inputFile);
		ArrayList<String[]> listOfResults = new ArrayList<String[]>();
		while(sc.hasNext()){
			String attributeRow = sc.nextLine();
			String modQuery = replaceWithParam(query, attributeRow);
			LinkedList<String[]> res = new LinkedList<String[]>(Arrays.asList(runQuery(modQuery)));
			if(res.get(0)[0].equals("no result")) 
				continue;
			if(!listOfResults.isEmpty())
				res.remove(0); // se non � il primo che appendo, rimuovo l'intestazione dalla query
			listOfResults.addAll(res);
		}
		if(listOfResults.isEmpty())
			listOfResults.add(new String[]{"no result"});
		sc.close();
		return listOfResults.toArray(new String[0][0]);
	}

	
	public void openConnection() throws SQLException, ClassNotFoundException, FileNotFoundException{
		if(isOpening){
			while(isOpening);
			return;
		}
		isOpening = true;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		if(cfg == null || !(new File(cfg).exists())) throw new SQLException("Missing configuration file");
		Scanner sc = new Scanner(new File(cfg));
		String connectionName = null;
		String[] connectionPath = null;
		while(sc.hasNextLine()){ //load site configuration
			connectionName = sc.nextLine();
			if(connectionName.charAt(0)=='#') continue;
			String token2;
			while ((token2=sc.nextLine()).charAt(0)=='#');
			connectionPath = token2.split(";");
			break;
		}
		sc.close();
		if(connectionName == null) throw new SQLException("Invalid configuration file");
		int resultsetType=0;
		if(enableUpdate)
			resultsetType = ResultSet.CONCUR_UPDATABLE;
		else
			resultsetType = ResultSet.CONCUR_READ_ONLY;
		System.out.print("Connecting on "+connectionName+"..");
		if(batchSize==0)
			statement = DriverManager.getConnection(connectionPath[0], connectionPath[1], connectionPath[2])
			.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, resultsetType);
		else//allora � un prepare statement
			statement = DriverManager.getConnection(connectionPath[0], connectionPath[1], connectionPath[2])
			.prepareStatement("", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		if(!enableUpdate) //select only
			statement.setFetchSize(fetchSize);
		this.connectionName = connectionName;
		System.out.println("Done");
		isOpening = false;
	}
	

	public long getBatchSize() {
		return batchSize;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setAutocommit(boolean selection) {
		this.autocommit = selection;
	}
	
	public void commit() throws SQLException {
		statement.getConnection().commit();
	}

	public void rollback() throws SQLException {
		statement.getConnection().rollback();
	}

	public void stopQuery() throws SQLException {
		if(statement!=null){
			this.statement.close();
			try {
				openConnection();
			} catch (ClassNotFoundException|FileNotFoundException e) {
				logger.error(e);
			}
		}
	}

	public String getFileSeparator() {
		return fileSeparator;
	}
	
	public String getConnectionName() {
		return this.connectionName;
	}
}
