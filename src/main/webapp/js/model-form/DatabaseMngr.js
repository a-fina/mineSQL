// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * @class DatabaseMngr
 *
 * A WindowsFormTable Factory for CRUD operation on Filter menu
 *
 * @author    Finamore Alessio
 * @copyright (c) 2009, by Assioma.net
 * @date      30 / 04 / 09
 * @version   0.1
 * @revision  $Id: DatabaseMngr.js,v 1.4 2009/08/03 15:30:54 assioma Exp $
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
DatabaseMngr = {}; 
/**
 * @method saveWin
 * @param {Object} config.ajaxParams Parameters to submit
 * @return {WindowFormTable} An Ext.WindowFormTable to save a filter
 */
DatabaseMngr.saveWin = function(config) {
	// pre-instantiation code
     var values = new Ext.data.SimpleStore({
          fields: ['DESC', 'TYPE'],
          data : [['DB2','DB2'],
                  ['H2','H2'],
                  ['MySQL','MySQL'],
                  ['Postgresql','Postgresql'],
                  ['Oracle','Oracle'],
                  ['AEM','AEM']
              ]
      });
      
	var defaults = {
               items: new Ext.form.FieldSet({
                    title: 'Database Connection',
                    autoHeight: true,
                    defaultType: 'textfield',
                    defaults: {width: 310},
                    items: [{
                            fieldLabel: 'Name',
                            name: 'NAME'
                        }, {
                            fieldLabel: 'Type',
                            name: 'TYPE',
                            xtype: 'combo',
                            mode: 'local',
                            store: values,
                            displayField:'TYPE',
                        }, {
                            fieldLabel: 'Host',
                            name: 'HOST'
                        }, {
                            fieldLabel: 'Database',
                            name: 'SOURCE'
                        }, {
                            fieldLabel: 'User',
                            name: 'USER'
                        }, {
                            fieldLabel: 'URL',
                            name: 'URL'
                        }, {
                            fieldLabel: 'Password',
                            name: 'PASSWORD'
                        }, {
                            fieldLabel: 'Show all',
                            name: 'SHOW_ALL_DATABASE'
                        }]
                }),
                submitParams: {
                        'idQuery':'undefined',
                        'context':'undefined',
                        'hidden_columns': 'undefined',
                        'entityName': 'Database'
                },
                title: 'New DB Connection', 
                tableName: 'Database',
                crud: "create",
                modal: true,
                url: "autogrid/auto-table.jsp",
                onAfterSubmit: function(){
                    // Ricarico il menu dei databases
                    saveFilterMenu.removeAll();
                    delete(saveFilterMenu._alreadyLoaded);
                }
	}; // eo defaults object
 
	var cfg = Ext.apply({}, config, defaults);
    var saveFilterWin = new WindowFormTable(cfg);
	return saveFilterWin;
} // eo function 
 
/**
 * @method deleteWin
 * @param {Object} config.ajaxParams Parameters to submit
 * @return {WindowFormTable} An Ext.WindowFormTable to delete a filter
 */
DatabaseMngr.deleteWin = function(config) {
	var defaults = {
               items: new Ext.form.FieldSet({
                    title: 'Informationi Filtro',
                    autoHeight: true,
                    defaultType: 'textfield',
                    defaults: {width: 310},
                    items: [{
                            xtype: 'hidden',
                            id: 'ID'
                        },{
                            xtype: 'hidden',
                            id: 'IDFLUSSO'
                        },{
                            fieldLabel: 'Nome',
                            id: 'NOME',
                            name: 'NOME',
                            disabled: true
                        }, {
                            fieldLabel: 'Note',
                            id: 'NOTE',
                            name: 'NOTE',
                            disabled: true
                        }]
                }),
                reader: new Ext.data.JsonReader({
                            root: 'row',
                            totalProperty: 'totalCount',
                            fields: [{name: 'ID'}, 
                                     {name: 'IDFLUSSO'},
                                     {name: 'NOME'},
                                     {name: 'NOTE'}]
                        }),
                submitParams: {
                        "ID":"undefined"
                },
                title: "Cancella Filtro", 
                tableName: "database",
                crud: "delete",
                modal: true,
                url: "/autogrid/auto-table.jsp",
                onAfterSubmit: function(){
                    // Ricarico il menu dei filtri
                    databaseMenu.removeAll();
                    delete(databaseMenu._alreadyLoaded);
                }
	};
 
	var cfg = Ext.apply({}, config, defaults);

    var deleteFilterWin = new WindowFormTable(cfg);

	return deleteFilterWin;
 
} // eo function 

/**
 * @method updateWin
 * @param {Object} config.ajaxParams Parameters to submit
 * @return {WindowFormTable} An Ext.WindowFormTable to update a filter
 */
DatabaseMngr.updateWin = function(config) {
	var defaults = {
               items: new Ext.form.FieldSet({
                    title: 'Informationi Filtro',
                    autoHeight: true,
                    defaultType: 'textfield',
                    defaults: {width: 310},
                    items: [{
                            xtype: 'hidden',
                            id: 'ID'
                        },{
                            xtype: 'hidden',
                            id: 'IDFLUSSO'
                        },{
                            fieldLabel: 'Name',
                            id: 'NOME',
                            name: 'NOME',
                            disabled: false,
                            readOnly: true 
                        }, {
                            fieldLabel: 'Note',
                            id: 'NOTE',
                            name: 'NOTE',
                            disabled: false 
                        }]
                }),
                reader: new Ext.data.JsonReader({
                            root: 'row',
                            totalProperty: 'totalCount',
                            fields: [{name: 'ID'}, 
                                     {name: 'IDFLUSSO'},
                                     {name: 'NOME'},
                                     {name: 'NOTE'}]
                        }),
                submitParams: {  
                        "ID":"undefined", //TODO specificare anche bodyQuery anche nel save ?   
                        "idQuery":"undefined",
                        "context":"undefined",
                        "hidden_columns": "undefined"
                },
                title: "Aggionramento Filtro", 
                tableName: "msq_FILTRI_T",
                crud: "update",
                modal: true,
                url: "/autogrid/auto-table.jsp",
                onAfterSubmit: function(){
                    // Ricarico il menu dei filtri
                    saveFilterMenu.removeAll();
                    delete(saveFilterMenu._alreadyLoaded);
                }
	};
 
	var cfg = Ext.apply({}, config, defaults);

    var updateFilterWin = new WindowFormTable(cfg);

	return updateFilterWin;
 
} // eo function 
// eof
