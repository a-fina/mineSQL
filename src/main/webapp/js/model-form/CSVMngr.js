// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * @class CSVMngr
 *
 * Update database from CSV manager
 *
 * @author    Finamore Alessio
 * @date      29 / 04 / 10
 * @version   0.1
 *
 * @license licensed under the terms of
 * the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
 * that the code/component(s) do NOT become part of another Open Source or 
 * Commercially licensed development library or toolkit 
 * without explicit permission.
 * 
 * <p>License details: <a href="http://www.gnu.org/licenses/lgpl.html"
 * target="_blank">http://www.gnu.org/licenses/lgpl.html</a></p>
 */
CSVMngr = {}; 
/**
 * @method update
 * @param {Object} config.ajaxParams Parameters to submit
 * @return {WindowFormTable} An Ext.WindowFormTable to save a filter
 */
CSVMngr.update= function(config) {
	// pre-instantiation code
	var defaults = {
               items: new Ext.form.FieldSet({
		            title: "Esito della validazione file CSV",
                    autoHeight: true,
		            defaultType: 'textfield',
                //    autoWidth: true,
                //    layout:"anchor",
                //    anchor:"100%",
                    border:false,
                    defaults: {width: 350,height:100},
                    items: [{
                            xtype: 'hidden',
                            id: 'COMMIT_CSV'
                        },{
                            xtype:'textarea',
                            fieldLabel: 'File CSV:',
                            id: 'reason',
                            name: 'reason',
                            disabled: false,
                            readOnly: true
                    }]
                }),
                /*submitParams: {
                        "table":"undefined",
                        "id":
                }, me lo passa il chiamante */
                reader: new Ext.data.JsonReader({
                                    root: 'row',
                                    totalProperty: 'totalCount',
                                    fields: [{name: 'success'},
                                             {name: 'valid'},
                                             {name: 'reason'}]
                }),
                title: "Aggiornamento database", 
		        tableName: "CP_LISTA_COMPONENTI_T",
                crud: "update",
                width: 550,
                height: 250,
                modal: true,
                collapsible: false,
                resizable: true,
                url: "autogrid/submit-csv.jsp",
		        showMessage: false,
                onAfterSubmit: function(result){
                    //TODO: refresh griglia
                    var idWin = "#importCSVwindow .x-window-body";
                    var win =  new Ext.Window({
                        id: 'importCSVwindow',
                        closable: true,
                        resizable: false,
                        layout:'auto',
                        autoScroll:true,
                        width:450,
                        height:300,
                        //closeAction:'hide',
                        plain: true,
                        html: result.reason,
                        /**tbar: new GridToolBar({
                                idflusso:'ID_FLUSSO_COMPENDING', 
                                idToolButton: 'ONLY_SAVE_FILE',
                                idTextContainer: idWin
                            }) , ****/
                            buttons: [{
                                text: 'Close',
                                handler: function(){
                                win.hide();
                                }
                            }]
                    });
				    win.show(this);
		            reloadGrid(config.dataStore);
                },
                onBeforeSubmit: function(){
			        alert(" MARK_CSVMngr.js timeout:" + Ext.Ajax.timeout );
                }
	}; // eo defaults object
 
	// create config object
	var cfg = Ext.apply({}, config, defaults);
 
	// instantiate
    var csvWin= new WindowFormTable(cfg);
 
	// post-instantiation code
	csvWin.show(this);
 
	// return the created component
	return csvWin;
 
} // eo function 
 
function reloadGrid(dataStore) {
    console.log("CSVMngr.js");
    console.dir(dataStore);
	dataStore.reload({ params: { start:0 } });
}
// eof
