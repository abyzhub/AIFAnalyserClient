/**
 * 
 */
package com.crmimpl.tool;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.amdocs.uif.action.ActionInfra;
import com.amdocs.uif.action.UifActionEvent;
import com.amdocs.uif.action.UifCancellableActionEvent;
import com.amdocs.uif.action.UifSubmitAction;
import com.amdocs.uif.data.DataModelInfra;
import com.amdocs.uif.data.UifDataModel;
import com.amdocs.uif.data.UifDataset;
import com.amdocs.uif.data.UifTabularDataModel;
import com.amdocs.uif.event.UifCancellableEvent;
import com.amdocs.uif.widgets.TreeNative;
import com.amdocs.uif.widgets.UifButton;
import com.amdocs.uif.widgets.UifComboBox;
import com.amdocs.uif.widgets.UifLabel;
import com.amdocs.uif.widgets.UifPanel;
import com.amdocs.uif.widgets.UifTextField;
import com.amdocs.uif.widgets.UifTree;
import com.amdocs.uif.widgets.tree.TreeNodeInfra;
import com.amdocs.uif.widgets.tree.UifTreeNode;
import com.amdocs.uif.workspace.UifForm;
import com.amdocs.uif.workspace.UifFormEvent;
import com.amdocs.uif.widgets.tree.UifPostTreeExpansionEvent;

/**
 * @author ARAI
 * 
 */
public class AifAnalyserForm extends UifForm {

	protected UifDataModel rootNode;

	protected UifDataModel nodeBoundDM;

	protected UifTabularDataModel OperationsTDM;

	protected UifTabularDataModel backupOperationTDM;

	protected UifDataModel inputDM;

	protected UifDataModel operationDM;

	protected UifDataModel OpDetials;

	protected UifTabularDataModel MapBindingTDM;

	protected UifDataModel ResourceDetail;

	protected UifTabularDataModel Classdetails;

	protected UifTabularDataModel propertyTemp;

	protected UifTree treeAifOperation;

	protected UifButton btnAnalyse;

	protected UifTextField txtQuickSearch;

	protected UifPanel Panel_16;

	protected UifLabel lblUrl1;

	protected UifTextField txtServer;

	protected UifLabel lblColon2;

	protected UifTextField txtPort;

	protected UifLabel lblColon1;

	protected UifTextField txtInstance;

	protected UifLabel lblUser;

	protected UifLabel lblPwd;

	protected UifTextField txtUserName;

	protected UifTextField txtPassword;

	protected UifComboBox cmbOperations;

	protected UifButton btnRead;

	protected UifLabel Label_32;

	protected UifSubmitAction getOperationsList;

	protected UifSubmitAction getOperationsDetail;

	protected UifSubmitAction getItemDetails;

	protected UifButton btnExpandAll;

	private Map<String, Integer> bindings = new HashMap<String, Integer>();

	private String url;
	
	private boolean isExpandAllClicked=false;	

	public AifAnalyserForm() {
		super();
		bindings.put("AdapterBinding", 0);
		bindings.put("AppFrameworkBinding", 1);
		bindings.put("CustomAdapterBinding", 2);
		bindings.put("EjbBinding", 3);
		bindings.put("JmsBinding", 4);
		bindings.put("TuxedoBinding", 5);
		bindings.put("WebServiceBinding", 6);
		bindings.put("CompositeBinding", 7);
	}

	/**
	 * Inputs starting from here
	 */
	private UifTabularDataModel createInputs() {
		UifTabularDataModel childNodeTDM = (UifTabularDataModel) this
				.getDataset().createDataModel("childNodeTDM_Inputs",
						UifDataset.DataModelType.TABULARDATAMODEL, true);
		int count = ((UifTabularDataModel) OpDetials
				.getValue("InputParameters")).getRecordCount();
		for (int i = 0; i < count; i++) {
			int j = childNodeTDM.addNew();
			UifDataModel tmp = ((UifTabularDataModel) OpDetials
					.getValue("InputParameters")).getAt(i);
			childNodeTDM.setValueAt(j, "name",
					tmp.getLocalizedStringValue("Name"));

			UifTabularDataModel grandChildNodeTDM = (UifTabularDataModel) this
					.getDataset().createDataModel("grandChildNodeTDM_Bindings",
							UifDataset.DataModelType.TABULARDATAMODEL, true);
			int props = grandChildNodeTDM.addNew();
			grandChildNodeTDM.setValueAt(props, "name",
					"Type -> " + tmp.getLocalizedStringValue("DataType"));

			childNodeTDM.setValueAt(j, "propertyTDM", grandChildNodeTDM);

		}
		return childNodeTDM;
	}

