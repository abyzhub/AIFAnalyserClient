/**
 * 
 */
package com.amdocs.bss.tool.aifAnalyser;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreePath;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.amdocs.uif.action.ActionInfra;
import com.amdocs.uif.action.UifActionEvent;
import com.amdocs.uif.action.UifCancellableActionEvent;
import com.amdocs.uif.action.UifSubmitAction;
import com.amdocs.uif.client.connection.ConnectionVo;
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
import com.amdocs.uif.widgets.tree.UifPostTreeExpansionEvent;
import com.amdocs.uif.widgets.tree.UifTreeNode;
import com.amdocs.uif.workspace.ServerInfoInfra;
import com.amdocs.uif.workspace.UifFileChooserInfo;
import com.amdocs.uif.workspace.UifFileDescriptor;
import com.amdocs.uif.workspace.UifForm;
import com.amdocs.uif.workspace.UifFormEvent;

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

	protected UifDataModel ResourceDetail;

	protected UifTabularDataModel Classdetails;

	protected UifTabularDataModel backends;

	protected UifTabularDataModel MapBindingTDM;

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

	protected UifButton btnExport;

	protected UifLabel lblMessage;

	private Map<String, Integer> bindings = new HashMap<String, Integer>();

	private String url;
	

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
			if ("AppFrameworkBinding".equals(bindingName.substring(bindingName
					.lastIndexOf(".") + 1))) {
				grandChildNodeTDM.setValueAt(props, "name", "ClassName -> "
						+ binding.getLocalizedStringValue("ClassName"));
			} else {
				grandChildNodeTDM.setValueAt(props, "name", "ResourceName -> "
						+ binding.getLocalizedStringValue("ResourceName"));
			}
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
		backupOperationTDM.removeAllDataModels();
		txtQuickSearch.setText("");
		cmbOperations.setSelectedIndex(0);
		backUpTDM();
		getWorkspace().alert("Data Loaded!");
		btnAnalyse.setEnabled(true);
	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getOperationsDetail_ActionDone(UifActionEvent event) {
		try {
			ServerInfoInfra info=(ServerInfoInfra) getWorkspace().getBackendInfo("crm").getServerInfo();
			Field fld = ServerInfoInfra.class.getDeclaredField("m_ConnectionVo");
			fld.setAccessible(true);
			ConnectionVo connectionVo=(ConnectionVo) fld.get(info);
			
			lblMessage.setText("Following details are fetched from the Backend : " + connectionVo.getURL());
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
		btnExpandAll.setEnabled(true);
		btnExport.setEnabled(true);
	}

	/**
	 * @param event
	 *            The event object.
	 */
	public void getItemDetails_ActionStart(UifCancellableActionEvent event) {
		final String[] skipTypes = { "boolean", "java.lang.String", "int",
				"long", "double", "byte", "char", "short", "float",
				"java.util.Date", "java.lang.Class" };
		MapBindingTDM.removeAllDataModels();
		List<String> skipTypeList = Arrays.asList(skipTypes);
		String item = event.getSource().toString();
		nodeBoundDM = ((UifTreeNode) event.getSource()).getNodeDM();
		if (item.contains("Binding -> Ejb") || item.contains("Binding -> WebService")) {
			int j = MapBindingTDM.addNew();
			MapBindingTDM.setValueAt(j, "ParentFldVal", "Binding");
			MapBindingTDM.setValueAt(j, "ChildFldVal", bindings.get(item.replace("Binding -> ", "")));

			j = MapBindingTDM.addNew();
			MapBindingTDM.setValueAt(j, "ParentFldVal", "Resource");
			UifTabularDataModel propertyOfServiceNode = (UifTabularDataModel) ((UifTreeNode)((UifTreeNode)event.getSource()).getParent()).getNodeDM().getValue("propertyTDM");
			int sizeOfTDM = propertyOfServiceNode.getRecordCount();
			for(int i=0;i<sizeOfTDM;i++){
				String name = propertyOfServiceNode.getStringValueAt(i, "name");
				if(name.startsWith("ResourceName -> ")){
					MapBindingTDM.setValueAt(j, "ChildFldVal", name.replace("ResourceName -> ",""));
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
			handleResourceReturn(nodeBoundDM.getLocalizedStringValue("name"));
		}
//		expandAllRecurser();
	}

	private void handleResourceReturn(String bindingName) {
		UifTabularDataModel childNodeTDM = (UifTabularDataModel) this
				.getDataset().createDataModel("childNodeTDM_Bindings",
						UifDataset.DataModelType.TABULARDATAMODEL, true);
		if (bindingName.contains("Ejb")) {
			UifDataModel resourceDetails = Classdetails.getAt(0);
			int props = childNodeTDM.addNew();
			String jndi = resourceDetails.getStringValue("ParamType");
			String homeClassName = "", remoteClassName = "", local = "", methodName = "", url = "";
			url = Classdetails.getAt(5).getStringValue("ParamType");
			String serviceName = (((UifTabularDataModel) nodeBoundDM
					.getParent()).getStringValueAt(1, "name")).replace(
					"Service Name -> ", "");
			UifTabularDataModel bindingsFromOperation = (UifTabularDataModel) OpDetials
					.getValue("Bindings");
			for (int i = 0; i < bindingsFromOperation.getRecordCount(); i++) {
				UifDataModel serviceFromBinding = (UifDataModel) bindingsFromOperation
						.getValueAt(i, "Service");
				if (serviceFromBinding.getLocalizedStringValue("Name").equals(
						serviceName)) {
					jndi = jndi.isEmpty() ? serviceFromBinding
							.getLocalizedStringValue("JndiHomeLookupName")
							: jndi;
					remoteClassName = serviceFromBinding
							.getLocalizedStringValue("RemoteClassName");
					homeClassName = serviceFromBinding
							.getLocalizedStringValue("HomeClassName");
					local = serviceFromBinding.getLocalizedStringValue("Local");
					methodName = bindingsFromOperation.getStringValueAt(i,
							"MethodName");
				}
			}

			childNodeTDM.setValueAt(props, "name", "JNDI -> " + jndi);

			props = childNodeTDM.addNew();
			childNodeTDM.setValueAt(props, "name", "Method -> " + methodName);

			props = childNodeTDM.addNew();
			childNodeTDM.setValueAt(props, "name", "Remote Class -> "
					+ remoteClassName);

			props = childNodeTDM.addNew();
			childNodeTDM.setValueAt(props, "name", "Home Class -> "
					+ homeClassName);

			props = childNodeTDM.addNew();
			childNodeTDM.setValueAt(props, "name", "Local EJB -> " + local);

			props = childNodeTDM.addNew();
			childNodeTDM.setValueAt(props, "name", "URL -> " + url);
		} else if (bindingName.contains("WebService")) {
			UifDataModel resourceDetails = Classdetails.getAt(0);
			int props = childNodeTDM.addNew();
			String wsdl = resourceDetails.getStringValue("ParamType");
			childNodeTDM.setValueAt(props, "name", "WSDL -> " + wsdl);

		}
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
		inputDM.setStringValue("Operation", cmbOperations.getSelectedText());
		System.out.println("");
	}

	/**
	 * @param event The event object.
	 */
	public void btnExpandAll_Action(UifCancellableEvent event) {
		expandAll(getTreeObject());
	}

	private JTree getTreeObject() {
		JTree tree = null;
		try {
			Field treeField = TreeNative.class.getDeclaredField("tree");
			treeField.setAccessible(true);
			tree = (JTree) treeField.get(treeAifOperation);
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
		return tree;
	}

	private UifTreeNode getTreeNodeByName(JTree tree, String nameOfNode){
		TreeUI treeUI = tree.getUI();
		for(int i=0 ;i<tree.getRowCount();i++){
			TreePath localTreePath = treeUI.getPathForRow(tree, i);
			UifTreeNode node = (UifTreeNode)localTreePath.getLastPathComponent();
			if(node.toString().equals(nameOfNode)){
				return node;
			}
		}
		return null;
	}
	
	public void expandAll(JTree tree) {
		int count = tree.getRowCount();
		for (int i = 0; i < count; ++i) {
			if (!tree.isExpanded(i)) {
				TreePath path = tree.getPathForRow(i);
				UifTreeNode node = (UifTreeNode) path.getLastPathComponent();
				if (node.toString().startsWith("Binding -> EjbBinding")
						|| node.toString().startsWith("Type ->")) {
					ActionInfra ae = ((ActionInfra) ((TreeNodeInfra) node)
							.getActivationAction());
					ae.execute(node, null);
				}
			}
		}
	}

	/**
	 * @param event The event object.
	 */
	public void treeAifOperation_PostNodeExpand(UifPostTreeExpansionEvent event) {
//		if(isExpandAllClicked) expandAllRecurser();
	}

	/**
	 * @param event The event object.
	 */
	public void btnExport_Action(UifCancellableEvent event) {

		UifFileChooserInfo fileChooserInfo = new UifFileChooserInfo();
		fileChooserInfo.setMultiSelectionEnabled(false);
		fileChooserInfo.setAllFilesAcceptable(true);
		String[] filterString = { "Microsoft Excel(*.xls)" };
		fileChooserInfo.setFilterStrings(filterString);
		fileChooserInfo.setDialogTitle("Export As");
		UifFileDescriptor[] fileDscrptr = getWorkspace().openFileChooser(
				fileChooserInfo);
		if (fileDscrptr.length != 1)
			return;

		try {
			FileOutputStream fileOut = new FileOutputStream(
					fileDscrptr[0].getAbsolutePath()+".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet apiDetailsSheet = workbook.createSheet("API Details");
			HSSFCellStyle cellStyleHeader = workbook.createCellStyle();
			cellStyleHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont boldFont = workbook.createFont();
			boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			cellStyleHeader.setFont(boldFont);
			HSSFCellStyle cellStyleBold = workbook.createCellStyle();
			cellStyleBold.setFont(boldFont);

			HSSFRow row1 = apiDetailsSheet.createRow(0);
			HSSFCell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Operation Name");
			cellA1.setCellStyle(cellStyleBold);
			HSSFCell cellB1 = row1.createCell(1);
			cellB1.setCellValue(inputDM.getStringValue("Operation"));

			apiDetailsSheet.createRow(1);
			HSSFRow row3 = apiDetailsSheet.createRow(2);
			HSSFCell cellA2 = row3.createCell(0);
			cellA2.setCellValue("Attribute");
			cellA2.setCellStyle(cellStyleHeader);
			
			HSSFCell cellB2 = row3.createCell(1);
			cellB2.setCellValue("Parameter");
			cellB2.setCellStyle(cellStyleHeader);
			
			HSSFCell cellC2 = row3.createCell(2);
			cellC2.setCellValue("Type");
			cellC2.setCellStyle(cellStyleHeader);
			
			HSSFCell cellD2 = row3.createCell(3);
			cellD2.setCellValue("Notes");
			cellD2.setCellStyle(cellStyleHeader);
			
			int rowCursor = 4;
			rowCursor += createInputs(apiDetailsSheet, rowCursor, cellStyleBold);
			rowCursor += createOutputs(apiDetailsSheet, rowCursor, cellStyleBold);
			apiDetailsSheet.createRow(rowCursor++);
			rowCursor += createBindings(apiDetailsSheet, rowCursor, cellStyleBold);
			
			apiDetailsSheet.addMergedRegion(new CellRangeAddress(0,0,1,2));
			apiDetailsSheet.setColumnWidth(0, 17*256);
			apiDetailsSheet.setColumnWidth(1, 30*256);
			apiDetailsSheet.setColumnWidth(2, 80*256);
			apiDetailsSheet.setColumnWidth(3, 50*256);
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int createInputs(HSSFSheet apiDetailsSheet, int startingFromRow, HSSFCellStyle cellStyleBold) {
		HSSFRow rowHeader = apiDetailsSheet.createRow(startingFromRow-1);
		HSSFCell cellInput = rowHeader.createCell(0);
		cellInput.setCellValue("Inputs");
		cellInput.setCellStyle(cellStyleBold);
		
		UifTreeNode inputNode = getTreeNodeByName(getTreeObject(), "Inputs");
		UifTreeNode[] inputNodesArray = inputNode.getChildren();
		for(int i=0; i<inputNodesArray.length;i++){
			HSSFCell cellInputParam = rowHeader.createCell(1);
			cellInputParam.setCellValue(inputNodesArray[i].getNodeDM().getStringValue("name"));	

			HSSFCell cellInputType = rowHeader.createCell(2);
			String currentType=((UifTabularDataModel)inputNodesArray[i].getNodeDM().getValue("propertyTDM")).getStringValueAt(0, "name").replace("Type -> ", ""); 
			cellInputType.setCellValue(currentType);
			
			UifTabularDataModel innerDataTypes = (UifTabularDataModel)(((UifTabularDataModel)inputNodesArray[i].getNodeDM().getValue("propertyTDM")).getValueAt(0, "propertyTDM"));
			if(innerDataTypes!=null && innerDataTypes.getRecordCount()>0){
				HSSFSheet sheet = createNewSheetAndExpandDatatype(apiDetailsSheet,innerDataTypes, currentType);
				HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
				link.setAddress("'"+sheet.getSheetName()+"'!A1");
				
				HSSFFont blueFont = apiDetailsSheet.getWorkbook().createFont();
				blueFont.setColor(IndexedColors.BLUE.index);
				blueFont.setUnderline((byte) 1);
				HSSFCellStyle cellStyleHyperlink = apiDetailsSheet.getWorkbook().createCellStyle();
				cellStyleHyperlink.setFont(blueFont);
				
				cellInputType.setHyperlink(link);
				cellInputType.setCellStyle(cellStyleHyperlink);
			}
			rowHeader = apiDetailsSheet.createRow(startingFromRow + i);
		}
		return inputNodesArray.length;
	}	
	
	private HSSFSheet createNewSheetAndExpandDatatype(HSSFSheet previousSheet,
			UifTabularDataModel innerDataTypes, String currentType) {
		HSSFWorkbook workbook = previousSheet.getWorkbook();
		String[] tokens = currentType.split("\\.");
		String name = tokens[tokens.length-1];
		int sheets = workbook.getNumberOfSheets();
		for(int i=0;i<sheets;i++){
			if(workbook.getSheetAt(i).getSheetName().equals(name)) return workbook.getSheetAt(i); 
		}
		
		HSSFSheet innerDatatypeSheet=null;
		try {
			innerDatatypeSheet= workbook.createSheet(name);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		innerDatatypeSheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
		innerDatatypeSheet.setColumnWidth(0, 17*256);
		innerDatatypeSheet.setColumnWidth(1, 30*256);
		innerDatatypeSheet.setColumnWidth(2, 80*256);
		HSSFCellStyle cellStyleHeader = workbook.createCellStyle();
		cellStyleHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont boldFont = workbook.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyleHeader.setFont(boldFont);
		
		HSSFRow rowHeader = innerDatatypeSheet.createRow(0);
		HSSFCell head = rowHeader.createCell(0);
		head.setCellStyle(cellStyleHeader);
		head.setCellValue(currentType);
		
		HSSFFont blueFont = workbook.createFont();
		blueFont.setColor(IndexedColors.BLUE.index);
		blueFont.setUnderline((byte) 1);
		HSSFCellStyle cellStyleHyperlink = workbook.createCellStyle();
		cellStyleHyperlink.setFont(blueFont);

		
		for(int i =0;i<innerDataTypes.getRecordCount();i++){
			rowHeader = innerDatatypeSheet.createRow(i+1);
			HSSFCell cellInputParam = rowHeader.createCell(1);
			cellInputParam.setCellValue(innerDataTypes.getStringValueAt(i, "name"));	

			HSSFCell cellInputType = rowHeader.createCell(2);
			UifTabularDataModel innerPropertyTDM = (UifTabularDataModel)innerDataTypes.getValueAt(i, "propertyTDM");
			UifTabularDataModel furtherInnerDataType = (UifTabularDataModel)innerPropertyTDM.getValueAt(0, "propertyTDM");
			cellInputType.setCellValue(innerPropertyTDM.getStringValueAt(0, "name").replace("Type -> ", ""));

		
			if(furtherInnerDataType!=null && furtherInnerDataType.getRecordCount()>0){
				HSSFSheet sheet = createNewSheetAndExpandDatatype(previousSheet, furtherInnerDataType, innerDataTypes.getStringValueAt(i, "name"));
				HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
				link.setAddress("'"+sheet.getSheetName()+"'!A1");
				cellInputType.setHyperlink(link);
				cellInputType.setCellStyle(cellStyleHyperlink);
			}
		}
		rowHeader = innerDatatypeSheet.createRow(innerDataTypes.getRecordCount()+1);
		HSSFCell cellBack = rowHeader.createCell(1);
		cellBack.setCellValue("<== BACK");	
		cellBack.setCellStyle(cellStyleHyperlink);
		HSSFHyperlink linkBack = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
		linkBack.setAddress("'"+previousSheet.getSheetName()+"'!A1");
		cellBack.setHyperlink(linkBack);
		
		return innerDatatypeSheet;
	}

	private int createOutputs(HSSFSheet apiDetailsSheet, int startingFromRow, HSSFCellStyle cellStyleBold) {
		HSSFRow rowHeader = apiDetailsSheet.createRow(startingFromRow-1);
		HSSFCell cellOutput = rowHeader.createCell(0);
		cellOutput.setCellValue("Outputs");
		cellOutput.setCellStyle(cellStyleBold);
		
		UifTreeNode outputNode = getTreeNodeByName(getTreeObject(), "Outputs");
		UifTreeNode[] outputNodesArray = outputNode.getChildren();
		for(int i=0; i<outputNodesArray.length;i++){
			HSSFCell cellOutputParam = rowHeader.createCell(1);
			cellOutputParam.setCellValue(outputNodesArray[i].getNodeDM().getStringValue("name"));	

			HSSFCell cellOutputType = rowHeader.createCell(2);
			String currentType=((UifTabularDataModel)outputNodesArray[i].getNodeDM().getValue("propertyTDM")).getStringValueAt(0, "name").replace("Type -> ", ""); 
			cellOutputType.setCellValue(currentType);

			UifTabularDataModel innerDataTypes = (UifTabularDataModel)(((UifTabularDataModel)outputNodesArray[i].getNodeDM().getValue("propertyTDM")).getValueAt(0, "propertyTDM"));
			if(innerDataTypes!=null && innerDataTypes.getRecordCount()>0){
				HSSFSheet sheet = createNewSheetAndExpandDatatype(apiDetailsSheet,innerDataTypes, currentType);
				HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
				link.setAddress("'"+sheet.getSheetName()+"'!A1");
				
				HSSFFont blueFont = apiDetailsSheet.getWorkbook().createFont();
				blueFont.setColor(IndexedColors.BLUE.index);
				blueFont.setUnderline((byte) 1);
				HSSFCellStyle cellStyleHyperlink = apiDetailsSheet.getWorkbook().createCellStyle();
				cellStyleHyperlink.setFont(blueFont);
				
				cellOutputType.setHyperlink(link);
				cellOutputType.setCellStyle(cellStyleHyperlink);
			}
			
			
			rowHeader = apiDetailsSheet.createRow(startingFromRow + i);
		}

		return outputNodesArray.length;
	}	

	private int createBindings(HSSFSheet apiDetailsSheet, int startingFromRow, HSSFCellStyle cellStyleBold) {
		HSSFRow rowHeader = apiDetailsSheet.createRow(startingFromRow-1);
		HSSFCell cellBindings = rowHeader.createCell(0);
		cellBindings.setCellValue("EJB Bindings");
		cellBindings.setCellStyle(cellStyleBold);
		
		UifTreeNode bindingsNode = getTreeNodeByName(getTreeObject(), "Bindings");
		UifTreeNode[] bindingsNodesArray = bindingsNode.getChildren();
		for(int i=0; i<bindingsNodesArray.length;i++){
			UifTabularDataModel properties=(UifTabularDataModel) ((UifTabularDataModel)bindingsNodesArray[i].getNodeDM().getValue("propertyTDM")).getValueAt(0, "propertyTDM");
			if(properties == null || properties.getStringValueAt(4, "name").equals("Local EJB -> true")) continue;

			HSSFCell cellBindingsParam = rowHeader.createCell(1);
			cellBindingsParam.setCellValue(bindingsNodesArray[i].getNodeDM().getStringValue("name"));	

			rowHeader = apiDetailsSheet.createRow(startingFromRow++);
			HSSFCell cellBindingsType = rowHeader.createCell(1);
			cellBindingsType.setCellValue("JNDI");
			HSSFCell cellBindingsVal = rowHeader.createCell(2);
			cellBindingsVal.setCellValue(properties.getStringValueAt(0, "name").replace("JNDI -> ", ""));

			rowHeader = apiDetailsSheet.createRow(startingFromRow++);
			cellBindingsType = rowHeader.createCell(1);
			cellBindingsType.setCellValue("EJB Method");
			cellBindingsVal = rowHeader.createCell(2);
			cellBindingsVal.setCellValue(properties.getStringValueAt(1, "name").replace("Method -> ", ""));

			rowHeader = apiDetailsSheet.createRow(startingFromRow++);
			cellBindingsType = rowHeader.createCell(1);
			cellBindingsType.setCellValue("Remote Class");
			cellBindingsVal = rowHeader.createCell(2);
			cellBindingsVal.setCellValue(properties.getStringValueAt(2, "name").replace("Remote Class -> ", ""));

			rowHeader = apiDetailsSheet.createRow(startingFromRow++);
			cellBindingsType = rowHeader.createCell(1);
			cellBindingsType.setCellValue("Home Class");
			cellBindingsVal = rowHeader.createCell(2);
			cellBindingsVal.setCellValue(properties.getStringValueAt(3, "name").replace("Home Class -> ", ""));
		}
		return bindingsNodesArray.length;
	}


}
