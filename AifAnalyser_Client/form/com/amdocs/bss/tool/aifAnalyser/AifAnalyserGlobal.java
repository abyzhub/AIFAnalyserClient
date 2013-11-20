/**
 * 
 */
package com.amdocs.bss.tool.aifAnalyser;

import com.amdocs.uif.workspace.UifForm;
import com.amdocs.uif.action.UifLaunchAction;
import com.amdocs.uif.workspace.UifFormEvent;

/**
 * @author ARAI
 * 
 */
public class AifAnalyserGlobal extends UifForm {

	protected UifLaunchAction LaunchAifAnalyser;

	/**
	 * @param event
	 *            The event object.
	 */
	public void form_PostOpen(UifFormEvent event) {
		String initParam = getWorkspace().getInitParameter("AifAnalyser");
		if ("true".equals(initParam)) {
			LaunchAifAnalyser.setVisible(true);
		}
	}

}