	/**
	 * Outputs starting from here
	 */
	private UifTabularDataModel createOutputs() {
		UifTabularDataModel childNodeTDM = (UifTabularDataModel) this
				.getDataset().createDataModel("childNodeTDM_Inputs",
						UifDataset.DataModelType.TABULARDATAMODEL, true);
		int count = ((UifTabularDataModel) OpDetials
				.getValue("OutputParameters")).getRecordCount();
		for (int i = 0; i < count; i++) {
			int j = childNodeTDM.addNew();
			UifDataModel tmp = ((UifTabularDataModel) OpDetials
					.getValue("OutputParameters")).getAt(i);
			childNodeTDM.setValueAt(j, "name",
					tmp.getLocalizedStringValue("Name"));

			UifTabularDataModel grandChildNodeTDM = (UifTabularDataModel) this
					.getDataset().createDataModel("grandChildNodeTDM_Bindings",
							UifDataset.DataModelType.TABULARDATAMODEL, true);
			int props = grandChildNodeTDM.addNew();
			grandChildNodeTDM.setValueAt(props, "name",
					"Type -> " + tmp.getLocalizedStringValue("DataType"));

			childNodeTDM.setValueAt(j, "propertyTDM", grandChildNodeTDM);
		}
		return childNodeTDM;
	}

	/**
	 * Bindings starting from here
	 */
	private UifTabularDataModel createBindings() {
		UifTabularDataModel childNodeTDM = (UifTabularDataModel) this
				.getDataset().createDataModel("childNodeTDM_Bindings",
						UifDataset.DataModelType.TABULARDATAMODEL, true);
		int count = ((UifTabularDataModel) OpDetials.getValue("Bindings"))
				.getRecordCount();
		for (int i = 0; i < count; i++) {
			int j = childNodeTDM.addNew();
			UifDataModel binding = ((UifTabularDataModel) OpDetials
					.getValue("Bindings")).getAt(i);
			childNodeTDM.setValueAt(j, "name",
					binding.getLocalizedStringValue("Name"));

			UifTabularDataModel grandChildNodeTDM = (UifTabularDataModel) this
					.getDataset().createDataModel("grandChildNodeTDM_Bindings",
							UifDataset.DataModelType.TABULARDATAMODEL, true);
			int props = grandChildNodeTDM.addNew();
			UifDataModel service = (UifDataModel) binding.getValue("Service");

			String bindingName = ((DataModelInfra) ((UifTabularDataModel) service
					.getValue("Bindings")).getAt(0)).getXdo().getMetaDataName();

			grandChildNodeTDM.setValueAt(props, "name", "Binding -> "
					+ bindingName.substring(bindingName.lastIndexOf(".") + 1));
			props = grandChildNodeTDM.addNew();

			grandChildNodeTDM.setValueAt(props, "name", "Service Name -> "
					+ service.getLocalizedStringValue("Name"));
			props = grandChildNodeTDM.addNew();
			grandChildNodeTDM.setValueAt(props, "name", "ResourceName -> "
					+ binding.getLocalizedStringValue("ResourceName"));

			childNodeTDM.setValueAt(j, "propertyTDM", grandChildNodeTDM);
		}
		return childNodeTDM;
	}

