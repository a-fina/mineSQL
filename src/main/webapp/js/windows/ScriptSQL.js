// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * @class ScriptSQL
 * @config Object: sono i parametri di configurazione passati
 *                 passati a WindowFormTable
 * @return Object
 *
 * Form con dei campi predefiniti caricati lato server che servono 
 * all'esecuizione dello script PL
 *
 * @author    Finamore Alessio
 * @copyright (c) 2010
 * @date      27 / 05 / 10
 * @version   0.1
 * @revision  $Id: 
 *
 * @license licensed under the terms of
 * the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
 * that the code/component(s) do NOT become part of another Open Source or 
 * Commercially licensed development library or toolkit 
 * without explicit permission.
 * 
 * <p>License details: <a href="http://www.gnu.org/licenses/lgpl.html"
 * target="_blank">http://www.gnu.org/licenses/lgpl.html</a></p>
 *
 *
 */
ScriptSQL = function(){

    var ver = "0.9.7";
    var contatoreWindows = 0;
    var queryBodyID = 'queryBody'+contatoreWindows;
    var queryPanelID = 'queryPanel'+contatoreWindows;


    var interactiveSQL = undefined;
   
    // Attributi di classe 
    var _idScript = "";
    //var _myFormPanel = "";
    var _myWindow = "";
    var _myGridPanel= "";
	
	function guidGenerator() {
		var S4 = function() {
			return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
		};
		return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	}



    /**
     * Rchiama la pagina che costruisce lato server tutto il JSON che serve
     * per la costruzione della griglia
     */
    function loadAutoGrid(formPanel, params){
        var url = params.url || "autogrid/auto-json.jsp";
        var div = params.target || "principale";
        var config = {};

        /*_myFormPanel.getForm().submit({
            url: url,
            waitMsg: "Waiting for script execution",
            params: params,
            success:function(form, action) {
                        //Ext.MessageBox.alert('Success', action);
            },
            failure:function(form, action){
                        //Ext.MessageBox.alert('Error', action);
            }                        
        });*/

        /* Scandisco i campi della form e li POSTO insieme ai params */
        var form_fields = formPanel.getForm().getValues();
        // Ciclo acquisizione campi
        for (var field_name in form_fields){
            params[field_name] = form_fields[field_name]; 
        }
        var myGrid = new AutoGrid(/*div,*/ url, params, config); 
		//alert("2 loadAutogrid " + _myGridPanel.getId() + " carico la risposta in questo GridPanel invece che qui: " + div);
        myGrid.getJSONGrid(_myGridPanel);
    }

    /**
     * Istanzia e assembla la form con i parametri dello script
	 * L'handler del bottone Run fa una chiamata ricorsiva a se stesso
     **/
    var loadAutoform = function(loadParam){
                _idScript = loadParam.idScript || _idScript; 
				_showTableName = loadParam.showTableName || "msq_SCRIPT_T";
                _action = loadParam.action || undefined;
				_showDatabaseName = loadParam.showDatabaseName || undefined;
				_hostName = loadParam.hostName || undefined;
				var onBeforeSuccessLoadGrid = loadParam.onBeforeSuccessLoadGrid || undefined;

				//alert(" loadAutoform _idScript: " + _idScript+ " host: "+ _hostName + " tableName: "+ _showTableName  + " DB: " + _showDatabaseName);
                
                //var url= "http://"+HOST+"/autogrid/auto-table.jsp";
                var url= "autogrid/auto-table.jsp";
    
               // alert("MARK_ before ajax request " + url + " ScriptSQL.js");
                Ext.Ajax.request({
                        url: url,
                        success: function(response){ 
                            try{
                            var myFields = eval("("+ response.responseText  +")");
							var formId = "loadAutoform-"+guidGenerator();
							//alert(" _myFormPanel id: loadAutoform-"+id);
                            var myFormPanel = new Ext.FormPanel({ 
                                                   baseCls: 'x-plain',
                                                   id: formId,
                                                 //labelAlign: 'right',
                                                 //frame:true,
                                                    region: 'north',
                                                    autoHeight:'auto',
                                                    layout: 'fit',  
                                                    autoScroll:true,
                                                    labelWidth: 85,
                                                    waitMsgTarget: true,
                                                    defaultType: 'textfield',
                                                    tbar: [/******************************************
                                                        TODO:   bho, si possono creare dei gruppi di Query e qui
                                                                visualizzare tutte le query/report di un determinato gruppo
                                                                o appartenemti alla tabella inziale (tabname)
                                                            { 
                                                            text:'Load',
                                                            menu:  new GenericMenu({ 
                                                                id: "reportMenu", 
                                                                //TODO verificare inel DB corrente l'esistenza della tabella
                                                                // che contiene gli script
                                                                // se non c'é iallora niente menu o menu vuoto
                                                                url:"menus/scripts.jsp", 
																params: {
																		tableName: 'msq_SCRIPT_T',
																		databaseName: 'mineSQL',
																		idScript: _idScript,
                                                                        hostName: "localhost"
																},
                                                                clickHandler: function (menuItem){
                                                                    _myWindow.close();
                                                                    var subStr = menuItem.id.split("-"); 
																	//alert(" idQuery:  " + subStr[subStr.length -1] + " showDatabaseName:" + _showDatabaseName + " host: " + _hostName );
                                                                    loadAutoform({ 
																			idScript: subStr[subStr.length -1], 
																			showDatabaseName: 'mineSQL',//_showDatabaseName,
																			hostName: "localhost",//_hostName,
																			showTableName: 'msq_SCRIPT_T' 
																		});
                                                                    }
                                                                }),
                                                            iconCls: 'report'
                                                        },{
                                                        TODO: non va e non mi ricordo cosa volevo fare    
                                                            text: "Run Query",
                                                            handler: function(){
                                                                Ext.MessageBox.alert("Not implemented TODO"); 
																//runQuery(myFormPanel,'runScript');
																loadAutoGrid(myFormPanel,{  
																		action: 'runQuery',
																		idQuery: _idScript, 
																		tableName: _showTableName,
																		databaseName: _showDatabaseName,
																		hostName: _hostName,
																		target: myFormPanel.getId()
																}); 
                                                            },
                                                            iconCls: 'accept'
                                                        }, **************************************/
                                                        {
                                                            text: "Run Script",
                                                            handler: function(){
																loadAutoGrid(myFormPanel,{  
																		action: 'runScript',
																		idQuery: _idScript, 
																		tableName: _showTableName,
																		databaseName: _showDatabaseName,
																		hostName: _hostName,
																		target: myFormPanel.getId()
																}); 
                                                            },
                                                            iconCls: 'accept'
                                                            /*},{
                                                            text: "Clear",
                                                            handler: function(){$$(queryBodyID).reset()} */
                                                    }],
                                                    items: [myFields.items],
                                                    reader: [myFields.reader]
                            });
                            openNewWindow(myFormPanel);
                            } catch (e){
                            Ext.MessageBox.alert("Errore nel caricamento dei parametri dello script",e); 
                            }
							if ( onBeforeSuccessLoadGrid !== undefined ){
								//runQuery(myFormPanel, 'runQueryBody');
								loadAutoGrid(myFormPanel,{  
										action: _action,
										idQuery: _idScript, 
										tableName: _showTableName,
										databaseName: _showDatabaseName,
										hostName: _hostName,
										target: myFormPanel.getId()
								}); 
							}
                        }, 
                        failure: function(){ 
                            Ext.MessageBox.alert("Errore Ajax", "Errore di  caricamento del fieldSet dall'url: " +  url); 
                        },
                        params: {
                                "action": _action,
		                        "tableName": _showTableName,
		                        "databaseName": _showDatabaseName,
		                        "hostName": _hostName,
		                        "idScript": _idScript,
                                "crudOperation" : "fieldSet" 
                        }
                });
    }

    /*
     * Apre AutoGrid
     **/
    function runQuery(formPanel,action){ 
		var _action = action || 'dummy';
        //var query = $$(queryBodyID).getValue();
        loadAutoGrid(formPanel,{  
                action: _action, 
                idQuery: _idScript, 
                tableName: _showTableName,
				databaseName: _showDatabaseName,
				hostName: _hostName,
                target: formPanel.getId()
        }); 
    }

    /**
     * Istanzia e apre la finestra
     **/
    var openNewWindow = function(formPanel){
            //Ext.get('principale').update('');
            contatoreWindows++;
            /**
             * Pannello di comodo
             */
            _myGridPanel = new Ext.Panel({
                           region: 'center',
                           name: 'queryPanel',
                           layout:'fit',
                           autoScroll: true
            });

			//alert("1 loadAutogrid _myGridPanel.getId: " + _myGridPanel.getId());

            _myWindow = new Ext.Window({
                title:"Ricerca avanzata Host:"+_hostName +" DB: " + _showDatabaseName +" TABLE: " + _showTableName ,
                closable:true,
                width:850,
                height:690,
                modal: false,
                resizable: true,
                border:true,
                collapsible: true,
                plain:true,
                layout: 'border',
                //autoScroll: true,
                closeAction: 'close',
                //constrain: true,
                constrainHeader: true,
                buttonAlign:'center',
                items: [formPanel, _myGridPanel]
            });
            _myWindow.show();
            /*** NON VA Create a KeyMap
            var map = new Ext.KeyMap($('queryBody'), {
                key: Ext.EventObject.F9,
                //ctrl: true,
                fn: runQuery,
                scope: this
            });
            ***/
            
        }

    //Metodi pubblici    
    return {
        version : function(){
           Ext.MessageBox.alert("Version: " + ver);
        },
        openWindow : function(){
            loadAutoform();
        },
        viewTable : function(params){
			params.onBeforeSuccessLoadGrid = true;
            loadAutoform(params);
        }
    };
};
