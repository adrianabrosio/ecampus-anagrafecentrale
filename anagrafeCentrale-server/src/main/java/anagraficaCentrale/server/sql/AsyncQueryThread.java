package anagraficaCentrale.server.sql;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AsyncQueryThread extends Thread{//implements Runnable {
	
	public static final int RUNNING = 1, COMPLETE = 2, ERROR = 3, STOPPED = 4;
	
	private String query;
	private File inputFile;
	private int status;
	private QueryManagerCore qmc;
	private String errorStack;
	private String[][] result;
	private long startTime, endTime;
	
	public AsyncQueryThread(QueryManagerCore qmc, String query){
		this(qmc, query, null);
	}
	
	public AsyncQueryThread(QueryManagerCore qmc, String query, File inputFile) {
		this.query = query;
		this.inputFile = inputFile;
		this.qmc = qmc;
		status = RUNNING;
		startTime = qmc.getTime();
	}
	
	/*@Override
	public synchronized void start() {
		errorStack = "";
		startTime = qmc.getTime();
		super.start();
	}*/
	
	public void stopQuery() {
		/*try{
			this.interrupt();
		}catch(Exception e){
			errorStack+= e.getMessage();
		}*/
		try {
			qmc.stopQuery();
		} catch (Exception e) {
			errorStack+= errorStack +"\n"+ e.getMessage();
		}
		status = STOPPED;
	}

	@Override
	public void run() {
		errorStack = "";
		startTime = qmc.getTime();
		try{
			if(this.inputFile == null || !this.inputFile.exists())
				result = qmc.runQuery(query);
			else
				result = qmc.runQueryList(query, this.inputFile);
			status = COMPLETE;
			endTime = qmc.getTime();
		}catch(Exception e){
			if(status != STOPPED){
				status = ERROR;
				endTime = qmc.getTime();
				StringWriter sw = new StringWriter();
	            e.printStackTrace(new PrintWriter(sw));
	            errorStack = sw.toString();
			}
		}
	}
	
	public int getStatus(){
		return status;
	}

	public String getError() {
		return errorStack;
	}
	
	public String[][] getResult() {
		return result;
	}
	
	public long getExecTime(){
		if(this.status == RUNNING)
			return this.qmc.getTime()-this.startTime;
		return this.endTime-this.startTime;
	}
}