	/**
	 * @param event
	 *            The event object.
	 * @throws Exception
	 */
	public void btnRead_Action(UifCancellableEvent event) {
		this.getOperationsList.execute();
	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void form_PostOpen(UifFormEvent event) {
		addTextListeners();
	}

	private void addTextListeners() {
		((JTextField) txtQuickSearch.getNativeControl())
				.addKeyListener(new KeyListener() {
					public void keyReleased(KeyEvent arg0) {
						char key = arg0.getKeyChar();
						if ((key >= 'A' && key <= 'Z')
								|| (key >= 'a' && key <= 'z') || key == 8
								|| key == 127 || key == 32) {
							restoreBackUpTDM();
							shuffleTDM(txtQuickSearch.getText().trim());
						}
					}

					public void keyTyped(KeyEvent arg0) {
					}

					public void keyPressed(KeyEvent arg0) {
						// restoreBackUpTDM();
						// shuffleTDM(txtQuickSearch.getText().trim());
					}
				});
	}

	private void backUpTDM() {
		for (int i = 0; i < OperationsTDM.getRecordCount(); i++) {
			backupOperationTDM.setAt(backupOperationTDM.addNew(), OperationsTDM
					.getAt(i).clone());
		}
	}

	private void restoreBackUpTDM() {
		OperationsTDM.removeAllDataModels();
		for (int i = 0; i < backupOperationTDM.getRecordCount(); i++) {
			OperationsTDM.setAt(OperationsTDM.addNew(), backupOperationTDM
					.getAt(i).clone());
		}
	}

	private void shuffleTDM(String startsWith) {
		for (int i = 0; i < OperationsTDM.getRecordCount(); i++) {
			String title = OperationsTDM.getAt(i).getLocalizedStringValue(
					"Title");
			if (!title.toLowerCase().contains(startsWith.toLowerCase()))
				OperationsTDM.removeAt(i--);
		}
		if (OperationsTDM.getRecordCount() == 0)
			OperationsTDM.setAt(OperationsTDM.addNew(), backupOperationTDM
					.getAt(0).clone());
		cmbOperations.setSelectedIndex(0);
	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getOperationsList_ActionStart(UifCancellableActionEvent event) {
		url = "jdbc:oracle:thin:@" + txtServer.getText() + ":"
				+ txtPort.getText() + ":" + txtInstance.getText();
		this.inputDM.setLocalizedStringValue("URL", url);

	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getOperationsList_ActionDone(UifActionEvent event) {
		cmbOperations.setSelectedIndex(0);
		backUpTDM();
		btnAnalyse.setEnabled(true);
	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getOperationsDetail_ActionDone(UifActionEvent event) {
		try {
			rootNode.clearAllFields();

			propertyTemp = (UifTabularDataModel) rootNode
					.getValue("propertyTDM");

			int index = propertyTemp.addNew();
			propertyTemp.setValueAt(index, "name", "Inputs");
			propertyTemp.setValueAt(index, "propertyTDM", createInputs());

			index = propertyTemp.addNew();
			propertyTemp.setValueAt(index, "name", "Outputs");
			propertyTemp.setValueAt(index, "propertyTDM", createOutputs());

			index = propertyTemp.addNew();
			propertyTemp.setValueAt(index, "name", "Bindings");
			propertyTemp.setValueAt(index, "propertyTDM", createBindings());
		} catch (Exception e) {
			e.printStackTrace();
			getWorkspace().alert(e.getLocalizedMessage());
		}

	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getItemDetails_ActionStart(UifCancellableActionEvent event) {
		final String[] skipTypes = { "boolean", "java.lang.String", "int",
				"long", "double", "byte", "char", "short", "float",
				"java.util.Date", "java.lang.Class" };
		List<String> skipTypeList = Arrays.asList(skipTypes);
		String item = event.getSource().toString();
		nodeBoundDM = ((UifTreeNode) event.getSource()).getNodeDM();
		if (item.contains("Binding -> ")) {
			int j = MapBindingTDM.addNew();
			MapBindingTDM.setValueAt(j, "parent_fld_val", "Binding");
			MapBindingTDM.setValueAt(j, "child_fld_val", bindings.get(item.replace("Binding -> ", "")));

			j = MapBindingTDM.addNew();
			MapBindingTDM.setValueAt(j, "parent_fld_val", "Resource");
			UifTabularDataModel propertyOfServiceNode = (UifTabularDataModel) ((UifTreeNode)((UifTreeNode)event.getSource()).getParent()).getNodeDM().getValue("propertyTDM");
			int sizeOfTDM = propertyOfServiceNode.getRecordCount();
			for(int i=0;i<sizeOfTDM;i++){
				String name = propertyOfServiceNode.getStringValueAt(i, "name");
				if(name.startsWith("ResourceName -> ")){
					MapBindingTDM.setValueAt(j, "child_fld_val", name.replace("ResourceName -> ",""));
				}
			}
		} else if (item.contains("Type -> ")) {
			String clazzName = item.replace("Type -> ", "");
			if (clazzName.endsWith("[]")) {
				clazzName = clazzName.substring(0, clazzName.length() - 2);
			}
			if (skipTypeList.contains(clazzName)) {
				event.setCancel(true);
				return;
			}
		} else {
			event.setCancel(true);
			return;
		}
		inputDM.setLocalizedStringValue("item", item);

	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getItemDetails_ActionDone(UifActionEvent event) {
		nodeBoundDM = ((UifTreeNode) event.getSource()).getNodeDM();
		if (nodeBoundDM.getLocalizedStringValue("name").startsWith("Type ->")) {
			handleDatatypeReturn();
		}else if (nodeBoundDM.getLocalizedStringValue("name").startsWith("Binding ->")) {
			handleResourceReturn();
		}
//		expandAllRecurser();
	}

	private void handleResourceReturn() {
		UifTabularDataModel childNodeTDM = (UifTabularDataModel) this
				.getDataset().createDataModel("childNodeTDM_Bindings",
						UifDataset.DataModelType.TABULARDATAMODEL, true);

		UifDataModel resourceDetails = Classdetails.getAt(0);
		int props = childNodeTDM.addNew();
		String jndi = resourceDetails.getStringValue("ParamType");
		String homeClassName= "", remoteClassName= "", local= "", methodName = "";
		String serviceName = (((UifTabularDataModel)nodeBoundDM.getParent()).getStringValueAt(1,"name")).replace("Service Name -> ","");
		UifTabularDataModel bindingsFromOperation = (UifTabularDataModel) OpDetials.getValue("Bindings");
		for(int i = 0; i<bindingsFromOperation.getRecordCount();i++){
			UifDataModel serviceFromBinding = (UifDataModel) bindingsFromOperation.getValueAt(i, "Service");
			if(serviceFromBinding.getLocalizedStringValue("Name").equals(serviceName)){
				jndi=jndi.isEmpty()?serviceFromBinding.getLocalizedStringValue("JndiHomeLookupName"):jndi;
				remoteClassName=serviceFromBinding.getLocalizedStringValue("RemoteClassName");
				homeClassName=serviceFromBinding.getLocalizedStringValue("HomeClassName");
				local=serviceFromBinding.getLocalizedStringValue("Local");
				methodName = bindingsFromOperation.getStringValueAt(i, "MethodName");
				
			}
		}
		
		childNodeTDM.setValueAt(props, "name",
				"JNDI -> " + jndi);

		props = childNodeTDM.addNew();
		childNodeTDM.setValueAt(props, "name",
				"Method -> " + methodName);

		props = childNodeTDM.addNew();
		childNodeTDM.setValueAt(props, "name",
				"Remote Class -> " + remoteClassName);

		props = childNodeTDM.addNew();
		childNodeTDM.setValueAt(props, "name",
				"Home Class -> " + homeClassName);

		props = childNodeTDM.addNew();
		childNodeTDM.setValueAt(props, "name",
				"Local EJB -> " + local);

		nodeBoundDM.setValue("propertyTDM", childNodeTDM);
	}

	private void handleDatatypeReturn() {
		UifTabularDataModel childNodeTDM = (UifTabularDataModel) this
				.getDataset().createDataModel("childNodeTDM_Bindings",
						UifDataset.DataModelType.TABULARDATAMODEL, true);
		int count = Classdetails.getRecordCount();
		for (int i = 0; i < count; i++) {
			UifDataModel dataType = Classdetails.getAt(i);

			int props = childNodeTDM.addNew();
			UifTabularDataModel grandChildNodeTDM = (UifTabularDataModel) this
					.getDataset()
					.createDataModel("grandChildNodeTDM_Bindings",
							UifDataset.DataModelType.TABULARDATAMODEL, true);

			childNodeTDM.setValueAt(props, "name",
					dataType.getLocalizedStringValue("ParamName"));
			childNodeTDM
					.setValueAt(props, "propertyTDM", grandChildNodeTDM);

			props = grandChildNodeTDM.addNew();
			grandChildNodeTDM.setValueAt(props, "name", "Type -> "
					+ dataType.getLocalizedStringValue("ParamType"));

		}
		nodeBoundDM.setValue("propertyTDM", childNodeTDM);
	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getOperationsDetail_ActionStart(UifCancellableActionEvent event) {
		System.out.println("");
	}

	/**
	 * @param event The event object.
	 */
	public void btnExpandAll_Action(UifCancellableEvent event) {
//		isExpandAllClicked=true;
		expandAllRecurser();
	}

	private void expandAllRecurser() {
		try {
			Field treeField = TreeNative.class.getDeclaredField("tree");
			treeField.setAccessible(true);
			JTree tree = (JTree) treeField.get(treeAifOperation);
				expandAll(tree);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private void expandAll(){
//		
//	}
	
	public void expandAll(JTree tree) {
//		ArrayList<UifTreeNode> nodes = new ArrayList<UifTreeNode>();
		try {

			int count = tree.getRowCount();
			for (int i = 0; i < count; ++i) {
				if (!tree.isExpanded(i)) {
					TreePath path = tree.getPathForRow(i);
					UifTreeNode node = (UifTreeNode) path.getLastPathComponent();
//					nodes.add(node);
					System.out.println("==>" + node);
					if(node.toString().startsWith("Binding") || node.toString().startsWith("Type ->")){
						ActionInfra ae = ((ActionInfra)((TreeNodeInfra)node).getActivationAction());
						ae.execute(node, null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

//		return ((UifTreeNode[]) nodes.toArray(new UifTreeNode[nodes.size()]));
	}

	/**
	 * @param event The event object.
	 */
	public void treeAifOperation_PostNodeExpand(UifPostTreeExpansionEvent event) {
//		if(isExpandAllClicked) expandAllRecurser();
	}	
	
}
