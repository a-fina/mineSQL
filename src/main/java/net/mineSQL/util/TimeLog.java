/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.util;

import java.util.Calendar;

/**
 *
 * @author afinamore
 */
public class TimeLog {

	private long startTime;

	public TimeLog() {
		startTime = Calendar.getInstance().getTimeInMillis();
	}

	public String getTime(){
		long elapsedTime = Calendar.getInstance().getTimeInMillis() - startTime;

		return "Execution - class:" + Thread.currentThread().getStackTrace()[2].getClassName() + 
				" method: " + Thread.currentThread().getStackTrace()[2].getMethodName() +
				" line: " + Thread.currentThread().getStackTrace()[2].getLineNumber() +
				" time: " +  ( elapsedTime ) + " ms.";

	}
}
